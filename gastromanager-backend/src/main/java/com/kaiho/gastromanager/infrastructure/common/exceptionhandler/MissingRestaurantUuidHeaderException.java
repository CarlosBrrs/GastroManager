package com.kaiho.gastromanager.infrastructure.common.exceptionhandler;

public class MissingRestaurantUuidHeaderException extends RuntimeException {
    public MissingRestaurantUuidHeaderException() {
        super("Missing required header: X-Restaurant-Uuid");
    }
}
