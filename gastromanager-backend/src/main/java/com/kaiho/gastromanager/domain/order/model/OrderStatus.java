package com.kaiho.gastromanager.domain.order.model;

public enum OrderStatus {
    AWAITING_PAYMENT,
    PENDING,
    PREPARING,
    READY,
    SERVED,
    COMPLETED,
    CANCELLED;

}
