package com.kaiho.gastromanager.domain.cashregistersession.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

import java.util.UUID;

public class CashRegisterSessionNotFoundException extends EntityDoesNotExistException {

    public CashRegisterSessionNotFoundException(UUID sessionUuid) {
        super("Sesión de caja registradora no encontrada con UUID: " + sessionUuid);
    }
}
