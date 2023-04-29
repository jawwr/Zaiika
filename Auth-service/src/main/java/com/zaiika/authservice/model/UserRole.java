package com.zaiika.authservice.model;

public enum UserRole {
    DUNGEON_MASTER(AvailablePermission.values()),
    PLACE_OWNER(new AvailablePermission[]//TODO изменить на релизе
            {
                    AvailablePermission.VIEW_DELIVERY,
                    AvailablePermission.VIEW_ORDER,
                    AvailablePermission.VIEW_MENU,
                    AvailablePermission.VIEW_PRODUCT,
                    AvailablePermission.VIEW_SITE,
                    AvailablePermission.MANAGE_PLACE_ROLE,
                    AvailablePermission.MANAGE_ROLE,
                    AvailablePermission.MANAGE_USER,
                    AvailablePermission.MANAGE_WORKER,
                    AvailablePermission.MANAGE_ORDER,
                    AvailablePermission.MANAGE_MENU,
                    AvailablePermission.MANAGE_PRODUCT,
                    AvailablePermission.MANAGE_SITE,
                    AvailablePermission.MANAGE_DELIVERY,
                    AvailablePermission.VIEW_PERMISSIONS,
            }
    ),
    ADMIN(new AvailablePermission[0]),
    WORKER(new AvailablePermission[0]),
    USER(new AvailablePermission[0]),
    ;

    public final AvailablePermission[] permissions;

    private UserRole(AvailablePermission[] permissions) {
        this.permissions = permissions;
    }
}
