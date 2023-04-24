package com.project.zaiika.controllers.place;

import com.project.zaiika.models.placeModels.Site;
import com.project.zaiika.services.placeServices.SiteService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/site")
@Slf4j
@Tag(name = "Работа с точками продажи")
public class SiteController {
    private final SiteService service;

    @Autowired
    public SiteController(SiteService service) {
        this.service = service;
    }

    @Operation(summary = "Получение списка точек продаж в заведении")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Site.class
                                            )
                                    )
                            )
                    }
            )
    })
    @PreAuthorize("hasAnyAuthority('VIEW_SITE')")
    @GetMapping
    public ResponseEntity<?> getAllSites() {
        return ResponseEntity.ok(service.getAllSites());
    }

    @Operation(summary = "Создание новой точки продажи",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                        "name": "some name"
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
                                                        "name" : "some name",
                                                        "place" : {
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
    @PreAuthorize("hasAnyAuthority('MANAGE_SITE')")
    @PostMapping
    public ResponseEntity<?> createSite(@RequestBody Site site) {
        return ResponseEntity.ok(service.createSite(site));
    }

    @Operation(summary = "Обновление точки продажи по id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                        "name" : "some new name"
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
    @PreAuthorize("hasAnyAuthority('MANAGE_SITE')")
    @PutMapping("/{siteId}")
    public ResponseEntity<?> updateSite(@PathVariable("siteId") Long siteId, @RequestBody Site site) {
        site.setId(siteId);
        service.updateSite(site);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление точки продажи")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
    @PreAuthorize("hasAnyAuthority('MANAGE_SITE')")
    @DeleteMapping("/{siteId}")
    public ResponseEntity<?> deleteSite(@PathVariable("siteId") Long siteId) {
        service.deleteSite(siteId);
        return ResponseEntity.ok().build();
    }
}
