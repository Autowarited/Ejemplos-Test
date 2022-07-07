package com.micro.sale.imput.rs.controller;

import com.micro.sale.domain.usercase.SaleOrderService;
import com.micro.sale.imput.rs.api.ApiConstants;
import com.micro.sale.imput.rs.mapper.SaleOrderMapperController;
import com.micro.sale.imput.rs.request.OrderRequestFilter;
import com.micro.sale.imput.rs.response.OrderResponseList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import util.models.OrderRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

/**
 * @author jrodriguez
 */
@RestController()
@RequestMapping(ApiConstants.ORDER_URI)
@RequiredArgsConstructor
public class OrderController {

    private final SaleOrderService service;
    private final SaleOrderMapperController mapper;


    @Transactional
    @PostMapping("/sendOrder")
    public ResponseEntity<Void> saleOrder(@RequestBody OrderRequest inputOrder) {
        final String number = service.saveOrder(mapper.toSaleOrder(inputOrder), inputOrder);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{number}").buildAndExpand(number).toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{number}")
    @ResponseStatus(HttpStatus.OK)
    public void changeStatus(@Valid @NotNull @PathVariable String number, @Valid @NotNull @RequestParam Long status){
        service.changeStatusOrder(number, status);
    }

    @DeleteMapping("/{number}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@Valid @NotNull @PathVariable String number) {
        service.delete(number);
    }

    @GetMapping()
    public ResponseEntity<OrderResponseList> getOrders(@RequestParam(required = false) String number,
                                                      @RequestParam(required = false) Long customer,
                                                      @RequestParam(required = false) Long status,
                                                      @RequestParam(required = false, name = "category") Integer idCategory,
                                                      @RequestParam(defaultValue = "0", required = false) Integer page,
                                                      @RequestParam(defaultValue = "10", required = false) Integer size) {

        OrderRequestFilter filter = new OrderRequestFilter(number, customer, status, idCategory);
        OrderResponseList list = service.getAllOrdersByPageAndFilter(PageRequest.of(page, size), filter);
        return ResponseEntity.ok(list);
    }
}
