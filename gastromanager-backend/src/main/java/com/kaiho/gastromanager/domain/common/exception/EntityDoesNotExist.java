package com.kaiho.gastromanager.domain.common.exception;

import lombok.Getter;

@Getter
public class EntityDoesNotExist extends RuntimeException {

    public EntityDoesNotExist(String message) {
        super(message);
    }
}
