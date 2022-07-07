package com.micro.sale.imput.rs.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author jrodriguez
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseList {
    @JsonProperty("content")
    private List<OrderResponse> content = null;

    @JsonProperty("nextUri")
    private String nextUri;

    @JsonProperty("previousUri")
    private String previousUri;

    @JsonProperty("totalPages")
    private Integer totalPages;

    @JsonProperty("totalElements")
    private Long totalElements;
}
