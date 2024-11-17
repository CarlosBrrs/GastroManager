package com.kaiho.gastromanager.domain.ingredient.exception;

public class AvailableStockNotUpdatedException extends RuntimeException {

    public AvailableStockNotUpdatedException() {
        super("New stock quantity is not updated");
    }
}
