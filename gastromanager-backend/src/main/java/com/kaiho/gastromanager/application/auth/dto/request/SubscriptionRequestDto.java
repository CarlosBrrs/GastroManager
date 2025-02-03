package com.kaiho.gastromanager.application.auth.dto.request;

public record SubscriptionRequestDto(String paymentToken, String plan) {// Tipo de plan ('basic', 'premium', 'enterprise', etc.)// Token de pago para procesar la suscripción
}
