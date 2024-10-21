package com.kaiho.gastromanager.domain.productitem.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

import java.util.UUID;

public class ProductItemDoesNotExistException extends EntityDoesNotExistException {
    public ProductItemDoesNotExistException(UUID uuid) {
        super("Product item with UUID: " + uuid + " does not exist in the system");
    }
}
