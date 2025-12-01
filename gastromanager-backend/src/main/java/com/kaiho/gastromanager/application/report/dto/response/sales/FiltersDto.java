package com.kaiho.gastromanager.application.report.dto.response.sales;

import java.time.Instant;

import lombok.Builder;

@Builder
public record FiltersDto(
        Instant startDate,    // Fecha inicial del período
        Instant endDate,      // Fecha final del período
        Integer totalDays    // Cantidad de días analizados
) {}