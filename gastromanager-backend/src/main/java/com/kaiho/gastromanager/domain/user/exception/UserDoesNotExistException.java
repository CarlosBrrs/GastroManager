package com.kaiho.gastromanager.domain.user.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

import java.util.UUID;

public class UserDoesNotExistException extends EntityDoesNotExistException {
    public UserDoesNotExistException(UUID userUuid) {
        super("User with UUID: " + userUuid + " does not exist in the system");
    }
}
