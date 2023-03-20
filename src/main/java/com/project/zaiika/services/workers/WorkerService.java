package com.project.zaiika.services.workers;

import com.project.zaiika.models.worker.WorkerDto;

import java.util.List;

public interface WorkerService {
    WorkerDto createWorker(WorkerDto worker);

    void updateWorker(WorkerDto newWorker);

    List<WorkerDto> getAllWorkers();

    WorkerDto getWorker(long workerId);

    void deleteWorker(long workerId);
    void addWorkerRole(long workerId, String roleName);
}
