package com.project.zaiika.services.workers;

import com.project.zaiika.models.userModels.User;
import com.project.zaiika.models.worker.Worker;
import com.project.zaiika.models.worker.WorkerDto;
import com.project.zaiika.repositories.userRepositories.UserRepository;
import com.project.zaiika.repositories.worker.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {
    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void createWorker(Worker worker) {
        worker.setPassword(encoder.encode(worker.getPassword()));
        worker.setPin(encoder.encode(worker.getPin()));
        var user = new User(worker);
        workerRepository.save(worker);
        userRepository.save(user);
    }

    @Override
    public void updateWorker(Worker newWorker) {
        workerRepository.updateWorker(newWorker);
    }

    @Override
    public List<Worker> getAllWorkers(long placeId) {
        return workerRepository.findAllByPlaceId(placeId);
    }

    @Override
    public WorkerDto getWorker(long placeId, long workerId) {
        var worker = workerRepository.findWorkerByPlaceIdAndId(placeId, workerId);
        return new WorkerDto(worker);
    }

    @Override
    public void deleteWorker(long placeId, long workerId) {
        workerRepository.deleteWorkerByPlaceIdAndId(placeId, workerId);
    }
}
