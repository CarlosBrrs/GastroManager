package com.kaiho.gastromanager.application.order.dto.request;

import lombok.Builder;

@Builder
public record ChangeOrderStatusRequestDto(String newStatus, String reason) {
}
