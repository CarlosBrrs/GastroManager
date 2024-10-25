package com.kaiho.gastromanager.domain.orderitem.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderItem(UUID productItemUuid, int quantity, double unitPrice) {
}
