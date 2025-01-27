package com.kaiho.gastromanager.domain.order.exception;

import java.util.UUID;

public class UnauthorizedOrderStatusChangeException extends RuntimeException {
    public UnauthorizedOrderStatusChangeException(UUID userUuid, String newStatus) {
        super("The user " + userUuid + " has no permission to change the status to " + newStatus);
    }
}
