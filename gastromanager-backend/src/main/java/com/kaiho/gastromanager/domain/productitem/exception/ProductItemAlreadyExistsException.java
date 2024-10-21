package com.kaiho.gastromanager.domain.productitem.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityAlreadyExistsException;

public class ProductItemAlreadyExistsException extends EntityAlreadyExistsException {
    public ProductItemAlreadyExistsException(String name) {
        super("Product with UUID: " + name + " already exists in the database.");
    }
}
