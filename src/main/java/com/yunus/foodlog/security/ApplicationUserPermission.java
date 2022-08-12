package com.yunus.foodlog.security;

public enum ApplicationUserPermission {

    GUEST_READ("guest:read"),
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USER_READ_WRITE("user:read_write"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_READ_WRITE("admin:read_write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
