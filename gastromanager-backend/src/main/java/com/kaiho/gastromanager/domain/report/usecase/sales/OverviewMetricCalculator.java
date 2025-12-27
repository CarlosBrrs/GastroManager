package com.kaiho.gastromanager.domain.report.usecase.sales;

import com.kaiho.gastromanager.domain.order.model.OperationalStatus;
import com.kaiho.gastromanager.domain.report.datacontext.sales.OverviewSalesDataContext;
import com.kaiho.gastromanager.domain.report.model.sales.overview.OverviewSummary;
import com.kaiho.gastromanager.domain.report.usecase.MetricCalculator;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.payment.output.jpa.entity.PaymentEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class OverviewMetricCalculator implements MetricCalculator<OverviewSalesDataContext, OverviewSummary> {

    private static Integer getTotalOrders(OverviewSalesDataContext dataContext) {
        return dataContext.orders().size();
    }

    private static Integer getCompletedOrders(OverviewSalesDataContext dataContext) {
        return Math.toIntExact(dataContext.orders().stream()
                                          //TODO cambiar a COMPLETED cuando este implementado
                                          .filter(o -> o.getOperationalStatus() == OperationalStatus.PENDING)
                                          .count());
    }

    private static Integer getCancelledOrders(OverviewSalesDataContext dataContext) {
        return Math.toIntExact(dataContext.orders().stream()
                                          .filter(o -> o.getOperationalStatus() == OperationalStatus.CANCELLED)
                                          .count());
    }

    private static Double getCancellationRate(Integer totalOrders, Integer cancelledOrders) {
        return totalOrders == 0
                ? 0.0
                : roundDouble((double) cancelledOrders / totalOrders * 100.0);
    }

    private static BigDecimal getTotalOrderValue(OverviewSalesDataContext dataContext) {
        return dataContext.orders().stream()
                          .map(OrderEntity::getTotalAmount)
                          .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal getTotalRevenue(OverviewSalesDataContext dataContext) {
        return dataContext.payments().stream()
                          .map(PaymentEntity::getAmount)
                          .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal getTotalTips(OverviewSalesDataContext dataContext) {
        return dataContext.payments().stream()
                          .map(PaymentEntity::getTipAmount)
                          .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal getTotalPaid(OverviewSalesDataContext dataContext) {
        return dataContext.payments().stream()
                          .map(p -> sumNullable(p.getAmount(), p.getTipAmount()))
                          .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal getPendingAmount(BigDecimal totalOrderValue, BigDecimal totalRevenue) {
        return totalOrderValue.subtract(totalRevenue);
    }

    private static Double getCollectionRate(BigDecimal totalRevenue, BigDecimal totalOrderValue) {
        if (totalOrderValue.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return roundDouble(
                totalRevenue.divide(totalOrderValue, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .doubleValue()
        );
    }

    private static Double getAverageOrderValue(BigDecimal totalOrderValue, Integer completedOrders) {
        return completedOrders == 0
                ? 0.0
                : roundDouble(totalOrderValue.divide(BigDecimal.valueOf(completedOrders), RoundingMode.HALF_UP).doubleValue(), 2);
    }

    private static BigDecimal sumNullable(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a)
                .add(b == null ? BigDecimal.ZERO : b);
    }

    private static Double roundDouble(double value) {
        return roundDouble(value, 2);
    }

    private static Double roundDouble(double value, int scale) {
        return BigDecimal.valueOf(value)
                         .setScale(scale, RoundingMode.HALF_UP)
                         .doubleValue();
    }

    @Override
    public OverviewSummary calculate(OverviewSalesDataContext dataContext) {
        Integer totalOrders = getTotalOrders(dataContext);
        Integer completedOrders = getCompletedOrders(dataContext);
        Integer cancelledOrders = getCancelledOrders(dataContext);
        Double cancellationRate = getCancellationRate(totalOrders, cancelledOrders);
        BigDecimal totalOrderValue = getTotalOrderValue(dataContext);
        BigDecimal totalRevenue = getTotalRevenue(dataContext);
        BigDecimal totalTips = getTotalTips(dataContext);
        BigDecimal totalPaid = getTotalPaid(dataContext);

        // Métricas derivadas
        BigDecimal pendingAmount = getPendingAmount(totalOrderValue, totalRevenue);
        Double collectionRate = getCollectionRate(totalRevenue, totalOrderValue);

        Double averageOrderValue = getAverageOrderValue(totalOrderValue, completedOrders);

        return OverviewSummary.builder()
                              .totalOrders(totalOrders)
                              .completedOrders(completedOrders)
                              .cancelledOrders(cancelledOrders)
                              .cancellationRate(cancellationRate)
                              .totalOrderValue(totalOrderValue)
                              .totalRevenue(totalRevenue)
                              .totalTips(totalTips)
                              .totalPaidWithTips(totalPaid)
                              .pendingAmount(pendingAmount)
                              .collectionRate(collectionRate)
                              .averageOrderValue(averageOrderValue)
                              .build();
    }

    @Override
    public String getMetricName() {
        return "overviewSummary";
    }
}
