package com.kaiho.gastromanager.domain.payment.model;

public enum PaymentMethod {
    CASH("Efectivo"),
    CREDIT_CARD("Tarjeta de crédito"),
    DEBIT_CARD("Tarjeta de débito"),
    TRANSFER("Transferencia bancaria"),
    NEQUI("Nequi"),
    CREDIT("Crédito"),
    DAVIPLATA("Daviplata");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
