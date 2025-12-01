package com.kaiho.gastromanager.domain.order.model;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    UNPAID("Sin pagar"),
    PARTIALLY_PAID("Parcialmente pagado"),
    FULLY_PAID("Completamente pagado"),
    REFUNDED("Reembolsado");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

}
