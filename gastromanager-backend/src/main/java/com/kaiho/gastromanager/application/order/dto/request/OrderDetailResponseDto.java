package com.kaiho.gastromanager.application.order.dto.request;

import com.kaiho.gastromanager.application.invoice.dto.response.InvoiceResponseDto;
import com.kaiho.gastromanager.application.orderitem.dto.response.OrderItemResponseDto;
import com.kaiho.gastromanager.domain.order.model.InvoicingStatus;
import com.kaiho.gastromanager.domain.order.model.OperationalStatus;
import com.kaiho.gastromanager.domain.order.model.PaymentStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderDetailResponseDto(
        UUID uuid,
        String code,
        String createdBy,
        String customerName,
        String customerNotes,
        String tableNumber,
        BigDecimal totalAmount,
        BigDecimal totalPaid,
        BigDecimal remainingToPay,
        OperationalStatus operationalStatus,
        PaymentStatus paymentStatus,
        InvoicingStatus invoicingStatus,
        List<OrderItemResponseDto> orderItems,
        Instant updatedDate,
        List<InvoiceResponseDto> invoices,
        boolean requiresPaymentBefore
) {

}
