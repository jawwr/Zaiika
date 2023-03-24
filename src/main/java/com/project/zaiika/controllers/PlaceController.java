package com.project.zaiika.controllers;

import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.services.placeServices.PlaceService;
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
    @GetMapping
    public ResponseEntity<?> getAllPlaces() {
        try {
            return ResponseEntity.ok(service.getAllPlaces());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
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
                                            example = """
                                                    {
                                                      "id": 0,
                                                      "name": "string",
                                                      "ownerId": 0,
                                                      "sites": []
                                                    }
                                                    """
                                    )
                            )
                    }
            )
    })
    @PostMapping
    public ResponseEntity<?> createPlace(@RequestBody Place place) {
        try {
            return ResponseEntity.ok(service.createPlace(place));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Удаление заведения по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
    @DeleteMapping("/{placeId}")
    public ResponseEntity<?> deletePlace(@PathVariable("placeId") Long placeId) {
        try {
            service.deletePlace(placeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
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
    @PutMapping("/{placeId}")
    public ResponseEntity<?> updatePlace(@PathVariable("placeId") Long placeId,
                                         @RequestBody Place place) {
        try {
            place.setId(placeId);
            service.updatePlace(place);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}