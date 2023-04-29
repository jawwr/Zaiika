package com.zaiika.placeservice.controller;

import com.zaiika.placeservice.model.Place;
import com.zaiika.placeservice.service.PlaceService;
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
@RequestMapping("/api/manage-place")
@Slf4j
@Tag(name = "Работа с заведениями")
public class PlaceController {//TODO сделать управление для владельцев
    private final PlaceService service;

    @Autowired
    public PlaceController(PlaceService service) {
        this.service = service;
    }

    @Operation(summary = "Получение списка заведений")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Place.class
                                            )
                                    )
                            )
                    }
            )
    })
//    @PreAuthorize("hasAnyAuthority('VIEW_PLACE')")
    @GetMapping
    public ResponseEntity<?> getAllPlaces() {
        return ResponseEntity.ok(service.getAllPlaces());
    }

    @Operation(summary = "Создание заведения",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"id\" : 0, \"name\" : \"string\" }"
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
                                            implementation = Place.class
                                    )
                            )
                    }
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_PLACE')")
    @PostMapping
    public ResponseEntity<?> createPlace(@RequestBody Place place) {
        return ResponseEntity.ok(service.createPlace(place));
    }

    @Operation(summary = "Удаление заведения по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_PLACE')")
    @DeleteMapping("/{placeId}")
    public ResponseEntity<?> deletePlace(@PathVariable("placeId") Long placeId) {
        service.deletePlace(placeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление заведения оп id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = "{ \"name\" : \"string\" }"
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_PLACE')")
    @PutMapping("/{placeId}")
    public ResponseEntity<?> updatePlace(@PathVariable("placeId") Long placeId,
                                         @RequestBody Place place) {
        place.setId(placeId);
        service.updatePlace(place);
        return ResponseEntity.ok().build();
    }
}