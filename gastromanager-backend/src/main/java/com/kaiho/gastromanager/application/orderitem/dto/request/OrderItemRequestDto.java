package com.kaiho.gastromanager.application.orderitem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderItemRequestDto(@NotNull UUID productUuid, @Min(1) int quantity, String customerNotes) {
}
