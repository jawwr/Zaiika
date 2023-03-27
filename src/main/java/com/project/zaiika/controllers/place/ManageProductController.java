package com.project.zaiika.controllers.place;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.placeModels.Product;
import com.project.zaiika.models.utils.ResponseMessage;
import com.project.zaiika.services.placeServices.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage/menu/{menuId}/products")
@Slf4j
@Tag(name = "Работа с блюдами")
public class ManageProductController {
    private final ProductService service;

    @Autowired
    public ManageProductController(ProductService service) {
        this.service = service;
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
    @PostMapping
    public ResponseEntity<?> createProduct(@PathVariable("menuId") Long menuId,
                                           @RequestBody Product product) {
        try {
            return ResponseEntity.ok(service.addProductToMenu(menuId, product));
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage(e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
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
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("menuId") Long menuId,
                                           @PathVariable("productId") Long productId) {
        try {
            service.deleteProductById(menuId, productId);
            return ResponseEntity.ok().build();
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage(e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
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
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("menuId") Long menuId,
                                           @PathVariable("id") Long id,
                                           @RequestBody Product product) {
        try {
            product.setId(id);
            service.updateProduct(menuId, product);
            return ResponseEntity.ok().build();
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage(e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
