package com.zaiika.workerservice.service.worker;

import com.zaiika.worker.WorkerServiceGrpc;
import com.zaiika.worker.WorkerServiceOuterClass;
import com.zaiika.workerservice.model.Worker;
import com.zaiika.workerservice.model.WorkerCredentials;
import com.zaiika.workerservice.repository.PlaceRoleRepository;
import com.zaiika.workerservice.repository.WorkerRepository;
import com.zaiika.workerservice.service.user.UserService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//TODO caching
@Service
@RequiredArgsConstructor
public class WorkerServiceImpl extends WorkerServiceGrpc.WorkerServiceImplBase implements WorkerService {
    private final WorkerRepository workerRepository;
    private final UserService userService;
    private final PlaceRoleRepository placeRoleRepository;

    @Override
    @Transactional
    public Worker createWorker(WorkerCredentials workerDto) {
        var user = userService.getUser();
        var placeRole = placeRoleRepository
                .findPlaceRoleByPlaceIdAndNameIgnoreCase(user.placeId(), workerDto.placeRole());

        validatePinCode(workerDto.pin());
        var userId = userService.saveWorker(workerDto);
        Worker worker = Worker
                .builder()
                .placeId(user.placeId())
                .userId(userId)
                .placeRole(placeRole)
                .build();

        return workerRepository.save(worker);
    }

    @Override
    public List<Worker> getAllWorkers() {
        var placeId = userService.getUser().placeId();
        return workerRepository.findAllByPlaceId(placeId);
    }

    @Override
    public Worker getWorker(long workerId) {
        var workers = getPlaceWorkers();

        return workers.stream()
                .filter(x -> x.getId() == workerId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Worker does not exist"));
    }

    @Override
    @Transactional
    public void deleteWorker(long workerId) {
        var workers = getPlaceWorkers();
        var isWorkerExist = workers.stream().anyMatch(x -> x.getId() == workerId);
        if (!isWorkerExist) {
            throw new IllegalArgumentException("Worker does not exist");
        }
        workerRepository.deleteWorkerById(workerId);
    }

    @Override
    @Transactional
    public void addWorkerRole(long workerId, String roleName) {
        var placeId = userService.getUser().placeId();
        var worker = workerRepository.findById(workerId);
        if (placeId != worker.getPlaceId()) {
            throw new IllegalArgumentException("Worker does not exist");
        }
        var placeRole = placeRoleRepository.findPlaceRoleByPlaceIdAndNameIgnoreCase(placeId, roleName);
        if (placeRole == null) {
            throw new IllegalArgumentException("Role does not exist");
        }
        worker.setPlaceRole(placeRole);

        workerRepository.save(worker);
    }

    @Override
    public boolean hasPermission(long userId, String permissionName) {
        var worker = workerRepository.getWorkerByUserId(userId);
        var placesRole = worker.getPlaceRole();
        return placesRole.getPermissions().stream().anyMatch(x -> x.getName().equals(permissionName));
    }

    private void validatePinCode(String pin) {
        if (!pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("Pin code must be of 4 digits");
        }
    }

    @Override
    public void hasPermission(WorkerServiceOuterClass.HasPermissionRequest request,
                              StreamObserver<WorkerServiceOuterClass.HasPermissionResponse> responseObserver) {
        var hasPermission = hasPermission(request.getUserId(), request.getPermission());

        var response = WorkerServiceOuterClass.HasPermissionResponse
                .newBuilder()
                .setHasPermission(hasPermission)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private List<Worker> getPlaceWorkers() {
        var placeId = userService.getUser().placeId();
        return workerRepository.findAllByPlaceId(placeId);
    }
}
