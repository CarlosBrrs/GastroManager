package com.kaiho.gastromanager.infrastructure.report.output.jpa.specification;

import com.kaiho.gastromanager.infrastructure.payment.output.jpa.entity.PaymentEntity;
import com.kaiho.gastromanager.infrastructure.report.input.rest.criteria.SalesReportCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Specifications para consultas dinámicas de reportes de ventas
 * Permite crear consultas complejas de forma modular y reutilizable
 */
public class PaymentReportSpecifications {

    /**
     * Filtro por restaurante (siempre obligatorio)
     */
    public static Specification<PaymentEntity> hasRestaurant(UUID restaurantUuid) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.join("order").get("restaurant").get("uuid"),
                        restaurantUuid
                );
    }

    /**
     * Filtro por rango de fechas
     */
    public static Specification<PaymentEntity> hasDateBetween(Instant dateFrom, Instant dateTo) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdDate"), dateFrom, dateTo);
    }

    /**
     * Filtro por estado completado
     */
    public static Specification<PaymentEntity> isCompleted() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("state"), "COMPLETED");
    }

    /**
     * Filtro por múltiples cajas registradoras
     */
    public static Specification<PaymentEntity> hasCashRegisters(List<UUID> cashRegisterUuids) {
        return (root, query, criteriaBuilder) -> {
            if (cashRegisterUuids == null || cashRegisterUuids.isEmpty()) {
                return criteriaBuilder.conjunction(); // TRUE - no filter
            }
            return root.join("cashRegisterSession")
                       .join("cashRegister")
                       .get("uuid")
                       .in(cashRegisterUuids);
        };
    }

    /**
     * Filtro por estado de sesión
     */
    public static Specification<PaymentEntity> hasSessionState(String sessionState) {
        return (root, query, criteriaBuilder) -> {
            if (sessionState == null || "ALL".equalsIgnoreCase(sessionState)) {
                return criteriaBuilder.conjunction(); // TRUE - no filter
            }
            return criteriaBuilder.equal(
                    root.join("cashRegisterSession").get("status"),
                    sessionState
            );
        };
    }

    /**
     * Filtro por múltiples métodos de pago
     */
    public static Specification<PaymentEntity> hasPaymentMethods(List<String> paymentMethods) {
        return (root, query, criteriaBuilder) -> {
            if (paymentMethods == null || paymentMethods.isEmpty()) {
                return criteriaBuilder.conjunction(); // TRUE - no filter
            }
            return root.get("paymentMethod").in(paymentMethods);
        };
    }

    /**
     * Filtro por múltiples usuarios asignados
     */
    public static Specification<PaymentEntity> hasAssignedUsers(List<UUID> userUuids) {
        return (root, query, criteriaBuilder) -> {
            if (userUuids == null || userUuids.isEmpty()) {
                return criteriaBuilder.conjunction(); // TRUE - no filter
            }
            return root.join("cashRegisterSession")
                       .join("user")
                       .get("uuid")
                       .in(userUuids);
        };
    }

    /**
     * Specification compuesta que combina todos los filtros de criterios
     */
    public static Specification<PaymentEntity> withCriteria(SalesReportCriteria criteria, UUID restaurantUuid) {
        return Specification.where(hasRestaurant(restaurantUuid))
                            .and(hasDateBetween(criteria.dateFrom(), criteria.dateTo()))
                            .and(isCompleted())
                            .and(hasCashRegisters(criteria.cashRegisterUuids()))
                            .and(hasSessionState(criteria.sessionState()))
                            .and(hasPaymentMethods(criteria.paymentMethods()))
                            .and(hasAssignedUsers(criteria.assignedUserUuids()));
    }
}
