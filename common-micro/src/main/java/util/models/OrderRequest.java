package util.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import request.ProductRequest;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotNull
    @JsonProperty("customer")
    private Long customer;

    @NotNull
    @JsonProperty("date")
    @DateTimeFormat(pattern = "yyyy-M-d", iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate date;

    @NotNull
    @JsonProperty("status")
    private Long status;

    @JsonProperty("listProducts")
    private List<ProductRequest> listProducts;

    /**
     * id = 1 -> compra
     * id = 2 -> venta
     * id = 3 -> facturacion
     */
    @NotNull
    @JsonProperty("category")
    private Integer category;

    @JsonProperty("total")
    private Double total;

    @JsonProperty("description")
    private String description;

}
