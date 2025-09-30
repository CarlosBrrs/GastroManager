package com.kaiho.gastromanager.application.report.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record CashRegisterSummaryDto(
        UUID cashRegisterUuid,
        String cashRegisterName,
        BigDecimal totalSales,
        int orderCount,
        int transactionCount,
        List<CashRegisterSessionSummaryDto> sessionSummaries
) {
}
