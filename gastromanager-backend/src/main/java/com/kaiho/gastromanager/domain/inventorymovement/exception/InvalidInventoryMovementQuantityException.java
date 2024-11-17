package com.kaiho.gastromanager.domain.inventorymovement.exception;

public class InvalidInventoryMovementQuantityException extends RuntimeException {

    public InvalidInventoryMovementQuantityException() {
        super("Inventory movement cannot be zero");
    }
}
