package com.kaiho.gastromanager.domain.product.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityAlreadyExistsException;

public class ProductAlreadyExistsException extends EntityAlreadyExistsException {
    public ProductAlreadyExistsException(String name) {
        super("Product with name '" + name + "' already exists.");
    }
}
