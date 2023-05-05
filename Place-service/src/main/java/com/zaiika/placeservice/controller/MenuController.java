package com.zaiika.placeservice.controller;

import com.zaiika.placeservice.model.place.Menu;
import com.zaiika.placeservice.service.place.MenuService;
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
@RequestMapping("/api/site/{siteId}/menu")
@Slf4j
@Tag(name = "Работа с меню")
public class MenuController {
    private final MenuService service;

    @Autowired
    public MenuController(MenuService service) {
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
                                                    implementation = Menu.class
                                            )
                                    )
                            )
                    }
            )
    })
//    @PreAuthorize("hasAnyAuthority('VIEW_MENU')")
    @GetMapping
    public ResponseEntity<?> getAllMenus(@PathVariable("siteId") Long placeId) {
        return ResponseEntity.ok(service.getAllMenus(placeId));
    }

    @Operation(summary = "Создание меню",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                        "title" : "some title"
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
                                            example = """
                                                    {
                                                        "id" : 0,
                                                        "title" : "some title"
                                                    }"""
                                    )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_MENU')")
    @PostMapping
    public ResponseEntity<?> createNewMenu(@PathVariable("siteId") Long siteId,
                                           @RequestBody Menu menu) {
        return ResponseEntity.ok(service.createMenu(siteId, menu));
    }

    @Operation(summary = "Удаление меню")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_MENU')")
    @DeleteMapping("/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable("siteId") Long siteId,
                                        @PathVariable("menuId") Long menuId) {
        service.deleteMenu(siteId, menuId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление меню",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                        "title" : "some title"
                                                    }"""
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
//    @PreAuthorize("hasAnyAuthority('MANAGE_MENU')")
    @PutMapping("/{menuId}")
    public ResponseEntity<?> updateMenu(@PathVariable("siteId") Long siteId,
                                        @PathVariable("menuId") Long menuId,
                                        @RequestBody Menu menu) {
        menu.setId(menuId);
        service.updateMenu(siteId, menu);
        return ResponseEntity.ok().build();
    }
}
