package com.kaiho.gastromanager.domain.report.datacontext.sales;

import com.kaiho.gastromanager.domain.report.datacontext.ReportDataContext;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.repository.OrderEntityRepository;
import com.kaiho.gastromanager.infrastructure.payment.output.jpa.entity.PaymentEntity;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.OverviewSalesReportCriteria;
import com.kaiho.gastromanager.infrastructure.report.output.jpa.specification.OrderReportSpecifications;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record OverviewSalesDataContext(
        OverviewSalesReportCriteria criteria,
        UUID restaurantUuid,
        List<PaymentEntity> payments,
        List<OrderEntity> orders
) implements ReportDataContext<OverviewSalesReportCriteria> {

    public static OverviewSalesDataContext create(
            OverviewSalesReportCriteria criteria,
            UUID restaurantUuid,
            OrderEntityRepository orderEntityRepository) {

        List<OrderEntity> ordersWithoutPayments = orderEntityRepository.findAll(
                OrderReportSpecifications.withOverviewCriteria(criteria, restaurantUuid)
        );

        if (ordersWithoutPayments.isEmpty()) {
            return OverviewSalesDataContext.builder()
                                           .criteria(criteria)
                                           .restaurantUuid(restaurantUuid)
                                           .payments(List.of())
                                           .orders(List.of())
                                           .build();
        }

        List<UUID> orderUuids = ordersWithoutPayments.stream()
                                                      .map(OrderEntity::getUuid)
                                                      .toList();

        List<OrderEntity> ordersWithPayments = orderEntityRepository.findAllWithPayments(orderUuids);

        List<PaymentEntity> payments = ordersWithPayments.stream()
                                                         .flatMap(order -> order.getPayments().stream())
                                                         .toList();

        return OverviewSalesDataContext.builder()
                                       .criteria(criteria)
                                       .restaurantUuid(restaurantUuid)
                                       .payments(payments)
                                       .orders(ordersWithPayments)
                                       .build();
    }

    @Override
    public OverviewSalesReportCriteria getCriteria() {
        return criteria;
    }

    @Override
    public UUID getRestaurantUuid() {
        return restaurantUuid;
    }
}