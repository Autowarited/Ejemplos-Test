package com.micro.sale.IT;

import com.micro.sale.H2Config;
import com.micro.sale.imput.rs.api.ApiConstants;
import com.micro.sale.imput.rs.response.OrderResponseList;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import request.ProductRequest;
import util.AbstractTest;
import util.TemplateResponse;
import util.models.OrderRequest;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jrodriguez
 */

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringJUnitConfig(classes = H2Config.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderControllerIT extends AbstractTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @MockBean
    TemplateResponse templateResponse;

    @BeforeAll
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @Order(1)
    void createSaleOrder_shouldReturn_201() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
                .id(1)
                .amount(5)
                .price(20D)
                .build();

        OrderRequest request = OrderRequest.builder()
                .customer(1L)
                .date(LocalDate.of(2022, 7, 4))
                .status(1L)
                .category(2)
                .listProducts(Collections.singletonList(productRequest))
                .build();

        final String actualLocation = mockMvc.perform(post(ApiConstants.ORDER_URI.concat("/sendOrder"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(request)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getHeader(HttpHeaders.LOCATION);

        assertThat(actualLocation).isEqualTo("http://localhost/v1/sale/sendOrder/V-2022-7-10");
    }

    @Test
    @Order(2)
    void changeStatus_shouldReturn_200() throws Exception {
        mockMvc.perform(patch(ApiConstants.ORDER_URI.concat("/V-2022-7-10/?status=2"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(3)
    /**
     * Numero de Orden no encontrada en la DB.
     */
    void changeStatus_shouldReturn_404() throws Exception {
        mockMvc.perform(patch(ApiConstants.ORDER_URI.concat("/V-2022-7-11/?status=2"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    void getOrdersByNumber_shouldReturn_200() throws Exception {
        String content = mockMvc.perform(get(ApiConstants.ORDER_URI.concat("?number=V-2022-7-1")))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(content).isNotBlank();

        OrderResponseList responseList = mapToObject(content, OrderResponseList.class);

        assertThat(responseList.getTotalElements()).isEqualTo(1);
        assertThat(responseList.getTotalPages()).isEqualTo(1);
        assertThat(responseList.getNextUri()).isEqualTo("http://localhost/v1/sale?number=V-2022-7-1&page=1");
        assertThat(responseList.getPreviousUri()).isEqualTo("http://localhost/v1/sale?number=V-2022-7-1&page=0");
        assertThat(responseList.getContent()).isNotEmpty();

    }

    @Test
    @Order(5)
    void getOrdersByCustomer_shouldReturn_200() throws Exception {
        String content = mockMvc.perform(get(ApiConstants.ORDER_URI.concat("?customer=1")))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(content).isNotBlank();

        OrderResponseList responseList = mapToObject(content, OrderResponseList.class);

        assertThat(responseList.getTotalElements()).isEqualTo(4);
        assertThat(responseList.getTotalPages()).isEqualTo(1);
        assertThat(responseList.getNextUri()).isEqualTo("http://localhost/v1/sale?customer=1&page=1");
        assertThat(responseList.getPreviousUri()).isEqualTo("http://localhost/v1/sale?customer=1&page=0");
        assertThat(responseList.getContent()).isNotEmpty();

    }

    @Test
    @Order(6)
    void getOrdersByStatus_shouldReturn_200() throws Exception {
        String content = mockMvc.perform(get(ApiConstants.ORDER_URI.concat("?status=2")))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(content).isNotBlank();

        OrderResponseList responseList = mapToObject(content, OrderResponseList.class);

        assertThat(responseList.getTotalElements()).isEqualTo(3);
        assertThat(responseList.getTotalPages()).isEqualTo(1);
        assertThat(responseList.getNextUri()).isEqualTo("http://localhost/v1/sale?status=2&page=1");
        assertThat(responseList.getPreviousUri()).isEqualTo("http://localhost/v1/sale?status=2&page=0");
        assertThat(responseList.getContent()).isNotEmpty();

    }

    @Test
    @Order(7)
    void getOrdersBySize_2_shouldReturn_200() throws Exception {
        String content = mockMvc.perform(get(ApiConstants.ORDER_URI.concat("?size=2")))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(content).isNotBlank();

        OrderResponseList responseList = mapToObject(content, OrderResponseList.class);

        assertThat(responseList.getTotalElements()).isEqualTo(10);
        assertThat(responseList.getTotalPages()).isEqualTo(5);
        assertThat(responseList.getNextUri()).isEqualTo("http://localhost/v1/sale?size=2&page=1");
        assertThat(responseList.getPreviousUri()).isEqualTo("http://localhost/v1/sale?size=2&page=0");
        assertThat(responseList.getContent()).isNotEmpty();

    }

    @Test
    @Order(8)
    void getOrders_Size2_Page2_shouldReturn_200() throws Exception {
        String content = mockMvc.perform(get(ApiConstants.ORDER_URI.concat("?size=2&page=2")))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(content).isNotBlank();

        OrderResponseList responseList = mapToObject(content, OrderResponseList.class);

        assertThat(responseList.getTotalElements()).isEqualTo(10);
        assertThat(responseList.getTotalPages()).isEqualTo(5);
        assertThat(responseList.getNextUri()).isEqualTo("http://localhost/v1/sale?size=2&page=3");
        assertThat(responseList.getPreviousUri()).isEqualTo("http://localhost/v1/sale?size=2&page=1");
        assertThat(responseList.getContent()).isNotEmpty();

    }

    @Test
    @Order(9)
    void deleteOrder_shouldReturn_400() throws Exception {
        mockMvc.perform(patch(ApiConstants.ORDER_URI.concat("/V-2022-7-10/?status=1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        mockMvc.perform(delete(ApiConstants.ORDER_URI.concat("/V-2022-7-10"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Order(10)
    void deleteOrder_shouldReturn_204() throws Exception {
        mockMvc.perform(patch(ApiConstants.ORDER_URI.concat("/V-2022-7-10/?status=2"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        mockMvc.perform(delete(ApiConstants.ORDER_URI.concat("/V-2022-7-10"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


}
