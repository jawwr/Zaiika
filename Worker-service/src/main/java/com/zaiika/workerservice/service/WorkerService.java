package com.zaiika.workerservice.service;

import com.zaiika.workerservice.model.WorkerDto;

import java.util.List;

public interface WorkerService {
    WorkerDto createWorker(WorkerDto worker);

    void updateWorker(WorkerDto newWorker);

    List<WorkerDto> getAllWorkers();

    WorkerDto getWorker(long workerId);

    void deleteWorker(long workerId);

    void addWorkerRole(long workerId, String roleName);

    boolean isWorker();
}
