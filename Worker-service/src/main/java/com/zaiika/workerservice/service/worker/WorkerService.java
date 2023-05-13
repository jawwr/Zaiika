package com.zaiika.workerservice.service.worker;

import com.zaiika.workerservice.model.Worker;
import com.zaiika.workerservice.model.WorkerCredentials;

import java.util.List;

public interface WorkerService {
    Worker createWorker(WorkerCredentials worker);

    void updateWorker(WorkerCredentials newWorker);

    List<Worker> getAllWorkers();

    Worker getWorker(long workerId);

    void deleteWorker(long workerId);

    void addWorkerRole(long workerId, String roleName);

    boolean isWorker();

    boolean hasPermission(long userId, String permissionName);
}
