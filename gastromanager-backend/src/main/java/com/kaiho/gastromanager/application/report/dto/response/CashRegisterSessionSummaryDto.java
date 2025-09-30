package com.kaiho.gastromanager.application.report.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record CashRegisterSessionSummaryDto(
        UUID sessionUuid,
        UUID operatorUserUuid,
        String operatorUserName,
        Instant sessionOpenTime,
        Instant sessionCloseTime,
        String sessionStatus,
        BigDecimal sessionSales,
        int sessionOrderCount,
        int sessionTransactionCount
) {
}
