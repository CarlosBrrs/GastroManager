package com.kaiho.gastromanager.application.report.dto.response.sales;

import lombok.Builder;

import java.util.List;

@Builder
public record OverviewSalesReportResponseDto(
        FiltersDto filters,                     // Filtros del reporte (rango de fechas)
        SummaryDto summary                     // Resumen general
        /*List<DailyBreakdownDto> dailyBreakdown,                 // Métricas por día
        OrderStatusDistributionDto orderStatusDistribution,               // Distribución por estado operacional
        PaymentStatusDistributionDto paymentStatusDistribution,           // Distribución por estado de pago
        InvoicingStatusDistributionDto invoicingStatusDistribution,       // Distribución por facturación
        List<PeakHourDto> peakHours,                            // Horas pico
        List<SlowHourDto> slowestHours,                         // Horas más lentas
        ComparisonWithPreviousPeriodDto comparisonWithPreviousPeriod,     // Comparación con período anterior
        CustomerBehaviorDto customerBehavior*/
) {}