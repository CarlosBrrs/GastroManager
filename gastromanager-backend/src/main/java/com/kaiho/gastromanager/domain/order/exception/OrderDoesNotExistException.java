package com.kaiho.gastromanager.domain.order.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

import java.util.UUID;

public class OrderDoesNotExistException extends EntityDoesNotExistException {
    public OrderDoesNotExistException(UUID orderUuid) {
        super("The order with UUID: " + orderUuid + " does not exist in the system");
    }
}
