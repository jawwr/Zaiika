package com.project.zaiika.services.placeServices;

import com.project.zaiika.exceptions.PermissionDeniedException;
import com.project.zaiika.models.placeModels.Site;
import com.project.zaiika.repositories.place.SiteRepository;
import com.project.zaiika.services.util.ContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {
    private final SiteRepository siteRepository;
    private final ContextService ctx;

    @Override
    public Site createSite(Site site) {
        var role = ctx.getWorkerPlaceRole();
        ctx.checkRolePermission(role::isManageSitePermission);

        var place = ctx.getPlace();
        site.setPlace(place);
        return siteRepository.save(site);
    }

    @Override
    public List<Site> getAllSites() {
        var role = ctx.getWorkerPlaceRole();
        ctx.checkRolePermission(role::isViewSitePermission);

        var place = ctx.getPlace();
        return siteRepository.findAllByPlaceId(place.getId());
    }

    @Override
    public void updateSite(Site site) {
        var role = ctx.getWorkerPlaceRole();
        ctx.checkRolePermission(role::isManageSitePermission);

        checkPermission(site.getId());

        var place = ctx.getPlace();
        site.setPlace(place);

        siteRepository.save(site);
    }

    @Override
    public void deleteSite(long siteId) {
        var role = ctx.getWorkerPlaceRole();
        ctx.checkRolePermission(role::isManageSitePermission);

        checkPermission(siteId);
        siteRepository.deleteSiteById(siteId);
    }

    private void checkPermission(long siteId) {
        var site = ctx.getSite(siteId);
        if (site == null) {
            throw new PermissionDeniedException();
        }
    }
}
