package com.kaiho.gastromanager.domain.restaurant.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityAlreadyExistsException;

public class RestaurantAlreadyExistsException extends EntityAlreadyExistsException {
    public RestaurantAlreadyExistsException(String name, String address, String ownerUuid ) {
        super("Restaurant with name - owner - address " + name + " - " + ownerUuid + " - " + address + " already exists in the system");
    }
}
