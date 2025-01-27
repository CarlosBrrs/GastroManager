package com.kaiho.gastromanager.domain.user.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

public class RoleDoesNotExistException extends EntityDoesNotExistException {
    public RoleDoesNotExistException(String roles) {
        super("Roles with uuids/names: " + roles + " don't exist.");
    }
}
