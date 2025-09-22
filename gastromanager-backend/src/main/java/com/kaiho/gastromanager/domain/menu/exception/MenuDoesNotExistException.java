package com.kaiho.gastromanager.domain.menu.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

public class MenuDoesNotExistException extends EntityDoesNotExistException {
    public MenuDoesNotExistException(String uuid) {
        super("Menu with UUID: " + uuid + " does not exist");
    }
}
