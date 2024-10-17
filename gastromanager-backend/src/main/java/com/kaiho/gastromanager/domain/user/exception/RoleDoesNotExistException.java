package com.kaiho.gastromanager.domain.user.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExist;

public class RoleDoesNotExistException extends EntityDoesNotExist {
    public RoleDoesNotExistException(String roles) {
        super("Roles with uuids/names: " + roles + " don't exist.");
    }
}
