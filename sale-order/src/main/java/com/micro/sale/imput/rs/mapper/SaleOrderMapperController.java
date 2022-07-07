package com.micro.sale.imput.rs.mapper;

import com.micro.sale.domain.model.SaleOrder;
import com.micro.sale.imput.rs.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import util.models.OrderRequest;

import java.util.List;
/**
 * @author jrodriguez
 */
@Mapper()
public interface SaleOrderMapperController {

    List<OrderResponse> listOrderToListOrderResponse(List<SaleOrder> content);

    OrderResponse toOrderResponse (SaleOrder order);

    @Mapping(source = "category", target = "idCategory")
    SaleOrder toSaleOrder (OrderRequest request);
}
