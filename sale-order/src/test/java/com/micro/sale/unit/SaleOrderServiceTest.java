package com.micro.sale.unit;

import com.micro.sale.domain.model.SaleOrder;
import com.micro.sale.domain.model.SaleOrderItem;
import com.micro.sale.domain.repository.SaleOrderItemRepository;
import com.micro.sale.domain.repository.SaleOrderRepository;
import com.micro.sale.domain.usercase.SaleOrderServiceImpl;
import com.micro.sale.imput.rs.mapper.SaleOrderMapperController;
import errors.DeletionInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import request.ProductRequest;
import util.AbstractTest;
import util.NumberGenerator;
import util.models.OrderRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SaleOrderServiceTest extends AbstractTest {

    @InjectMocks
    SaleOrderServiceImpl impl;
    @Mock
    SaleOrderRepository repo;
    @Mock
    SaleOrderItemRepository repoOrderItem;
    @Spy
    NumberGenerator generator = new NumberGenerator();
    @Spy
    SaleOrderMapperController mapper = Mappers.getMapper(SaleOrderMapperController.class);

    static ProductRequest productRequest;
    static OrderRequest orderRequest;
    static String number;

    @BeforeEach
    void setUp() {
        productRequest = ProductRequest.builder()
                .id(1)
                .amount(5)
                .price(20D)
                .build();
        orderRequest = OrderRequest.builder()
                .customer(1L)
                .date(LocalDate.of(2022, 7, 4))
                .status(1L)
                .category(2)
                .listProducts(List.of(productRequest))
                .build();
        number = "V-2022-7-2";
    }

    @Test
    void repo_saveOrder() {
        SaleOrder order = mapper.toSaleOrder(orderRequest);
        order.setTotal(1000D);

        given(repo.save(any(SaleOrder.class))).willReturn(order);

        assertThat(repo.save(order)).isEqualTo(order);
        assertThat(repo.save(order).getStatus()).isEqualTo(1L);
        assertThat(repo.save(order).getCustomer()).isEqualTo(1L);
        assertThat(repo.save(order).getIdCategory()).isEqualTo(2);
        assertThat(repo.save(order).getTotal()).isEqualTo(1000D);
    }

    @Test
    void generateNumberOrder() {
        given(generator.generateNumberOrder(2, 1L)).willReturn(number);
        assertThat(generator.generateNumberOrder(2, 1L)).isEqualTo(number);
    }

    @Test
    void saveOrderItem() {
        SaleOrderItem orderItem = SaleOrderItem.builder()
                .idItem(1L)
                .idOrder(2L)
                .build();
        given(repoOrderItem.save(any(SaleOrderItem.class))).willReturn(orderItem);
        assertThat(repoOrderItem.save(orderItem).getIdOrder()).isEqualTo(2L);
        assertThat(repoOrderItem.save(orderItem).getIdItem()).isEqualTo(1L);
    }

    @Test
    void getByNumber() {
        SaleOrder order = mapper.toSaleOrder(orderRequest);
        given(repo.getByNumber(number)).willReturn(Optional.ofNullable(order));
        assertThat(impl.getByNumber(number)).isEqualTo(order);
    }

    @Test
    void changeStatusOrder() {
        SaleOrder order = mapper.toSaleOrder(orderRequest);
        given(repo.getByNumber(number)).willReturn(Optional.ofNullable(order));
        given(repo.save(any(SaleOrder.class))).willReturn(order);
        assertThat(impl.changeStatusOrder(number, 2L).getStatus()).isEqualTo(2L);
    }

    @Test
    void deleteOK() {
        SaleOrder order = mapper.toSaleOrder(orderRequest);
        given(repo.getByNumber(number)).willReturn(Optional.ofNullable(order));
        impl.changeStatusOrder(number, 2L);
        assert order != null;
        assertThat(order.getStatus()).isEqualTo(2L);
    }

    @Test
    void deleteError() {
        SaleOrder order = mapper.toSaleOrder(orderRequest);
        given(repo.getByNumber(number)).willReturn(Optional.ofNullable(order));
        assertThatThrownBy(() -> impl.delete(number)).hasNoCause();
        assertThat(impl.getByNumber(number).getNumber()).isNull();
    }
}
