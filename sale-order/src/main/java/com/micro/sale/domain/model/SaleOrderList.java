package com.micro.sale.domain.model;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author jrodriguez
 */
public class SaleOrderList extends PageImpl<SaleOrder> {
    public SaleOrderList(List<SaleOrder> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
