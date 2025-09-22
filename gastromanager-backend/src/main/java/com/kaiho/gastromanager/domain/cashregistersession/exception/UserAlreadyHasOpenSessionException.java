package com.kaiho.gastromanager.domain.cashregistersession.exception;

public class UserAlreadyHasOpenSessionException extends RuntimeException {

    public UserAlreadyHasOpenSessionException(String userId) {
        super("El usuario '" + userId + "' ya tiene una sesión de caja abierta");
    }
}
