package com.project.zaiika.services.workers;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.userModels.User;
import com.project.zaiika.models.worker.Worker;
import com.project.zaiika.models.worker.WorkerDto;
import com.project.zaiika.repositories.userRepositories.PlaceRoleRepository;
import com.project.zaiika.repositories.userRepositories.UserRepository;
import com.project.zaiika.repositories.worker.WorkerRepository;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {
    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final PlaceRoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final ContextService ctx;

    @Override
    public WorkerDto createWorker(WorkerDto workerDto) {
        var place = ctx.getPlace();
        workerDto.setPlaceId(place.getId());

        validatePinCode(workerDto.getPinCode());
        workerDto.setPinCode(encoder.encode(workerDto.getPinCode()));
        var user = new User(workerDto);
        var userId = userRepository.save(user).getId();

        var worker = Worker.builder()
                .userId(userId)
                .placeRoleId(workerDto.getPlaceRoleId())
                .placeId(workerDto.getPlaceId())
                .build();
        var savedUser = workerRepository.save(worker);
        workerDto.setId(savedUser.getId());
        workerDto.setPlaceId(savedUser.getPlaceId());
        workerDto.setRole(user.getRole().getName());
        return workerDto;
    }

    @Override
    public void updateWorker(WorkerDto newWorker) {
        checkPermission(newWorker.getId());

        validatePinCode(newWorker.getPinCode());
        var worker = Worker.builder()
                .placeId(newWorker.getPlaceId())
                .placeRoleId(newWorker.getPlaceRoleId())
                .build();

        workerRepository.updateWorker(worker);

        long userId = workerRepository.findById(newWorker.getId()).getUserId();

        var user = userRepository.findUserById(userId);
        user.setName(newWorker.getName());
        user.setSurname(newWorker.getSurname());
        user.setPatronymic(newWorker.getPatronymic());
        userRepository.updateUser(user);
    }

    @Override
    public List<WorkerDto> getAllWorkers() {
        var place = ctx.getPlace();

        var workers = workerRepository.findAllByPlaceId(place.getId());
        List<Long> usersId = workers.stream().map(Worker::getUserId).toList();
        var users = userRepository.findUserByIds(usersId);
        List<WorkerDto> dtoUsers = new ArrayList<>();

        for (Worker worker : workers) {
            var user = users.stream().filter(x -> x.getId() == worker.getUserId()).findFirst().get();
            var dto = WorkerDto.builder()
                    .id(worker.getId())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .patronymic(user.getPatronymic())
                    .role(worker.getPlaceRoleId() != 0
                            ? roleRepository.findPlaceRoleById(worker.getPlaceRoleId()).getName()
                            : "")
                    .placeRoleId(worker.getPlaceRoleId())
                    .build();

            dtoUsers.add(dto);
        }
        return dtoUsers;
    }

    @Override
    public WorkerDto getWorker(long workerId) {
        checkPermission(workerId);
        var place = ctx.getPlace();

        var worker = workerRepository.findWorkerByPlaceIdAndId(place.getId(), workerId);
        var user = userRepository.findUserById(worker.getUserId());
        var role = roleRepository.findPlaceRoleById(worker.getPlaceRoleId());

        return WorkerDto.builder()
                .id(worker.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .patronymic(user.getPatronymic())
                .placeId(place.getId())
                .role(role.getName())
                .build();
    }

    @Override
    public void deleteWorker(long workerId) {
        checkPermission(workerId);
        var place = ctx.getPlace();

        workerRepository.deleteWorkerByPlaceIdAndId(place.getId(), workerId);
    }

    @Override
    public void addWorkerRole(long workerId, String roleName) {
        checkPermission(workerId);
        var place = ctx.getPlace();

        var role = roleRepository.findPlaceRoleByName(roleName);
        var worker = workerRepository.findWorkerByPlaceIdAndId(place.getId(), workerId);
        worker.setPlaceRoleId(role.getId());
        workerRepository.updateWorker(worker);
    }

    private void validatePinCode(String pin) {
        if (pin.length() != 4) {
            throw new IllegalArgumentException("Pin code should have 4 digits");
        }
        var place = ctx.getPlace();

        var placeWorkers = workerRepository.findAllByPlaceId(place.getId());
        var users = userRepository.findUserByIds(placeWorkers
                .stream()
                .map(Worker::getUserId)
                .collect(Collectors.toList())
        );
        for (User user : users) {
            if (encoder.matches(pin, user.getPassword())) {
                throw new IllegalArgumentException("Pin code already exist");
            }
        }
    }

    private void checkPermission(long workerId) {
        var place = ctx.getPlace();
        var worker = workerRepository.findById(workerId);

        if (place.getId() != worker.getPlaceId()) {
            throw new PermissionDeniedException();
        }
    }
}
