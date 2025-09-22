package com.kaiho.gastromanager.application.order.dto.response;

import com.kaiho.gastromanager.application.orderitem.dto.response.OrderItemResponseDto;
import com.kaiho.gastromanager.domain.order.model.OperationalStatus;
import com.kaiho.gastromanager.domain.order.model.PaymentStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderSummaryResponseDto(
        UUID uuid,
        String code,
        BigDecimal totalAmount,
        BigDecimal totalPaid,
        BigDecimal remainingToPay,
        PaymentStatus paymentStatus,
        OperationalStatus operationalStatus,
        List<OrderItemResponseDto> orderItems
) {
}
