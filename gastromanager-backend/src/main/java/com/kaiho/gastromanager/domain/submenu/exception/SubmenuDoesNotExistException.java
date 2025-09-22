package com.kaiho.gastromanager.domain.submenu.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

import java.util.UUID;

public class SubmenuDoesNotExistException extends EntityDoesNotExistException {
    public SubmenuDoesNotExistException(UUID uuid) {
        super("Submenu with UUID " + uuid + " does not exist in the system");
    }
}
