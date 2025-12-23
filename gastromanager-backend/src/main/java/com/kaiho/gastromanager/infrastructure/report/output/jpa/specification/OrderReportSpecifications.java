package com.kaiho.gastromanager.infrastructure.report.output.jpa.specification;

import com.kaiho.gastromanager.domain.order.model.OperationalStatus;
import com.kaiho.gastromanager.domain.order.model.PaymentStatus;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.report.input.rest.criteria.OrdersReportCriteria;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.OverviewSalesReportCriteria;
import com.kaiho.gastromanager.infrastructure.report.input.rest.sales.criteria.ProductSalesReportCriteria;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderReportSpecifications {

    private static Specification<OrderEntity> hasRestaurant(UUID restaurantUuid) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("restaurant").get("uuid"), restaurantUuid);
    }

    private static Specification<OrderEntity> hasOrdersBetween(Instant dateFrom, Instant dateTo) {
        return (root, query, criteriaBuilder) -> {
            if (dateFrom == null || dateTo == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.between(root.get("createdDate"), dateFrom, dateTo);
        };
    }

    private static Specification<OrderEntity> hasOperationalStatuses(List<OperationalStatus> operationalStatuses) {
        return (root, query, criteriaBuilder) -> {
            if (operationalStatuses == null || operationalStatuses.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("operationalStatus").in(operationalStatuses);
        };
    }

    private static Specification<OrderEntity> hasPaymentStatuses(List<PaymentStatus> paymentStatuses) {
        return (root, query, criteriaBuilder) -> {
            if (paymentStatuses == null || paymentStatuses.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("paymentStatus").in(paymentStatuses);
        };
    }

    /**
     * Filtro por órdenes que tengan pagos completados en el rango de fechas
     */
    public static Specification<OrderEntity> hasCompletedPaymentsBetween(Instant dateFrom, Instant dateTo) {
        return (root, query, criteriaBuilder) -> {
            var paymentJoin = root.join("payments", JoinType.INNER);
            return criteriaBuilder.and(
                    criteriaBuilder.equal(paymentJoin.get("state"), "COMPLETED"),
                    criteriaBuilder.between(paymentJoin.get("createdDate"), dateFrom, dateTo)
            );
        };
    }

    /**
     * Filtro por cajas registradoras a través de los pagos
     */
    public static Specification<OrderEntity> hasPaymentsInCashRegisters(List<UUID> cashRegisterUuids) {
        return (root, query, criteriaBuilder) -> {
            if (cashRegisterUuids == null || cashRegisterUuids.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            var paymentJoin = root.join("payments", JoinType.INNER);
            var sessionJoin = paymentJoin.join("cashRegisterSession");
            var cashRegisterJoin = sessionJoin.join("cashRegister");

            return cashRegisterJoin.get("uuid").in(cashRegisterUuids);
        };
    }

    /**
     * Filtro por estado de sesión a través de los pagos
     */
    public static Specification<OrderEntity> hasPaymentsInSessionState(String sessionState) {
        return (root, query, criteriaBuilder) -> {
            if (sessionState == null || "ALL".equalsIgnoreCase(sessionState)) {
                return criteriaBuilder.conjunction();
            }
            var paymentJoin = root.join("payments", JoinType.INNER);
            var sessionJoin = paymentJoin.join("cashRegisterSession");

            return criteriaBuilder.equal(sessionJoin.get("status"), sessionState);
        };
    }

    /**
     * Filtro por métodos de pago
     */
    public static Specification<OrderEntity> hasPaymentMethods(List<String> paymentMethods) {
        return (root, query, criteriaBuilder) -> {
            if (paymentMethods == null || paymentMethods.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            var paymentJoin = root.join("payments", JoinType.INNER);
            return paymentJoin.get("paymentMethod").in(paymentMethods);
        };
    }

    /**
     * Filtro por usuarios asignados a través de las sesiones de pago
     */
    public static Specification<OrderEntity> hasPaymentsFromUsers(List<UUID> userUuids) {
        return (root, query, criteriaBuilder) -> {
            if (userUuids == null || userUuids.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            var paymentJoin = root.join("payments", JoinType.INNER);
            var sessionJoin = paymentJoin.join("cashRegisterSession");
            var userJoin = sessionJoin.join("user");

            return userJoin.get("uuid").in(userUuids);
        };
    }

    /**
     * Specification compuesta para órdenes con pagos filtrados
     */
    public static Specification<OrderEntity> withCriteria(OrdersReportCriteria criteria, UUID restaurantUuid) {
        return Specification.where(hasRestaurant(restaurantUuid))
                            .and(hasOrdersBetween(criteria.dateFrom(), criteria.dateTo()))
                            .and(hasOperationalStatuses(criteria.operationalStatuses()))
                            .and(hasPaymentStatuses(criteria.paymentStatuses()));
    }

    public static Specification<OrderEntity> withOverviewCriteria(
            OverviewSalesReportCriteria criteria,
            UUID restaurantUuid) {
        return Specification.where(hasRestaurant(restaurantUuid))
                            .and(hasOrdersBetween(criteria.dateFrom(), criteria.dateTo()));
    }

    public static Specification<OrderEntity> withProductSalesCriteria(
            ProductSalesReportCriteria criteria,
            UUID restaurantUuid) {
        return Specification.where(hasRestaurant(restaurantUuid))
                            .and(hasOrdersBetween(criteria.dateFrom(), criteria.dateTo()));
    }

    private OrderReportSpecifications() {
    }
}
