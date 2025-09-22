package com.kaiho.gastromanager.domain.cashregistersession.exception;

import java.util.UUID;

public class CashRegisterSessionAlreadyClosedException extends RuntimeException {

    public CashRegisterSessionAlreadyClosedException(UUID sessionUuid) {
        super("La sesión de caja registradora ya está cerrada o no está abierta: " + sessionUuid);
    }
}
