package com.kaiho.gastromanager.domain.submenu.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityAlreadyExistsException;

public class SubmenuAlreadyExistsException extends EntityAlreadyExistsException {
    public SubmenuAlreadyExistsException(String name) {
        super("Submenu with name " + name + " already exists in the system");
    }
}
