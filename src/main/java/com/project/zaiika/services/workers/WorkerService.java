package com.project.zaiika.services.workers;

import com.project.zaiika.models.worker.WorkerDto;

import java.util.List;

public interface WorkerService {
    void createWorker(WorkerDto worker);

    void updateWorker(WorkerDto newWorker);

    List<WorkerDto> getAllWorkers(long placeId);

    WorkerDto getWorker(long placeId, long workerId);

    void deleteWorker(long placeId, long workerId);
    void addWorkerRole(long placeId, long workerId, String roleName);
}
