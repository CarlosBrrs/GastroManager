package com.kaiho.gastromanager.domain.cashregistersession.exception;

import java.util.UUID;

public class NoOpenSessionFoundException extends RuntimeException {

    public NoOpenSessionFoundException(UUID userUuid) {
        super("No hay una sesión abierta para el usuario: " + userUuid);
    }
}
