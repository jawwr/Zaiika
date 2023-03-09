package com.project.zaiika.services.workers;

import com.project.zaiika.models.worker.Worker;
import com.project.zaiika.models.worker.WorkerDto;

import java.util.List;

public interface WorkerService {
    void createWorker(Worker worker);
    void updateWorker(Worker newWorker);
    List<Worker> getAllWorkers(long placeId);
    WorkerDto getWorker(long placeId, long workerId);
    void deleteWorker(long placeId, long workerId);
}
