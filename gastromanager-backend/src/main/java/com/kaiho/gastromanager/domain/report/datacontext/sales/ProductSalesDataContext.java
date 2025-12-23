package com.kaiho.gastromanager.domain.report.datacontext.sales;

import com.kaiho.gastromanager.domain.order.model.OperationalStatus;
import com.kaiho.gastromanager.domain.report.datacontext.ReportDataContext;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.repository.OrderEntityRepository;
import com.kaiho.gastromanager.infrastructure.orderitem.output.jpa.entity.OrderItemEntity;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.ProductSalesReportCriteria;
import com.kaiho.gastromanager.infrastructure.report.output.jpa.specification.OrderReportSpecifications;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ProductSalesDataContext(
        ProductSalesReportCriteria criteria,
        UUID restaurantUuid,
        List<OrderItemEntity> orderItems
) implements ReportDataContext<ProductSalesReportCriteria> {

    public static ProductSalesDataContext create(
            ProductSalesReportCriteria criteria,
            UUID restaurantUuid,
            OrderEntityRepository orderEntityRepository) {

        // TODO: Filtrar solo órdenes COMPLETED (actualmente usando PENDING como placeholder)
        List<OrderEntity> orders = orderEntityRepository.findAll(
                OrderReportSpecifications.withProductSalesCriteria(criteria, restaurantUuid)
        );

        // Extraer todos los OrderItems de las órdenes, excluyendo órdenes canceladas
        List<OrderItemEntity> orderItems = orders.stream()
                                                 .filter(order -> order.getOperationalStatus() != OperationalStatus.CANCELLED)
                                                 .flatMap(order -> order.getOrderItems().stream())
                                                 .toList();

        return ProductSalesDataContext.builder()
                                      .criteria(criteria)
                                      .restaurantUuid(restaurantUuid)
                                      .orderItems(orderItems)
                                      .build();
    }

    @Override
    public ProductSalesReportCriteria getCriteria() {
        return criteria;
    }

    @Override
    public UUID getRestaurantUuid() {
        return restaurantUuid;
    }
}

