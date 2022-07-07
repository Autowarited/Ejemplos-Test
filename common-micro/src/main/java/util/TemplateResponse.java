package util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import request.ProductRequest;
import util.models.ItemResponse;
import util.models.OrderRequest;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TemplateResponse {

    private final RestTemplate restTemplate;

    private final String URI_PROCESS_ORDER = "http://localhost:8082/item";
   private final  String URI_CALC_TOTAL = "http://localhost:8085/products/calcTotal";

    public TemplateResponse() {
        this.restTemplate = new RestTemplate();
    }

    public List<ItemResponse> setItems(OrderRequest inputOrder) {
        Long[] idItems = restTemplate.postForObject(URI_PROCESS_ORDER, inputOrder, Long[].class);

        return Arrays.stream(idItems).map(ItemResponse::new).toList();
    }

    public Double calcTotal(List<ProductRequest> listProducts) {
        return restTemplate.postForObject(URI_CALC_TOTAL, listProducts, Double.class);
    }

}
