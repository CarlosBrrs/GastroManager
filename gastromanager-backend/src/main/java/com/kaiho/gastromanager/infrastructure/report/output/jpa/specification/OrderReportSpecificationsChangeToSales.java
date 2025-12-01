package com.kaiho.gastromanager.infrastructure.report.output.jpa.specification;

import com.kaiho.gastromanager.infrastructure.order.output.jpa.entity.OrderEntity;
import com.kaiho.gastromanager.infrastructure.report.input.rest.criteria.SalesReportCriteria;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Specifications para consultas dinámicas de órdenes en reportes de ventas
 * Filtra órdenes basado en sus pagos completados
 */
public class OrderReportSpecificationsChangeToSales {

    /**
     * Filtro por restaurante
     */
    public static Specification<OrderEntity> hasRestaurant(UUID restaurantUuid) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("restaurant").get("uuid"), restaurantUuid);
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
    public static Specification<OrderEntity> withCriteria(SalesReportCriteria criteria, UUID restaurantUuid) {
        return Specification.where(hasRestaurant(restaurantUuid))
                .and(hasCompletedPaymentsBetween(criteria.dateFrom(), criteria.dateTo()))
                .and(hasPaymentsInCashRegisters(criteria.cashRegisterUuids()))
                .and(hasPaymentsInSessionState(criteria.sessionState()))
                .and(hasPaymentMethods(criteria.paymentMethods()))
                .and(hasPaymentsFromUsers(criteria.assignedUserUuids()));
    }
}
