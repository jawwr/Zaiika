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
import java.util.Random;

//TODO TODO TODO !!!
@Service
@RequiredArgsConstructor
public class WorkerServiceImpl extends WorkerServiceGrpc.WorkerServiceImplBase implements WorkerService {
    private final WorkerRepository workerRepository;
    private final UserService userService;
    private final PlaceRoleRepository placeRoleRepository;

    @Override
    @Transactional
    public Worker createWorker(WorkerCredentials workerDto) {//TODO
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

    private String generateLoginFromWorkerDto(WorkerCredentials dto) {
        Random random = new Random();
        return "w" +
                random.nextInt(1000) +
                dto.name().toCharArray()[0] +
                dto.name().toCharArray()[dto.name().length() - 1] +
                dto.name().length() +
                dto.surname().toCharArray()[0] +
                dto.surname().toCharArray()[dto.surname().length() - 1] +
                dto.surname().length() +
                dto.patronymic().toCharArray()[0] +
                dto.patronymic().toCharArray()[dto.patronymic().length() - 1] +
                dto.patronymic().length() +
                dto.placeId();
    }

    @Override
    public void updateWorker(WorkerCredentials updateWorker) {
//        checkPermission(updateWorker.getId());
//
//        validatePinCode(updateWorker.getPinCode());
//        updateWorker.setPinCode(encoder.encode(updateWorker.getPinCode()));

//        var worker = workerRepository.findById(updateWorker.getId());
//        saveWorker(worker.getUser(), updateWorker);
//
//        var user = userJpaRepository.getUserById(worker.getId());
//        var userWorker = convertWorkerDtoToUser(updateWorker, false);
//        userWorker.setLogin(user.getLogin());
//        userJpaRepository.save(userWorker);
    }

    @Override
    public List<Worker> getAllWorkers() {
        var user = userService.getUser();
        var placeId = user.placeId();

//        var place = ctx.getPlace();
//        var workers = workerRepository.findAllByPlaceId(place.getId()); // place.getWorkers();

//        return generateWorkersDto(workers);
        return null;
    }

    private List<Worker> generateWorkersDto(List<Worker> workers) {
//        return workers.stream().map(this::generateWorkerDto).toList();
        return null;
    }

//    private WorkerDto generateWorkerDto(Worker worker) {
//        var user = worker.getUser();
//        return WorkerDto.builder()
//                .id(worker.getId())
//                .name(user.getName())
//                .surname(user.getSurname())
//                .patronymic(user.getPatronymic())
//                .role(worker.getPlaceRole() != null
//                        ? worker.getPlaceRole().getName()
//                        : UserRole.WORKER.name())
//                .placeRoleId(worker.getPlaceRole().getId())
//                .build();
//    }

    @Override
    public Worker getWorker(long workerId) {
//        checkPermission(workerId);
        var worker = workerRepository.findById(workerId);

//        return generateWorkerDto(worker);
        return null;
    }

    @Override
    public void deleteWorker(long workerId) {
//        checkPermission(workerId);

        workerRepository.deleteWorkerById(workerId);
    }

    @Override
    public void addWorkerRole(long workerId, String roleName) {
//        checkPermission(workerId);
//        if (UserRole.ADMIN.name().equals(roleName.toUpperCase())) {
//            addAdminRole(workerId);
//        } else {
//            addPlaceRole(workerId, roleName);
//        }
    }

    @Override//TODO
    public boolean isWorker() {
        return false;
    }

    @Override
    public boolean hasPermission(long userId, String permissionName) {
        var worker = workerRepository.getWorkerByUserId(userId);
        var placesRole = worker.getPlaceRole();
        return placesRole.getPermissions().stream().anyMatch(x -> x.getName().equals(permissionName));
    }

    private void addPlaceRole(long workerId, String roleName) {
//        var place = ctx.getPlace();
//        var role = placeRoleRepository.findPlaceRoleByPlaceIdAndNameIgnoreCase(place.getId(), roleName);
//        if (role == null) {
//            throw new IllegalArgumentException("Wrong role name");
//        }

//        var worker = workerRepository.findById(workerId);
//        worker.setPlaceRole(role);
//        workerRepository.save(worker);
    }

    private void addAdminRole(long workerId) {
//        var worker = workerRepository.findById(workerId);
//        var adminRole = roleRepository.findRoleByName(UserRole.ADMIN.name());
//        worker.getUser().getRoles().add(adminRole);
//        worker.setPlaceRole(null);
//
//        workerRepository.save(worker);
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
}
