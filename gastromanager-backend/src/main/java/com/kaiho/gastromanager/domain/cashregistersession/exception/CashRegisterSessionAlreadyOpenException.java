package com.kaiho.gastromanager.domain.cashregistersession.exception;

import java.util.UUID;

public class CashRegisterSessionAlreadyOpenException extends RuntimeException {

    public CashRegisterSessionAlreadyOpenException(String message) {
        super(message);
    }

    public CashRegisterSessionAlreadyOpenException(UUID cashRegisterUuid) {
        super("Ya existe una sesión abierta para la caja registradora: " + cashRegisterUuid);
    }
}
