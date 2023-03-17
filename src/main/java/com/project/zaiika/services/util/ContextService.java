package com.project.zaiika.services.util;

import com.project.zaiika.models.placeModels.Menu;
import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.placeModels.Site;
import com.project.zaiika.models.userModels.User;
import com.project.zaiika.models.userModels.UserDetailImpl;
import com.project.zaiika.models.userModels.UserRole;
import com.project.zaiika.repositories.placesRepository.MenuRepository;
import com.project.zaiika.repositories.placesRepository.PlaceRepository;
import com.project.zaiika.repositories.placesRepository.SiteRepository;
import com.project.zaiika.repositories.userRepositories.UserRepository;
import com.project.zaiika.repositories.worker.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ContextService {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final SiteRepository siteRepository;
    private final MenuRepository menuRepository;
    private final WorkerRepository workerRepository;

    public User getContextUser() {
        var login = ((UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin();
        return userRepository.findUserByLogin(login);
    }

    public Place getPlace() {
        var user = getContextUser();

        var role = UserRole.values()[(int) user.getRoleId()];
        if (role.equals(UserRole.ADMIN) || role.equals(UserRole.PLACE_OWNER)) {
            return placeRepository.findPlaceByOwnerId(user.getId());
        }

        var worker = workerRepository.findWorkerByUserId(user.getId());
        return placeRepository.findPlaceById(worker.getPlaceId());
    }

    public List<Site> getSite() {
        var place = getPlace();
        return siteRepository.findAllByPlaceId(place.getId());
    }

    public Site getSite(long id) {
        var sites = getSite();
        for (Site site : sites) {
            if (site.getId() == id) {
                return site;
            }
        }
        return null;
    }

    public List<Menu> getMenu() {
        var sites = getSite();
        List<Menu> menus = new ArrayList<>();
        for (Site site : sites) {
            menus.addAll(menuRepository.findAllBySiteId(site.getId()));
        }
        return menus;
    }

    public Menu getMenu(long id) {
        var menus = getMenu();
        for (Menu menu : menus) {
            if (menu.getId() == id) {
                return menu;
            }
        }
        return null;
    }
}
