package com.kaiho.gastromanager.domain.cashregistersession.model;

import lombok.Getter;

@Getter
public enum CashRegisterSessionBalanceStatus {

    BALANCED("Balanced"),
    SHORT("Short"),
    OVER("Over");

    private final String description;

    CashRegisterSessionBalanceStatus(String description) {
        this.description = description;
    }

}
