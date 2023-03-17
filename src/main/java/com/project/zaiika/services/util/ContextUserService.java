package com.project.zaiika.services.util;

import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.userModels.User;
import com.project.zaiika.models.userModels.UserDetailImpl;
import com.project.zaiika.models.userModels.UserRole;
import com.project.zaiika.repositories.placesRepository.PlaceRepository;
import com.project.zaiika.repositories.userRepositories.UserRepository;
import com.project.zaiika.repositories.worker.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ContextUserService {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final WorkerRepository workerRepository;

    public User getContextUser() {
        var login = ((UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin();
        return userRepository.findUserByLogin(login);
    }

    public Place getPlace(){
        var user = getContextUser();

        var role = UserRole.values()[(int) user.getRoleId()];
        if (role.equals(UserRole.ADMIN) || role.equals(UserRole.PLACE_OWNER)){
            return placeRepository.findPlaceByOwnerId(user.getId());
        }

        var worker = workerRepository.findWorkerByUserId(user.getId());
        return placeRepository.findPlaceById(worker.getPlaceId());
    }
}
