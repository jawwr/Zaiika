package com.project.zaiika.services.workers;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.userModels.Role;
import com.project.zaiika.models.userModels.User;
import com.project.zaiika.models.userModels.UserRole;
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
import java.util.Random;
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

        var user = saveWorkerAsUser(workerDto);
        var savedWorker = saveWorker(user.getId(), workerDto);

        workerDto.setId(savedWorker.getId());
        workerDto.setPlaceId(savedWorker.getPlaceId());
        workerDto.setRole(roleRepository.findPlaceRoleById(workerDto.getPlaceRoleId()).getName());

        return workerDto;
    }

    private Worker saveWorker(long userId, WorkerDto workerDto) {
        var worker = Worker.builder()
                .id(workerDto.getId())
                .userId(userId)
                .placeRoleId(workerDto.getPlaceRoleId())
                .placeId(workerDto.getPlaceId())
                .build();
        return workerRepository.save(worker);
    }

    private User saveWorkerAsUser(WorkerDto dto) {
        var user = convertWorkerToUser(dto, true);
        return userRepository.save(user);
    }

    private User convertWorkerToUser(WorkerDto dto, boolean generateLogin) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .surname(dto.getSurname())
                .patronymic(dto.getPatronymic())
                .role(new Role(4L, UserRole.WORKER.name()))
                .roleId(4L)
                .password(dto.getPinCode())
                .login(generateLogin ? generateLoginFromWorkerDto(dto) : "")
                .build();
    }

    private String generateLoginFromWorkerDto(WorkerDto dto) {
        Random random = new Random();
        return "w" +
                random.nextInt(1000) +
                dto.getName().toCharArray()[0] +
                dto.getName().toCharArray()[dto.getName().length() - 1] +
                dto.getName().length() +
                dto.getSurname().toCharArray()[0] +
                dto.getSurname().toCharArray()[dto.getSurname().length() - 1] +
                dto.getSurname().length() +
                dto.getPatronymic().toCharArray()[0] +
                dto.getPatronymic().toCharArray()[dto.getPatronymic().length() - 1] +
                dto.getPatronymic().length() +
                dto.getPlaceId();
    }

    @Override
    public void updateWorker(WorkerDto updateWorker) {
        checkPermission(updateWorker.getId());

        validatePinCode(updateWorker.getPinCode());
        updateWorker.setPinCode(encoder.encode(updateWorker.getPinCode()));

        var worker = workerRepository.findById(updateWorker.getId());
        saveWorker(worker.getUserId(), updateWorker);

        var user = userRepository.findUserById(worker.getId());
        var userWorker = convertWorkerToUser(updateWorker, false);
        userWorker.setLogin(user.getLogin());
        userRepository.save(userWorker);
    }

    @Override
    public List<WorkerDto> getAllWorkers() {
        var place = ctx.getPlace();

        var workers = workerRepository.findAllByPlaceId(place.getId());
        List<Long> usersId = workers.stream().map(Worker::getUserId).toList();
        var users = userRepository.findUserByIds(usersId);
        return generateWorkersDto(workers, users);
    }

    private List<WorkerDto> generateWorkersDto(List<Worker> workers, List<User> users) {
        List<WorkerDto> dtoUsers = new ArrayList<>();

        for (Worker worker : workers) {
            var user = users.stream().filter(x -> x.getId() == worker.getUserId()).findFirst().get();
            var dto = generateWorkerDto(user, worker);

            dtoUsers.add(dto);
        }

        return dtoUsers;
    }

    private WorkerDto generateWorkerDto(User user, Worker worker) {
        return WorkerDto.builder()
                .id(worker.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .patronymic(user.getPatronymic())
                .role(worker.getPlaceRoleId() != 0
                        ? roleRepository.findPlaceRoleById(worker.getPlaceRoleId()).getName()
                        : user.getRole().getName())
                .placeRoleId(worker.getPlaceRoleId())
                .build();
    }

    @Override
    public WorkerDto getWorker(long workerId) {
        checkPermission(workerId);

        var worker = workerRepository.findById(workerId);
        var user = userRepository.findUserById(worker.getUserId());

        return generateWorkerDto(user, worker);
    }

    @Override
    public void deleteWorker(long workerId) {
        checkPermission(workerId);

        workerRepository.deleteWorkerById(workerId);
    }

    @Override
    public void addWorkerRole(long workerId, String roleName) {
        checkPermission(workerId);
        var place = ctx.getPlace();

        var role = roleRepository.findPlaceRoleByPlaceIdAndName(place.getId(), roleName);
        var worker = workerRepository.findById(workerId);
        worker.setPlaceRoleId(role.getId());
        workerRepository.save(worker);
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
