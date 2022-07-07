package com.micro.sale.domain.usercase;

import com.micro.sale.domain.model.SaleOrder;
import com.micro.sale.imput.rs.request.OrderRequestFilter;
import com.micro.sale.imput.rs.response.OrderResponseList;
import org.springframework.data.domain.PageRequest;
import util.models.OrderRequest;

/**
 * @author jrodriguez
 */
public interface SaleOrderService {

    String saveOrder(SaleOrder order, OrderRequest inputOrder);

    SaleOrder getByNumber(String number);

    OrderResponseList getAllOrdersByPageAndFilter(PageRequest pageRequest, OrderRequestFilter filter);

    SaleOrder changeStatusOrder(String number, Long status) ;

    void delete(String number);
}
