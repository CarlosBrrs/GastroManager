package com.kaiho.gastromanager.domain.order.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String ingredientName, int requiredQuantity, int availableQuantity) {
        super("Insufficient stock for ingredient: " + ingredientName + ". You are requiring " + requiredQuantity + " but there is only " + availableQuantity + " in stock");
    }
}
