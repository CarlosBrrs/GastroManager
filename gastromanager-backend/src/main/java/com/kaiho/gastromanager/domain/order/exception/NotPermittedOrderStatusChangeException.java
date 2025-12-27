package com.kaiho.gastromanager.domain.order.exception;

import com.kaiho.gastromanager.domain.order.model.OperationalStatus;

import java.util.UUID;

public class NotPermittedOrderStatusChangeException extends RuntimeException {
    public NotPermittedOrderStatusChangeException(UUID orderUuid, OperationalStatus current, OperationalStatus attempted) {
        super(String.format("Cambio de estado no permitido para orden %s: %s -> %s", orderUuid, current, attempted));
    }
}

