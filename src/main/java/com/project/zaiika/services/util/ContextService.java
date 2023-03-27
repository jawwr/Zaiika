package com.project.zaiika.services.util;

import com.project.zaiika.models.placeModels.Menu;
import com.project.zaiika.models.placeModels.Place;
import com.project.zaiika.models.placeModels.Site;
import com.project.zaiika.models.userModels.User;
import com.project.zaiika.models.userModels.UserDetailImpl;
import com.project.zaiika.models.roles.UserRole;
import com.project.zaiika.models.worker.Worker;
import com.project.zaiika.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ContextService {
    private final UserRepository userRepository;

    public User getContextUser() {
        var login = ((UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin();
        return userRepository.findUserByLogin(login);
    }

    public Worker getContextWorker() {
        var user = getContextUser();
        return user.getWorker();
    }

    public Place getPlace() {
        var user = getContextUser();

        var role = UserRole.valueOf(user.getRole().getName());
        if (role.equals(UserRole.PLACE_OWNER)) {
            return user.getPlace();
        }

        var worker = user.getWorker();
        return worker.getPlace();
    }

    public List<Site> getSite() {
        var place = getPlace();
        return place.getSites();
    }

    public Site getSite(long id) {
        var sites = getSite();
//        for (Site site : sites) {
//            if (site.getId() == id) {
//                return site;
//            }
//        }
        return sites.stream().filter(x -> x.getId() == id).findAny().orElse(null);
    }

    public List<Menu> getMenu() {
        var sites = getSite();
        List<Menu> menus = new ArrayList<>();
        for (Site site : sites) {
            menus.addAll(site.getMenus());
        }
        return menus;
    }

    public Menu getMenu(long id) {
        var menus = getMenu();
//        for (Menu menu : menus) {
//            if (menu.getId() == id) {
//                return menu;
//            }
//        }
        return menus.stream().filter(x -> x.getId() == id).findAny().orElse(null);
    }
}
