package com.kaiho.gastromanager.application.report.dto.response.sales;

import lombok.Builder;

import java.time.Instant;

@Builder
public record FiltersDto(
        Instant startDate,    // Fecha inicial del período
        Instant endDate,      // Fecha final del período
        Integer totalDays    // Cantidad de días analizados
) {
}