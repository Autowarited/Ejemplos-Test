package com.micro.sale.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author jrodriguez
 */
@Table(name = "sale_order")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "customer")
    private Long customer;

    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd ")
    private LocalDate date;

    @Column(name = "status")
    private Long status;

    @Column(name = "category")
    private Integer idCategory;

    @Column(name = "total")
    private Double total;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleOrder order = (SaleOrder) o;
        return Objects.equals(id, order.id)
                && Objects.equals(number, order.number)
                && Objects.equals(customer, order.customer)
                && Objects.equals(date, order.date)
                && Objects.equals(status, order.status)
                && Objects.equals(idCategory, order.idCategory)
                && Objects.equals(total, order.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
