package com.zaiika.orderservice.controller;

import com.zaiika.orderservice.model.Order;
import com.zaiika.orderservice.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@Slf4j
@Tag(name = "Работа с заказами")
public class OrderController {//TODO
    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @Operation(summary = "Создание заказа",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Order.class
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
    @PreAuthorize("hasPermission(#token, 'VIEW_ORDER')")
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order,
                                         @RequestHeader("AUTHORIZATION") String token) {
        service.createOrder(order);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Отмена заказа")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
    @PreAuthorize("hasPermission(#token, 'VIEW_ORDER')")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderId") Long id,
                                         @RequestHeader("AUTHORIZATION") String token) {
//        service.cancelOrder(id);
//        return ResponseEntity.ok().build();
        return ResponseEntity.ok("cancel order " + id);
    }

    @Operation(summary = "Получение всех заказов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Order.class
                                            )
                                    )
                            )
                    }
            )
    })
//    @PreAuthorize("hasPermission(#token, 'MANAGE_ORDER')")
    @GetMapping
    public ResponseEntity<?> getAllOrders(@RequestHeader("AUTHORIZATION") String token) {
        return ResponseEntity.ok("it's work");
//        return ResponseEntity.ok(service.getOrders());
    }

    @Operation(summary = "Получение заказов по фильтру")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Order.class
                                            )
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasPermission(#token, 'MANAGE_ORDER')")
    @GetMapping("/filter")
    public ResponseEntity<?> getOrdersByDeliveryType(@RequestParam("delivery_type") String type,
                                                     @RequestHeader("AUTHORIZATION") String token) {
        return ResponseEntity.ok(service.getOrders(type));
    }
}
