package com.zaiika.deliveryservice.controller;

import com.zaiika.deliveryservice.model.Delivery;
import com.zaiika.deliveryservice.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
@Slf4j
@Tag(name = "Управление способами доставки")
public class DeliveryController {
    private final DeliveryService service;

    @Autowired
    public DeliveryController(DeliveryService service) {
        this.service = service;
    }

    @Operation(summary = "Получение всех способов доставки")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Delivery.class
                                            )
                                    )
                            )
                    }
            )
    })
//    @PreAuthorize("hasAnyAuthority('VIEW_DELIVERY')")
    @GetMapping
    public ResponseEntity<?> getExistDelivery() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Добавление новой доставки", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{ \"deliveryType\" : \"some type\"}"
                    )
            )
    ))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = Delivery.class
                                    )
                            )
                    }
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_DELIVERY')")
    @PostMapping
    public ResponseEntity<?> createDelivery(@RequestBody Delivery delivery) {
        return ResponseEntity.ok(service.create(delivery));
    }

    @Operation(summary = "Изменение способа доставки",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"deliveryType\": \"string\" }"
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_DELIVERY')")
    @PutMapping("/{deliveryId}")
    public ResponseEntity<?> updateDelivery(@PathVariable("deliveryId") Long id,
                                            @RequestBody Delivery delivery) {
        service.updateDelivery(id, delivery);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Изменение способа доставки")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_DELIVERY')")
    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<?> deleteDelivery(@PathVariable("deliveryId") Long id) {
        service.deleteDelivery(id);
        return ResponseEntity.ok().build();
    }
}
