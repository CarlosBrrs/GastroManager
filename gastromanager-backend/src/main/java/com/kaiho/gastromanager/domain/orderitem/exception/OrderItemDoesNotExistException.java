package com.kaiho.gastromanager.domain.orderitem.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

public class OrderItemDoesNotExistException extends EntityDoesNotExistException {
    public OrderItemDoesNotExistException(int order, int required) {
        super("Difference between order item and required: Required " + required + " but found " + order);
    }
}
