package com.kaiho.gastromanager.domain.cashregister.exception;

import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

public class CashRegisterNotFoundException extends EntityNotFoundException {

    public CashRegisterNotFoundException(UUID cashRegisterUuid) {
        super("Caja registradora no encontrada: " + cashRegisterUuid);
    }
}
