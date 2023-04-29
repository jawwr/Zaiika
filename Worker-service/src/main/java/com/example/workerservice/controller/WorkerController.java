package com.example.workerservice.controller;

import com.example.workerservice.model.WorkerDto;
import com.example.workerservice.service.WorkerService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workers")
@Slf4j
@Tag(name = "Управление работниками")
public class WorkerController {
    private final WorkerService service;

    @Autowired
    public WorkerController(WorkerService service) {
        this.service = service;
    }

    @Operation(summary = "Получение всех работников заведения")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = WorkerDto.class
                                            )
                                    )
                            )
                    }
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_WORKER')")
    @GetMapping
    public ResponseEntity<?> getAllWorkers() {
        return ResponseEntity.ok(service.getAllWorkers());
    }

    @Operation(summary = "Создание нового работника")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = WorkerDto.class
                                    )
                            )
                    }
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_WORKER')")
    @PostMapping
    public ResponseEntity<?> createWorker(@RequestBody WorkerDto worker) {
        return ResponseEntity.ok(service.createWorker(worker));
    }

    @Operation(summary = "Обновление работника по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_WORKER')")
    @PutMapping("/{workerId}")
    public ResponseEntity<?> updateWorker(@PathVariable("workerId") Long workerId,
                                          @RequestBody WorkerDto worker) {
        worker.setId(workerId);
        service.updateWorker(worker);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление работника заведения по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_WORKER')")
    @DeleteMapping("/{workerId}")
    public ResponseEntity<?> deleteWorker(@PathVariable("workerId") Long workerId) {
        service.deleteWorker(workerId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение работника заведения по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = WorkerDto.class
                                    )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_WORKER')")
    @GetMapping("/{workerId}")
    public ResponseEntity<?> getWorkerById(@PathVariable("workerId") Long workerId) {
        return ResponseEntity.ok(service.getWorker(workerId));
    }

    @Operation(summary = "Изменение роли заведения у работника")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "permissionDenied"
            )
    })
//    @PreAuthorize("hasAnyAuthority('MANAGE_WORKER')")
    @PostMapping("/{workerId}")
    public ResponseEntity<?> addWorkerRole(@PathVariable("workerId") Long workerId,
                                           @RequestParam(value = "roleName") String roleName) {
        service.addWorkerRole(workerId, roleName);
        return ResponseEntity.ok().build();
    }
}
