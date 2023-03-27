package com.project.zaiika.controllers.place;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.placeModels.Menu;
import com.project.zaiika.models.utils.ResponseMessage;
import com.project.zaiika.services.placeServices.MenuService;
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
@RequestMapping("/api/manage/site/{siteId}/menu")
@Slf4j
@Tag(name = "Работа с меню")
public class ManageMenuController {
    private final MenuService service;

    @Autowired
    public ManageMenuController(MenuService service) {
        this.service = service;
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
                                                        "title" : "some title",
                                                        "site" : {
                                                            ...
                                                        }
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
    @PostMapping
    public ResponseEntity<?> createNewMenu(@PathVariable("siteId") Long siteId,
                                           @RequestBody Menu menu) {
        try {
            return ResponseEntity.ok(service.createMenu(siteId, menu));
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage(e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
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
    @DeleteMapping("/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable("siteId") Long siteId,
                                        @PathVariable("menuId") Long menuId) {
        try {
            service.deleteMenu(siteId, menuId);
            return ResponseEntity.ok().build();
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage(e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
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
    @PutMapping("/{menuId}")
    public ResponseEntity<?> updateMenu(@PathVariable("siteId") Long siteId,
                                        @PathVariable("menuId") Long menuId,
                                        @RequestBody Menu menu) {
        try {
            menu.setId(menuId);
            service.updateMenu(siteId, menu);
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
