package com.kaiho.gastromanager.domain.user.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

public class RoleDoesNotExistExceptionException extends EntityDoesNotExistException {
    public RoleDoesNotExistExceptionException(String roles) {
        super("Roles with uuids/names: " + roles + " don't exist.");
    }
}
