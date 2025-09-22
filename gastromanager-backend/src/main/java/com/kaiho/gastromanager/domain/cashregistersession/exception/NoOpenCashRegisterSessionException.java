package com.kaiho.gastromanager.domain.cashregistersession.exception;

public class NoOpenCashRegisterSessionException extends RuntimeException {

    public NoOpenCashRegisterSessionException(String userId) {
        super("El usuario '" + userId + "' no tiene una sesión de caja abierta");
    }
}
