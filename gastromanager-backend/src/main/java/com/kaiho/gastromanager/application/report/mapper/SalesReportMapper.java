package com.kaiho.gastromanager.application.report.mapper;

import com.kaiho.gastromanager.application.report.dto.response.CashRegisterSessionSummaryDto;
import com.kaiho.gastromanager.application.report.dto.response.CashRegisterSummaryDto;
import com.kaiho.gastromanager.application.report.dto.response.PaymentMethodSummaryDto;
import com.kaiho.gastromanager.application.report.dto.response.SalesReportDetailDto;
import com.kaiho.gastromanager.application.report.dto.response.SalesReportResponseDto;
import com.kaiho.gastromanager.application.report.dto.response.SalesReportSummaryDto;
import com.kaiho.gastromanager.domain.report.model.CashRegisterSummary;
import com.kaiho.gastromanager.domain.report.model.CashRegisterSessionSummary;
import com.kaiho.gastromanager.domain.report.model.PaymentMethodSummary;
import com.kaiho.gastromanager.domain.report.model.SalesReport;
import com.kaiho.gastromanager.domain.report.model.SalesReportDetail;
import com.kaiho.gastromanager.domain.report.model.SalesReportSummary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SalesReportMapper {

    public SalesReportResponseDto toResponseDto(SalesReport salesReport) {
        if (salesReport == null) {
            return null;
        }

        return SalesReportResponseDto.builder()
                .totalSales(salesReport.getTotalSales())
                .totalOrders(salesReport.getTotalOrders())
                .reportPeriodStart(salesReport.getReportPeriodStart())
                .reportPeriodEnd(salesReport.getReportPeriodEnd())
                .summary(toSummaryDto(salesReport.getSummary()))
                .details(toDetailDtoList(salesReport.getDetails()))
                .build();
    }

    private SalesReportSummaryDto toSummaryDto(SalesReportSummary summary) {
        if (summary == null) {
            return null;
        }

        return SalesReportSummaryDto.builder()
                .totalRevenue(summary.getTotalRevenue())
                .totalTips(summary.getTotalTips())
                .completedOrders(summary.getCompletedOrders())
                .cancelledOrders(summary.getCancelledOrders())
                .averageOrderValue(summary.getAverageOrderValue())
                .paymentMethodBreakdown(toPaymentMethodSummaryDtoList(summary.getPaymentMethodBreakdown()))
                .cashRegisterBreakdown(toCashRegisterSummaryDtoList(summary.getCashRegisterBreakdown()))
                .build();
    }

    private List<SalesReportDetailDto> toDetailDtoList(List<SalesReportDetail> details) {
        if (details == null) {
            return null;
        }

        return details.stream()
                .map(this::toDetailDto)
                .collect(Collectors.toList());
    }

    private SalesReportDetailDto toDetailDto(SalesReportDetail detail) {
        return SalesReportDetailDto.builder()
                .paymentMethodName(detail.getPaymentMethodName())
                .totalAmount(detail.getTotalAmount())
                .orderCount(detail.getOrderCount())
                .transactionCount(detail.getTransactionCount())
                .averageTransactionAmount(detail.getAverageTransactionAmount())
                .build();
    }

    private List<PaymentMethodSummaryDto> toPaymentMethodSummaryDtoList(List<PaymentMethodSummary> summaries) {
        if (summaries == null) {
            return null;
        }

        return summaries.stream()
                .map(this::toPaymentMethodSummaryDto)
                .collect(Collectors.toList());
    }

    private PaymentMethodSummaryDto toPaymentMethodSummaryDto(PaymentMethodSummary summary) {
        return PaymentMethodSummaryDto.builder()
                .methodName(summary.getMethodName())
                .totalAmount(summary.getTotalAmount())
                .transactionCount(summary.getTransactionCount())
                .percentage(summary.getPercentage())
                .build();
    }

    private List<CashRegisterSummaryDto> toCashRegisterSummaryDtoList(List<CashRegisterSummary> summaries) {
        if (summaries == null) {
            return null;
        }

        return summaries.stream()
                .map(this::toCashRegisterSummaryDto)
                .collect(Collectors.toList());
    }

    private CashRegisterSummaryDto toCashRegisterSummaryDto(CashRegisterSummary summary) {
        return CashRegisterSummaryDto.builder()
                .cashRegisterUuid(summary.getCashRegisterUuid())
                .cashRegisterName(summary.getCashRegisterName())
                .totalSales(summary.getTotalSales())
                .orderCount(summary.getOrderCount())
                .transactionCount(summary.getTransactionCount())
                .sessionSummaries(toSessionSummaryDtoList(summary.getSessionSummaries()))
                .build();
    }

    private List<CashRegisterSessionSummaryDto> toSessionSummaryDtoList(List<CashRegisterSessionSummary> sessions) {
        if (sessions == null) {
            return null;
        }

        return sessions.stream()
                .map(this::toSessionSummaryDto)
                .collect(Collectors.toList());
    }

    private CashRegisterSessionSummaryDto toSessionSummaryDto(CashRegisterSessionSummary session) {
        return CashRegisterSessionSummaryDto.builder()
                .sessionUuid(session.getSessionUuid())
                .operatorUserUuid(session.getOperatorUserUuid())
                .operatorUserName(session.getOperatorUserName())
                .sessionOpenTime(session.getSessionOpenTime())
                .sessionCloseTime(session.getSessionCloseTime())
                .sessionStatus(session.getSessionStatus())
                .sessionSales(session.getSessionSales())
                .sessionOrderCount(session.getSessionOrderCount())
                .sessionTransactionCount(session.getSessionTransactionCount())
                .build();
    }
}
