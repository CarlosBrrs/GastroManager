package com.kaiho.gastromanager.infrastructure.report.output.jpa.adapter;

import com.kaiho.gastromanager.domain.report.model.CashRegisterSummary;
import com.kaiho.gastromanager.domain.report.model.CashRegisterSessionSummary;
import com.kaiho.gastromanager.domain.report.model.PaymentMethodSummary;
import com.kaiho.gastromanager.domain.report.model.SalesReport;
import com.kaiho.gastromanager.domain.report.model.SalesReportDetail;
import com.kaiho.gastromanager.domain.report.model.SalesReportSummary;
import com.kaiho.gastromanager.domain.report.spi.ReportPersistencePort;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.repository.OrderEntityRepository;
import com.kaiho.gastromanager.infrastructure.payment.output.jpa.entity.PaymentEntity;
import com.kaiho.gastromanager.infrastructure.payment.output.jpa.repository.PaymentEntityRepository;
import com.kaiho.gastromanager.infrastructure.report.input.rest.criteria.SalesReportCriteria;
import com.kaiho.gastromanager.infrastructure.report.output.jpa.specification.OrderReportSpecifications;
import com.kaiho.gastromanager.infrastructure.report.output.jpa.specification.PaymentReportSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter que implementa la persistencia para reportes de ventas
 * Utiliza los repositorios existentes del dominio con Specifications específicas de reports
 */
@Repository
@RequiredArgsConstructor
public class ReportEntityAdapter implements ReportPersistencePort {

    // Usar los repositorios existentes del dominio, no crear duplicados
    private final PaymentEntityRepository paymentEntityRepository;
    private final OrderEntityRepository orderEntityRepository;

