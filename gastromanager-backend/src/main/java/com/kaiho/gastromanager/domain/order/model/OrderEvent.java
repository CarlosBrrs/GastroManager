package com.kaiho.gastromanager.domain.order.model;

public enum OrderEvent {
    PAYMENT_COMPLETED,  // Cuando se completa el pago
    START_PREPARATION,  // Cuando la cocina comienza a preparar
    ORDER_READY,        // Cuando la orden está lista
    ORDER_SERVED,       // Cuando el cliente recibe la orden
    COMPLETE_ORDER,     // Cuando el cliente paga
    CANCEL_ORDER        // Cuando la orden es cancelada
}
