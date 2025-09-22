package com.kaiho.gastromanager.application.invoice.dto.request;

import java.util.UUID;

public record InvoiceItemRequestDto(UUID orderItemUuid, int quantity) {
}
