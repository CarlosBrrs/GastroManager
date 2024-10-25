package com.kaiho.gastromanager.application.orderitem.dto.request;

import java.util.UUID;

public record OrderItemRequestDto(UUID productItemUuid, int quantity) {
}
