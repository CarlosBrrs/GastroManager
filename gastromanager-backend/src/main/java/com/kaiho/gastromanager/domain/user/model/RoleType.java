package com.kaiho.gastromanager.domain.user.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum RoleType implements GrantedAuthority {
    ROLE_SUPERUSER("SUPERUSER"),
    ROLE_OWNER("OWNER"),
    ROLE_MANAGER("MANAGER"),
    ROLE_WAITER("WAITER"),
    ROLE_CHEF("CHEF"),
    ROLE_KITCHEN_STAFF("KITCHEN_STAFF"),
    ROLE_CASHIER("CASHIER");

    private final String value;

    RoleType(String value) {
        this.value = value;
    }

    public static RoleType fromName(String name) {
        for (RoleType role : RoleType.values()) {
            if (role.value.equalsIgnoreCase(name)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role found for name: " + name);
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