    @Override
    public SalesReport getSalesReport(SalesReportCriteria criteria, UUID restaurantUuid) {
        // Obtener datos usando Specifications con los repositorios existentes
        List<PaymentEntity> payments = getFilteredPayments(criteria, restaurantUuid);
        List<OrderEntity> orders = getFilteredOrders(criteria, restaurantUuid);

        // Calcular métricas principales
        BigDecimal totalSales = payments.stream()
                .map(PaymentEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalOrders = orders.size();

        // Generar detalles y resumen
        List<SalesReportDetail> details = generateReportDetails(payments);
        SalesReportSummary summary = generateReportSummary(payments, orders, totalSales);

        return SalesReport.builder()
                .totalSales(totalSales)
                .totalOrders(totalOrders)
                .reportPeriodStart(criteria.dateFrom())
                .reportPeriodEnd(criteria.dateTo())
                .details(details)
                .summary(summary)
                .build();
    }

    /**
     * Obtiene pagos filtrados usando Specifications
     * Una sola línea elegante que reemplaza toda la lógica compleja anterior
     */
    private List<PaymentEntity> getFilteredPayments(SalesReportCriteria criteria, UUID restaurantUuid) {
        return paymentEntityRepository.findAll(
                PaymentReportSpecifications.withCriteria(criteria, restaurantUuid)
        );
    }

    /**
     * Obtiene órdenes filtradas usando Specifications
     * Igualmente simple y elegante
     */
    private List<OrderEntity> getFilteredOrders(SalesReportCriteria criteria, UUID restaurantUuid) {
        /*return orderEntityRepository.findAll(
            OrderReportSpecifications.withCriteria(criteria, restaurantUuid)
        );*/
        return null;
    }

    private List<SalesReportDetail> generateReportDetails(List<PaymentEntity> payments) {
        Map<String, List<PaymentEntity>> groupedByPaymentMethod = payments.stream()
                .collect(Collectors.groupingBy(p -> p.getPaymentMethod().toString()));

        List<SalesReportDetail> details = new ArrayList<>();

        for (Map.Entry<String, List<PaymentEntity>> entry : groupedByPaymentMethod.entrySet()) {
            String paymentMethod = entry.getKey();
            List<PaymentEntity> methodPayments = entry.getValue();

            BigDecimal totalAmount = methodPayments.stream()
                    .map(PaymentEntity::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            int orderCount = (int) methodPayments.stream()
                    .map(p -> p.getOrder().getUuid())
                    .distinct()
                    .count();

            int transactionCount = methodPayments.size();

            BigDecimal averageTransactionAmount = transactionCount > 0
                    ? totalAmount.divide(BigDecimal.valueOf(transactionCount), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            details.add(SalesReportDetail.builder()
                    .paymentMethodName(paymentMethod)
                    .totalAmount(totalAmount)
                    .orderCount(orderCount)
                    .transactionCount(transactionCount)
                    .averageTransactionAmount(averageTransactionAmount)
                    .build());
        }

        return details;
    }

    private SalesReportSummary generateReportSummary(List<PaymentEntity> payments, List<OrderEntity> orders, BigDecimal totalSales) {
        // Calcular propinas
        BigDecimal totalTips = payments.stream()
                .map(p -> p.getTipAmount() != null ? p.getTipAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // TODO: IMPLEMENTAR CUANDO EL MANEJO DE OPERATIONAL STATUS ESTÉ FUNCIONANDO
        // Esta lógica se debe activar cuando se implemente correctamente el flujo de estados de órdenes
        /*
        // Contar órdenes completadas vs canceladas basado en operational status
        int completedOrders = (int) orders.stream()
                .filter(o -> o.getOperationalStatus() != null && 
                           ("COMPLETED".equals(o.getOperationalStatus().toString()) ||
                            "READY".equals(o.getOperationalStatus().toString()) ||
                            "DELIVERED".equals(o.getOperationalStatus().toString())))
                .count();

        int cancelledOrders = (int) orders.stream()
                .filter(o -> o.getOperationalStatus() != null && 
                           "CANCELLED".equals(o.getOperationalStatus().toString()))
                .count();

        // Si no hay órdenes completadas identificadas pero hay pagos, usar totalOrders como fallback
        if (completedOrders == 0 && !payments.isEmpty()) {
            completedOrders = orders.size();
        }
        */

        // LÓGICA TEMPORAL: Basar el conteo en pagos completados mientras se implementa operational status
        // Si hay pagos completados (estado COMPLETED), consideramos esas órdenes como "vendidas" para efectos del reporte
        int completedOrders = orders.size(); // Todas las órdenes que aparecen aquí tienen pagos completados
        int cancelledOrders = 0; // Por ahora no manejamos cancelaciones desde operational status
        
        // NOTA: Cuando operational status esté funcionando:
        // - Descomentar el código anterior
        // - Eliminar esta lógica temporal
        // - Considerar órdenes con estados: COMPLETED, READY, DELIVERED como completadas
        // - Considerar órdenes con estado: CANCELLED como canceladas
        
        // Calcular valor promedio por orden
        BigDecimal averageOrderValue = completedOrders > 0 
                ? totalSales.divide(BigDecimal.valueOf(completedOrders), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // Generar breakdown por método de pago
        List<PaymentMethodSummary> paymentMethodBreakdown = generatePaymentMethodSummary(payments, totalSales);

        // Generar breakdown por caja registradora
        List<CashRegisterSummary> cashRegisterBreakdown = generateCashRegisterSummary(payments);

        return SalesReportSummary.builder()
                .totalRevenue(totalSales)
                .totalTips(totalTips)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .averageOrderValue(averageOrderValue)
                .paymentMethodBreakdown(paymentMethodBreakdown)
                .cashRegisterBreakdown(cashRegisterBreakdown)
                .build();
    }

    private List<PaymentMethodSummary> generatePaymentMethodSummary(List<PaymentEntity> payments, BigDecimal totalSales) {
        Map<String, List<PaymentEntity>> groupedByMethod = payments.stream()
                .collect(Collectors.groupingBy(p -> p.getPaymentMethod().toString()));

        return groupedByMethod.entrySet().stream()
                .map(entry -> {
                    String methodName = entry.getKey();
                    List<PaymentEntity> methodPayments = entry.getValue();

                    BigDecimal methodTotal = methodPayments.stream()
                            .map(PaymentEntity::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal percentage = totalSales.compareTo(BigDecimal.ZERO) > 0
                            ? methodTotal.divide(totalSales, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                            : BigDecimal.ZERO;

                    return PaymentMethodSummary.builder()
                            .methodName(methodName)
                            .totalAmount(methodTotal)
                            .transactionCount(methodPayments.size())
                            .percentage(percentage)
                            .build();
                })
                              .toList();
    }

    private List<CashRegisterSummary> generateCashRegisterSummary(List<PaymentEntity> payments) {
        // Agrupar pagos por caja registradora primero
        Map<UUID, List<PaymentEntity>> paymentsByCashRegister = payments.stream()
                .collect(Collectors.groupingBy(p -> p.getCashRegisterSession().getCashRegister().getUuid()));

        return paymentsByCashRegister.entrySet().stream()
                .map(entry -> {
                    UUID cashRegisterUuid = entry.getKey();
                    List<PaymentEntity> registerPayments = entry.getValue();

                    // Calcular totales generales de la caja
                    BigDecimal totalSales = registerPayments.stream()
                            .map(PaymentEntity::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    int orderCount = (int) registerPayments.stream()
                            .map(p -> p.getOrder().getUuid())
                            .distinct()
                            .count();

                    int transactionCount = registerPayments.size();

                    String cashRegisterName = registerPayments.get(0)
                            .getCashRegisterSession()
                            .getCashRegister()
                            .getName();

                    // Generar resúmenes por sesión dentro de esta caja
                    List<CashRegisterSessionSummary> sessionSummaries = generateSessionSummaries(registerPayments);

                    return CashRegisterSummary.builder()
                            .cashRegisterUuid(cashRegisterUuid)
                            .cashRegisterName(cashRegisterName)
                            .totalSales(totalSales)
                            .orderCount(orderCount)
                            .transactionCount(transactionCount)
                            .sessionSummaries(sessionSummaries)
                            .build();
                })
                                     .toList();
    }

    /**
     * Genera resúmenes por sesión individual dentro de una caja registradora
     * Cada sesión representa el trabajo de un usuario específico en un período determinado
     */
    private List<CashRegisterSessionSummary> generateSessionSummaries(List<PaymentEntity> paymentsInCashRegister) {
        // Agrupar pagos por sesión de caja registradora
        Map<UUID, List<PaymentEntity>> paymentsBySession = paymentsInCashRegister.stream()
                .collect(Collectors.groupingBy(p -> p.getCashRegisterSession().getUuid()));

        return paymentsBySession.entrySet().stream()
                .map(entry -> {
                    UUID sessionUuid = entry.getKey();
                    List<PaymentEntity> sessionPayments = entry.getValue();

                    // Obtener información de la sesión del primer pago (todos tienen la misma sesión)
                    var session = sessionPayments.get(0).getCashRegisterSession();

                    // Calcular métricas de la sesión
                    BigDecimal sessionSales = sessionPayments.stream()
                            .map(PaymentEntity::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    int sessionOrderCount = (int) sessionPayments.stream()
                            .map(p -> p.getOrder().getUuid())
                            .distinct()
                            .count();

                    int sessionTransactionCount = sessionPayments.size();

                    return CashRegisterSessionSummary.builder()
                            .sessionUuid(sessionUuid)
                            .operatorUserUuid(session.getUser() != null ? session.getUser().getUuid() : null)
                            .operatorUserName(session.getUser() != null ? session.getUser().getName() : "Usuario no asignado")
                            .sessionOpenTime(session.getOpeningTime())
                            .sessionCloseTime(session.getClosingTime())
                            .sessionStatus(session.getStatus() != null ? session.getStatus().toString() : "UNKNOWN")
                            .sessionSales(sessionSales)
                            .sessionOrderCount(sessionOrderCount)
                            .sessionTransactionCount(sessionTransactionCount)
                            .build();
                })
                .sorted((s1, s2) -> s1.getSessionOpenTime().compareTo(s2.getSessionOpenTime())) // Ordenar por hora de apertura
                .toList();
    }
}
