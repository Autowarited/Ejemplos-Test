package com.micro.sale.imput.rs.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
/**
 * @author jrodriguez
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestFilter {

    @JsonProperty("number")
    private String number;

    @JsonProperty("customer")
    private Long customer;

    @JsonProperty("status")
    private Long status;

    @JsonProperty("category")
    private Integer idCategory;
}
