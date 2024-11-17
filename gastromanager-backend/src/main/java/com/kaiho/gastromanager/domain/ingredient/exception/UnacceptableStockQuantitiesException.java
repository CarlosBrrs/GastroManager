package com.kaiho.gastromanager.domain.ingredient.exception;

public class UnacceptableStockQuantitiesException extends RuntimeException {

    public UnacceptableStockQuantitiesException() {
        super("Available initial stock has to be greater than minimum stock");
    }
}
