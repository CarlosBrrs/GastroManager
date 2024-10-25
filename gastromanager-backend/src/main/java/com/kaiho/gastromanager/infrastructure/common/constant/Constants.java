package com.kaiho.gastromanager.infrastructure.common.constant;

import static com.kaiho.gastromanager.domain.user.model.RoleType.ROLE_CASHIER;
import static com.kaiho.gastromanager.domain.user.model.RoleType.ROLE_CHEF;
import static com.kaiho.gastromanager.domain.user.model.RoleType.ROLE_KITCHEN_STAFF;
import static com.kaiho.gastromanager.domain.user.model.RoleType.ROLE_MANAGER;
import static com.kaiho.gastromanager.domain.user.model.RoleType.ROLE_OWNER;
import static com.kaiho.gastromanager.domain.user.model.RoleType.ROLE_SUPERUSER;
import static com.kaiho.gastromanager.domain.user.model.RoleType.ROLE_WAITER;

public abstract class Constants {

    public static final String BASE_URL = "/api/v1";
    public static final String INGREDIENTS_CONTROLLER = "/ingredients";
    public static final String USERS_CONTROLLER = "/users";
    public static final String ROLES_CONTROLLER = "/roles";
    public static final String PRODUCT_ITEMS_CONTROLLER = "/product-items";
    public static final String ORDERS_CONTROLLER = "/orders";
    public static final String INGREDIENT_UUID_PARAMETER = "/{ingredientUuid}";
    public static final String USER_UUID_PARAMETER = "/{userUuid}";
    public static final String ROLE_UUID_PARAMETER = "/{roleUuid}";
    public static final String PRODUCT_ITEM_UUID_PARAMETER = "/{productItemUuid}";
    public static final String ORDER_UUID_PARAMETER = "/{orderUuid}";
    public static final String OWNER = ROLE_OWNER.getValue();
    public static final String WAITER = ROLE_WAITER.getValue();
    public static final String KITCHEN_STAFF = ROLE_KITCHEN_STAFF.getValue();
    public static final String CASHIER = ROLE_CASHIER.getValue();
    public static final String MANAGER = ROLE_MANAGER.getValue();
    public static final String CHEF = ROLE_CHEF.getValue();
    public static final String SUPERUSER = ROLE_SUPERUSER.getValue();

    private Constants() {
        throw new IllegalArgumentException("Non instantiable class");
    }
}
