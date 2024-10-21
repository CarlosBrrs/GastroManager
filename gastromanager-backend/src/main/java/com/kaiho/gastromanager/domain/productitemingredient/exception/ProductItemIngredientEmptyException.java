package com.kaiho.gastromanager.domain.productitemingredient.exception;

public class ProductItemIngredientEmptyException extends RuntimeException {
    public ProductItemIngredientEmptyException() {
        super("Product must have at least one ingredient");
    }
}
