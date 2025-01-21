package com.kaiho.gastromanager.domain.restaurant.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

public class RestaurantDoesNotExistException extends EntityDoesNotExistException {
    public RestaurantDoesNotExistException(String uuid) {
        super("Restaurant with UUID: " + uuid + " does not exist");
    }
}
