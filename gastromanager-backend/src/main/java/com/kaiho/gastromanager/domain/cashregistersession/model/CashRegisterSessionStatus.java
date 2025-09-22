package com.kaiho.gastromanager.domain.cashregistersession.model;

public enum CashRegisterSessionStatus {
    OPEN("Abierta"),
    CLOSED("Cerrada");

    private final String description;

    CashRegisterSessionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
