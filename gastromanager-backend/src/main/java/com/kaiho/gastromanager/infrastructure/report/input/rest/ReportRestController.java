package com.kaiho.gastromanager.infrastructure.report.input.rest;

import com.kaiho.gastromanager.application.report.dto.response.SalesReportResponseDto;
import com.kaiho.gastromanager.application.report.handler.ReportHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.report.input.rest.criteria.SalesReportCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

/**
 * Controlador REST para generar reportes de ventas
 * Endpoint principal: GET /reports/sales con filtros flexibles
 */
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportRestController {

    private final ReportHandler reportHandler;

    /**
     * Genera reporte de ventas con filtros flexibles
     *
     * @param dateFrom          Fecha inicio (ISO 8601) - OBLIGATORIO
     * @param dateTo            Fecha fin (ISO 8601) - OBLIGATORIO
     * @param cashRegisterUuids Lista de UUIDs de cajas registradoras (opcional, null=todas)
     * @param sessionState      Estado de sesiones: OPENED, CLOSED, ALL (default: ALL)
     * @param paymentMethods    Lista de métodos de pago (opcional, null=todos)
     * @param assignedUserUuids Lista de UUIDs de usuarios/cajeros (opcional, null=todos)
     * @return Reporte completo de ventas con métricas y detalles
     */
    @GetMapping("/sales")
    public ResponseEntity<ApiGenericResponse<SalesReportResponseDto>> getSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant dateFrom,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant dateTo,
            @RequestParam(required = false) List<UUID> cashRegisterUuids,
            @RequestParam(defaultValue = "ALL") String sessionState,
            @RequestParam(required = false) List<String> paymentMethods,
            @RequestParam(required = false) List<UUID> assignedUserUuids
    ) {
        SalesReportCriteria criteria = SalesReportCriteria.builder()
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .cashRegisterUuids(cashRegisterUuids)
                .sessionState(sessionState)
                .paymentMethods(paymentMethods)
                .assignedUserUuids(assignedUserUuids)
                .build();

        ApiGenericResponse<SalesReportResponseDto> response = reportHandler.getSalesReport(criteria);
        return new ResponseEntity<>(response, OK);
    }
}
