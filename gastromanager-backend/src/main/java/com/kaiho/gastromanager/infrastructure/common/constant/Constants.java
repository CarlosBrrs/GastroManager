package com.kaiho.gastromanager.infrastructure.common.constant;

import static com.kaiho.gastromanager.domain.user.model.RoleType.ROLE_CHEF;
import static com.kaiho.gastromanager.domain.user.model.RoleType.ROLE_MANAGER;
import static com.kaiho.gastromanager.domain.user.model.RoleType.ROLE_OWNER;
import static com.kaiho.gastromanager.domain.user.model.RoleType.ROLE_SUPERUSER;

public abstract class Constants {

    public static final String BASE_URL = "/api/v1";
    public static final String INGREDIENTS_CONTROLLER = "/ingredients";
    public static final String USERS_CONTROLLER = "/users";
    public static final String ROLES_CONTROLLER = "/roles";
    public static final String INGREDIENT_UUID_PARAMETER = "/{ingredientUuid}";
    public static final String USER_UUID_PARAMETER = "/{userUuid}";
    public static final String ROLE_UUID_PARAMETER = "/{roleUuid}";
    public static final String OWNER = ROLE_OWNER.getValue();
    public static final String MANAGER = ROLE_MANAGER.getValue();
    public static final String CHEF = ROLE_CHEF.getValue();
    public static final String SUPERUSER = ROLE_SUPERUSER.getValue();

    private Constants() {
        throw new IllegalArgumentException("Non instantiable class");
    }
}