package com.kaiho.gastromanager.domain.payment.exception;

public class PaymentTransactionVerificationException extends RuntimeException {
    public PaymentTransactionVerificationException(String transactionId) {
        super("La verificación del transactionId '" + transactionId + "' ha fallado para métodos de pago virtuales.");
    }
}
