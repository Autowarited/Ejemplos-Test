package com.micro.sale.domain.repository;

import com.micro.sale.domain.model.SaleOrder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author jrodriguez
 */
public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long>, JpaSpecificationExecutor<SaleOrder> {
    List<SaleOrder> findAll(Specification<SaleOrder> spec);

    @Query(value = "SELECT max(id) FROM SaleOrder")
    Long getLast();

    Optional<SaleOrder> getByNumber(String number);
}
