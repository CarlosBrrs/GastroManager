package com.kaiho.gastromanager.domain.order.api;

import com.kaiho.gastromanager.domain.order.model.OperationalStatus;
import com.kaiho.gastromanager.domain.order.model.Order;
import com.kaiho.gastromanager.domain.order.model.PaymentStatus;
import com.kaiho.gastromanager.infrastructure.order.output.jpa.criteria.OrderSearchCriteria;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.UUID;

public interface OrderServicePort {
    Page<Order> getAllOrders(OrderSearchCriteria criteria);

    UUID createOrder(Order order);

    UUID changeInvoicingStatus(UUID orderUuid, String newStatus);

    Order getOrderByUUID(UUID orderUuid, UUID currentRestaurant);

    void updateOrderTotalPaid(UUID orderUuid, BigDecimal nuevoTotalPaid);

    void updateOrderTotalPaidAndStatus(UUID orderUuid, BigDecimal nuevoTotalPaid, OperationalStatus operationalStatus);

    void updateOrderPaymentStatus(UUID orderUuid, PaymentStatus paymentStatus);

    void updateOrderOperationalStatus(UUID orderUuid, OperationalStatus operationalStatus);

    void updateOrderOperationalStatusToPendingIfAwaiting(UUID orderUuid);

    /**
     * Devuelve un arreglo: [totalAmount, totalPaid] para la orden indicada
     */
    BigDecimal[] getOrderTotals(UUID orderUuid);
}