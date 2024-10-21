package com.kaiho.gastromanager.domain.common.exception;

import lombok.Getter;

@Getter
public class EntityDoesNotExistException extends RuntimeException {

    public EntityDoesNotExistException(String message) {
        super(message);
    }
}
