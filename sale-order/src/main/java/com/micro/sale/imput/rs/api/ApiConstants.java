package com.micro.sale.imput.rs.api;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.function.Function;

/**
 * @author jrodriguez
 */
public interface ApiConstants {

    String ORDER_URI = "/v1/sale";

    Function<Integer, String> uriByPageAsString = (page) ->
            ServletUriComponentsBuilder.fromCurrentRequest()
                    .replaceQueryParam("page", page).toUriString();
}
