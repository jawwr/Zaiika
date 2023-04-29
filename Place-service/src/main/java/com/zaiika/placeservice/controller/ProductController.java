package com.zaiika.placeservice.controller;

import com.zaiika.placeservice.model.Product;
import com.zaiika.placeservice.service.ProductService;
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
@RequestMapping("/api/menu/{menuId}/products")
@Slf4j
@Tag(name = "Работа с блюдами")
public class ProductController {
    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @Operation(summary = "Получение списка меню в заведении")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Product.class
                                            )
                                    )
                            )
                    }
            )
    })
//    @PreAuthorize("hasAnyAuthority('VIEW_PRODUCT')")
    @GetMapping
    public ResponseEntity<?> getMenu(@PathVariable("menuId") Long menuId) {
        return ResponseEntity.ok(service.getAllProductFromMenu(menuId));
    }

    @Operation(summary = "Создание блюд",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                      "title": "string",
                                                      "composition": [
                                                        {
                                                          "title": "string",
                                                          "netWeight": 0,
                                                          "grossWeight": 0
                                                        }
                                                      ],
                                                      "modifications": [
                                                        {
                                                          "modification": [
                                                            {
                                                              "title": "string",
                                                              "price": 0
                                                            }
                                                          ],
                                                          "title": "string"
                                                        }
                                                      ]
                                                    }"""
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = Product.class
                                    )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_PRODUCT')")
    @PostMapping
    public ResponseEntity<?> createProduct(@PathVariable("menuId") Long menuId,
                                           @RequestBody Product product) {
        return ResponseEntity.ok(service.addProductToMenu(menuId, product));
    }

    @Operation(summary = "удаление блюд")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_PRODUCT')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("menuId") Long menuId,
                                           @PathVariable("productId") Long productId) {
        service.deleteProductById(menuId, productId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление блюд",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                        "id" : 0,
                                                        "title" : "string",
                                                        "composition" : [
                                                            {
                                                                "id" : 0,
                                                                "title" : "string",
                                                                "netWeight" : 0,
                                                                "grossWeight": 0
                                                            }
                                                        ],
                                                        "modifications" : [
                                                            {
                                                                "id" : 0,
                                                                "modification": [
                                                                    {
                                                                        "id": 0,
                                                                        "title" : "string",
                                                                        "price" : 0
                                                                    }
                                                                ],
                                                                "title" : "string"
                                                            }
                                                        ]
                                                    }
                                                    """
                                    )
                            }
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_PRODUCT')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("menuId") Long menuId,
                                           @PathVariable("id") Long id,
                                           @RequestBody Product product) {
        product.setId(id);
        service.updateProduct(menuId, product);
        return ResponseEntity.ok().build();
    }
}
