package com.kaiho.gastromanager.application.order.dto.response;

import com.kaiho.gastromanager.application.invoice.dto.response.InvoiceResponseDto;
import com.kaiho.gastromanager.application.orderitem.dto.response.OrderItemResponseDto;
import com.kaiho.gastromanager.domain.order.model.InvoicingStatus;
import com.kaiho.gastromanager.domain.order.model.OperationalStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderResponseDto(UUID uuid, String code, BigDecimal totalAmount,
                               OperationalStatus operationalStatus, InvoicingStatus invoicingStatus,
                               List<OrderItemResponseDto> orderItems, Instant updatedDate,
                               List<InvoiceResponseDto> invoices) {
}
