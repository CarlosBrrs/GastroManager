package com.kaiho.gastromanager.domain.payment.exception;

public class PaymentAmountExceededException extends RuntimeException {
    public PaymentAmountExceededException(String message) {
        super(message);
    }
}

