package com.project.zaiika.services.workers;

import com.project.zaiika.models.userModels.User;
import com.project.zaiika.models.worker.Worker;
import com.project.zaiika.models.worker.WorkerDto;
import com.project.zaiika.repositories.userRepositories.PlaceRoleRepository;
import com.project.zaiika.repositories.userRepositories.UserRepository;
import com.project.zaiika.repositories.worker.WorkerRepository;
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

    @Override
    public void createWorker(WorkerDto workerDto) {
        validatePinCode(workerDto.getPinCode(), workerDto.getPlaceId());
        workerDto.setPinCode(encoder.encode(workerDto.getPinCode()));
        var user = new User(workerDto);
        var userId = userRepository.save(user).getId();


        var worker = Worker.builder()
                .userId(userId)
                .placeRoleId(workerDto.getPlaceRoleId())
                .placeId(workerDto.getPlaceId())
                .build();
        workerRepository.save(worker);
    }

    @Override
    public void updateWorker(WorkerDto newWorker) {
        validatePinCode(newWorker.getPinCode(), newWorker.getPlaceId());
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
    public List<WorkerDto> getAllWorkers(long placeId) {
        var workers = workerRepository.findAllByPlaceId(placeId);
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
                    .role(roleRepository.findPlaceRoleById(worker.getPlaceRoleId()).getName())
                    .placeRoleId(worker.getPlaceRoleId())
                    .build();

            dtoUsers.add(dto);
        }
        return dtoUsers;
    }

    @Override
    public WorkerDto getWorker(long placeId, long workerId) {
        var worker = workerRepository.findWorkerByPlaceIdAndId(placeId, workerId);
        var user = userRepository.findUserById(worker.getUserId());
        var role = roleRepository.findPlaceRoleById(worker.getPlaceRoleId());

        return WorkerDto.builder()
                .id(worker.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .patronymic(user.getPatronymic())
                .placeId(placeId)
                .role(role.getName())
                .build();
    }

    @Override
    public void deleteWorker(long placeId, long workerId) {
        workerRepository.deleteWorkerByPlaceIdAndId(placeId, workerId);
    }

    private void validatePinCode(String pin, long placeId) {
        if (pin.length() != 4) {
            throw new IllegalArgumentException("Pin code should have 4 digits");
        }

        var placeWorkers = workerRepository.findAllByPlaceId(placeId);
        var users = userRepository.findUserByIds(placeWorkers
                .stream()
                .map(Worker::getUserId)
                .collect(Collectors.toList())
        );
        for (User user : users) {
            if (encoder.matches(pin, user.getPassword())) {
                throw new IllegalArgumentException("This pin code already exist");
            }
        }
    }
}
