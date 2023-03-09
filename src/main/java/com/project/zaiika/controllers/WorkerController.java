package com.project.zaiika.controllers;

import com.project.zaiika.models.worker.WorkerDto;
import com.project.zaiika.services.workers.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/place/{placeId}/workers")
@Slf4j
public class WorkerController {
    private final WorkerService service;

    @Autowired
    public WorkerController(WorkerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllWorkers(@PathVariable("placeId") Long placeId) {
        try {
            return ResponseEntity.ok(service.getAllWorkers(placeId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> createWorker(@PathVariable("placeId") Long placeId,
                                          @RequestBody WorkerDto worker) {
        try {
            worker.setPlaceId(placeId);
            service.createWorker(worker);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{workerId}")
    public ResponseEntity<?> updateWorker(@PathVariable("placeId") Long placeId,
                                          @PathVariable("workerId") Long workerId,
                                          @RequestBody WorkerDto worker) {
        try {
            worker.setId(workerId);
            worker.setPlaceId(placeId);
            service.updateWorker(worker);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{workerId}")
    public ResponseEntity<?> updateWorker(@PathVariable("placeId") Long placeId,
                                          @PathVariable("workerId") Long workerId) {
        try {
            service.deleteWorker(placeId, workerId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{workerId}")
    public ResponseEntity<?> getWorkerById(@PathVariable("placeId") Long placeId,
                                           @PathVariable("workerId") Long workerId) {
        try {
            return ResponseEntity.ok(service.getWorker(placeId, workerId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
