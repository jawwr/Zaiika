package com.project.zaiika.services.util;

import com.project.zaiika.models.placeModels.Menu;
import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.placeModels.Site;
import com.project.zaiika.models.roles.PlaceRole;
import com.project.zaiika.models.roles.UserRole;
import com.project.zaiika.models.user.User;
import com.project.zaiika.models.user.UserDetailImpl;
import com.project.zaiika.models.worker.Worker;
import com.project.zaiika.repositories.place.menu.MenuRepository;
import com.project.zaiika.repositories.place.place.PlaceJpaRepository;
import com.project.zaiika.repositories.place.site.SiteRepository;
import com.project.zaiika.repositories.role.PlaceRoleRepository;
import com.project.zaiika.repositories.user.UserRepository;
import com.project.zaiika.repositories.worker.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ContextService {
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final SiteRepository siteRepository;
    private final MenuRepository menuRepository;
    private final PlaceRoleRepository placeRoleRepository;

    public User getContextUser() {
        var login = ((UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin();
        return userRepository.getUserByLogin(login);
    }

    public Worker getContextWorker() {
        var user = getContextUser();
        return workerRepository.findWorkerByUserId(user.getId());
    }

    public PlaceRole getWorkerPlaceRole() {
        var worker = getContextWorker();
        return worker.getPlaceRole();
    }

    public Place getPlace() {
        var user = getContextUser();

        var isOwner = user.getRoles()
                .stream()
                .anyMatch(x -> x.getName().equals(UserRole.PLACE_OWNER.name()));
        if (isOwner) {
            return placeJpaRepository.findPlaceByOwnerId(user.getId());
        }

        var worker = workerRepository.findWorkerByUserId(user.getId());
        return worker.getPlace();
    }

    public List<Site> getSite() {
        var place = getContextWorker().getPlace();
        return siteRepository.findSitesByPlaceId(place.getId());
    }

    public Site getSite(long id) {
        var sites = getSite();
        return sites.stream().filter(x -> x.getId() == id).findAny().orElse(null);
    }

    public List<Menu> getMenu() {
        var sites = getSite();
        List<Long> siteIds = sites.stream().map(Site::getId).toList();
        return menuRepository.findAllBySiteIds(siteIds);
    }

    public Menu getMenu(long id) {
        var menus = getMenu();
        return menus.stream().filter(x -> x.getId() == id).findAny().orElseThrow(IllegalArgumentException::new);
    }

    public List<PlaceRole> getPlaceRole() {
        var place = getContextWorker().getPlace();
        return placeRoleRepository.findAllByPlaceId(place.getId());
    }

    public void checkPlaceRoleExisting(long roleId) {
        var roles = getPlaceRole();
        roles.stream().filter(role -> role.getId() == roleId).findAny().orElseThrow(IllegalArgumentException::new);
    }
}
