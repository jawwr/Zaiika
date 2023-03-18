package com.project.zaiika.controllers;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.worker.WorkerDto;
import com.project.zaiika.services.workers.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage/workers")
@Slf4j
public class ManageWorkerController {
    private final WorkerService service;

    @Autowired
    public ManageWorkerController(WorkerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllWorkers() {
        try {
            return ResponseEntity.ok(service.getAllWorkers());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createWorker(@RequestBody WorkerDto worker) {
        try {
            service.createWorker(worker);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{workerId}")
    public ResponseEntity<?> updateWorker(@PathVariable("workerId") Long workerId,
                                          @RequestBody WorkerDto worker) {
        try {
            worker.setId(workerId);
            service.updateWorker(worker);
            return ResponseEntity.ok().build();
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{workerId}")
    public ResponseEntity<?> deleteWorker(@PathVariable("workerId") Long workerId) {
        try {
            service.deleteWorker(workerId);
            return ResponseEntity.ok().build();
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{workerId}")
    public ResponseEntity<?> getWorkerById(@PathVariable("workerId") Long workerId) {
        try {
            return ResponseEntity.ok(service.getWorker(workerId));
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{workerId}")
    public ResponseEntity<?> addWorkerRole(@PathVariable("workerId") Long workerId,
                                           @RequestParam(value = "roleName") String roleName) {
        try {
            service.addWorkerRole(workerId, roleName);
            return ResponseEntity.ok().build();
        } catch (PermissionDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
