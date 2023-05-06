package com.zaiika.workerservice.service;

import com.zaiika.workerservice.model.Worker;
import com.zaiika.workerservice.model.WorkerDto;
import com.zaiika.workerservice.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {
    private final WorkerRepository workerRepository;
    //    private final UserRepository userJpaRepository;
//    private final PlaceRoleRepository placeRoleRepository;
//    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
//    private final ContextService ctx;

    @Override
    public WorkerDto createWorker(WorkerDto workerDto) {
//        var place = ctx.getPlace();
//        workerDto.setPlaceId(place.getId());

        validatePinCode(workerDto.getPinCode());
        workerDto.setPinCode(encoder.encode(workerDto.getPinCode()));

//        var user = saveWorkerAsUser(workerDto);
//        var savedWorker = saveWorker(user, workerDto);

//        workerDto.setId(savedWorker.getId());
//        workerDto.setPlaceId(savedWorker.getPlace().getId());
//        workerDto.setRole(placeRoleRepository.findPlaceRoleById(workerDto.getPlaceRoleId()).getName());

        return workerDto;
    }

//    private Worker saveWorker(User user, WorkerDto workerDto) {
//        var placeRole = placeRoleRepository.findPlaceRoleById(workerDto.getPlaceRoleId());
//        var place = ctx.getPlace();
//        var worker = Worker.builder()
//                .id(workerDto.getId())
//                .user(user)
//                .placeRole(placeRole)
//                .place(place)
//                .build();
//        return workerRepository.save(worker);
//    }

//    private User saveWorkerAsUser(WorkerDto dto) {
//        var user = convertWorkerDtoToUser(dto, true);
//        return userJpaRepository.save(user);
//    }

//    private User convertWorkerDtoToUser(WorkerDto dto, boolean generateLogin) {
//        return User.builder()
//                .id(dto.getId())
//                .name(dto.getName())
//                .surname(dto.getSurname())
//                .patronymic(dto.getPatronymic())
//                .roles(List.of(roleRepository.findRoleByName(UserRole.WORKER.name())))
//                .password(dto.getPinCode())
//                .login(generateLogin ? generateLoginFromWorkerDto(dto) : "")
//                .build();
//    }

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

//        var worker = workerRepository.findById(updateWorker.getId());
//        saveWorker(worker.getUser(), updateWorker);
//
//        var user = userJpaRepository.getUserById(worker.getId());
//        var userWorker = convertWorkerDtoToUser(updateWorker, false);
//        userWorker.setLogin(user.getLogin());
//        userJpaRepository.save(userWorker);
    }

    @Override
    public List<WorkerDto> getAllWorkers() {
//        var place = ctx.getPlace();
//        var workers = workerRepository.findAllByPlaceId(place.getId()); // place.getWorkers();

//        return generateWorkersDto(workers);
        return null;
    }

    private List<WorkerDto> generateWorkersDto(List<Worker> workers) {
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
    public WorkerDto getWorker(long workerId) {
        checkPermission(workerId);
        var worker = workerRepository.findById(workerId);

//        return generateWorkerDto(worker);
        return null;
    }

    @Override
    public void deleteWorker(long workerId) {
        checkPermission(workerId);

        workerRepository.deleteWorkerById(workerId);
    }

    @Override
    public void addWorkerRole(long workerId, String roleName) {
        checkPermission(workerId);
//        if (UserRole.ADMIN.name().equals(roleName.toUpperCase())) {
//            addAdminRole(workerId);
//        } else {
//            addPlaceRole(workerId, roleName);
//        }
    }

    @Override
    public boolean isWorker() {
        return false;
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
//        if (pin.length() != 4) {
//            throw new IllegalArgumentException("Pin code should have 4 digits");
//        }
//        var place = ctx.getPlace();
//
//        var placeWorkers = workerRepository.findAllByPlaceId(place.getId());
//        var users = placeWorkers.stream().map(Worker::getUser).toList();
//        for (User user : users) {
//            if (encoder.matches(pin, user.getPassword())) {
//                throw new IllegalArgumentException("Pin code already exist");
//            }
//        }
    }

    private void checkPermission(long workerId) {
//        var place = ctx.getPlace();
//        var worker = workerRepository.findById(workerId);
//
//        if (place.getId() != worker.getPlace().getId()) {
//            throw new PermissionDeniedException();
//        }
    }
}
