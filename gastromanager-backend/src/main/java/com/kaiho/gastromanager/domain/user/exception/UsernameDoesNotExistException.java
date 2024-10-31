package com.kaiho.gastromanager.domain.user.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

import java.util.UUID;

public class UsernameDoesNotExistException extends EntityDoesNotExistException {
    public UsernameDoesNotExistException(UUID uuid) {
        super("Username with UUID: " + uuid + " does not exist.");
    }
}
