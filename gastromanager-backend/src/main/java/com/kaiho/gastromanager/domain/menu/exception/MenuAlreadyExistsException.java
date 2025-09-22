package com.kaiho.gastromanager.domain.menu.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityAlreadyExistsException;

public class MenuAlreadyExistsException extends EntityAlreadyExistsException {
    public MenuAlreadyExistsException(String name) {
        super("Ingredient with name " + name + " already exists in the system");
    }
}