package com.micro.sale.domain.usercase;

import com.micro.sale.domain.model.SaleOrder;
import com.micro.sale.domain.model.SaleOrderItem;
import com.micro.sale.domain.model.SaleOrderList;
import com.micro.sale.domain.repository.SaleOrderItemRepository;
import com.micro.sale.domain.repository.SaleOrderRepository;
import com.micro.sale.imput.rs.api.ApiConstants;
import com.micro.sale.imput.rs.mapper.SaleOrderMapperController;
import com.micro.sale.imput.rs.request.OrderRequestFilter;
import com.micro.sale.imput.rs.response.OrderResponseList;
import com.micro.sale.imput.rs.specification.OrderSpecification;
import errors.DeletionInvalidException;
import errors.NotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.NumberGenerator;
import util.TemplateResponse;
import util.models.OrderRequest;

/**
 * @author jrodriguez
 */
@Service
@RequiredArgsConstructor
public class SaleOrderServiceImpl implements SaleOrderService {
    private final SaleOrderRepository repo;
    private final SaleOrderItemRepository repoOrderItem;
    private final NumberGenerator generator;
    private final SaleOrderMapperController mapper;
    private final TemplateResponse restTemplate;
    private final OrderSpecification spec;


    @Transactional
    @Override
    public String saveOrder(SaleOrder order, OrderRequest inputOrder) {
        String number = generator.generateNumberOrder(order.getIdCategory(), repo.getLast());
        order.setNumber(number);
        order.setTotal(restTemplate.calcTotal(inputOrder.getListProducts()));
        repo.save(order);
        saveOrderItem(inputOrder, number);

        return number;
    }

    private void saveOrderItem(OrderRequest inputOrder, String number) {
        Long idOrder = getByNumber(number).getId();

        restTemplate.setItems(inputOrder).forEach(idItem -> {
            SaleOrderItem orderItem = SaleOrderItem.builder()
                    .idOrder(idOrder)
                    .idItem(idItem.getId())
                    .build();
            repoOrderItem.save(orderItem);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public SaleOrder getByNumber(String number) {
        return repo.getByNumber(number).orElseThrow(() -> new NotFoundException(number));
    }


    @Transactional
    @Override
    public SaleOrder changeStatusOrder(String number, Long status){
        SaleOrder order = getByNumber(number);
        order.setStatus(status);
       return repo.save(order);
    }

    @Transactional
    @Override
    public void delete(String order) {
        SaleOrder orderExist = getByNumber(order);
        if (orderExist.getStatus() != 2) throw new DeletionInvalidException(order);
        else repo.delete(orderExist);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponseList getAllOrdersByPageAndFilter(PageRequest pageRequest, OrderRequestFilter filter) {
        Specification<SaleOrder> orderSpec = spec.getAllBySpec(filter);
        Page<SaleOrder> page = repo.findAll(orderSpec, pageRequest);
        SaleOrderList saleOrderList = new SaleOrderList(page.getContent(), pageRequest, page.getTotalElements());
        return buildSaleOrderList(saleOrderList);
    }

    private OrderResponseList buildSaleOrderList(SaleOrderList saleOrderList) {
        return OrderResponseList.builder()
                .content(mapper.listOrderToListOrderResponse(saleOrderList.getContent()))
                .totalPages(saleOrderList.getTotalPages())
                .totalElements(saleOrderList.getTotalElements())
                .nextUri(getNexPage(saleOrderList))
                .previousUri(getPreviousPage(saleOrderList))
                .build();
    }

    private String getPreviousPage(SaleOrderList saleOrderList) {
        final int nextPage = saleOrderList.getPageable().previousOrFirst().getPageNumber();
        return ApiConstants.uriByPageAsString.apply(nextPage);
    }

    private String getNexPage(SaleOrderList saleOrderList) {
        final int nextPage = saleOrderList.getPageable().next().getPageNumber();
        return ApiConstants.uriByPageAsString.apply(nextPage);
    }
}
