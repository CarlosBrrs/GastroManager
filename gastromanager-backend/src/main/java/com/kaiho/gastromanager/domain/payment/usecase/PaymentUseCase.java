package com.kaiho.gastromanager.domain.payment.usecase;

import com.kaiho.gastromanager.domain.cashregistersession.api.CashRegisterSessionServicePort;
import com.kaiho.gastromanager.domain.cashregistersession.exception.NoOpenCashRegisterSessionException;
import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSession;
import com.kaiho.gastromanager.domain.order.api.OrderServicePort;
import com.kaiho.gastromanager.domain.order.model.PaymentStatus;
import com.kaiho.gastromanager.domain.payment.api.PaymentService;
import com.kaiho.gastromanager.domain.payment.exception.PaymentAmountExceededException;
import com.kaiho.gastromanager.domain.payment.exception.PaymentTransactionVerificationException;
import com.kaiho.gastromanager.domain.payment.model.Payment;
import com.kaiho.gastromanager.domain.payment.model.PaymentMethod;
import com.kaiho.gastromanager.domain.payment.spi.PaymentPersistencePort;
import com.kaiho.gastromanager.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static com.kaiho.gastromanager.domain.payment.model.PaymentState.COMPLETED;

@Service
@RequiredArgsConstructor
public class PaymentUseCase implements PaymentService {

    private final PaymentPersistencePort paymentPersistencePort;
    private final OrderServicePort orderServicePort;
    private final CashRegisterSessionServicePort cashRegisterSessionServicePort;

    @Override
    @Transactional
    public UUID createPayment(Payment payment) {
        // Obtener el usuario actual del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        UUID currentUserUuid = currentUser.getUuid();

        // Buscar la sesión de caja abierta del usuario actual
        CashRegisterSession openSession = cashRegisterSessionServicePort.findOpenSessionByUserUuid(currentUserUuid)
                                                                        .orElseThrow(() -> new NoOpenCashRegisterSessionException(currentUserUuid.toString()));

        // Relacionar el pago con la sesión de caja encontrada
        payment.setCashRegisterSession(openSession);

        // TODO convertir en un metodo para mejor lectura
        // Obtener totales de la orden
        BigDecimal[] orderTotals = orderServicePort.getOrderTotals(payment.getOrder().getUuid());
        BigDecimal totalAmount = orderTotals[0]; // Total de la orden (sin propina)
        BigDecimal totalPaid = orderTotals[1];   // Total pagado hasta ahora

        // Calcular el remaining (lo que falta por pagar)
        BigDecimal amount = payment.getAmount() != null ? payment.getAmount() : BigDecimal.ZERO;
        BigDecimal remaining = totalAmount.subtract(totalPaid);

        if (amount.compareTo(remaining) > 0) {
            throw new PaymentAmountExceededException("El pago de " + amount + " excede el monto restante de " + remaining);
        }

        // El pago viene en estado pending por defecto
        processPaymentByMethod(payment);

        // Guardar el pago
        UUID paymentUuid = paymentPersistencePort.save(payment);

        // Calcular el nuevo total pagado
        BigDecimal nuevoTotalPaid = totalPaid.add(amount);

        // Decidir si este pago cubre exactamente el remaining
        boolean coversRemaining = amount.compareTo(remaining) == 0;

        if (coversRemaining) {
            // Si cubre el remaining, actualizar totalPaid y estados en las operaciones necesarias
            updateOrderWithFullPayment(payment.getOrder().getUuid(), nuevoTotalPaid, totalAmount);
        } else {
            // Si no cubre todo, solo actualizar el totalPaid
            orderServicePort.updateOrderTotalPaid(payment.getOrder().getUuid(), nuevoTotalPaid);
            // Actualizar solo el estado de pago (sin tocar el operacional)
            PaymentStatus newPaymentStatus = determinePaymentStatus(totalAmount, nuevoTotalPaid);
            orderServicePort.updateOrderPaymentStatus(payment.getOrder().getUuid(), newPaymentStatus);
        }

        return paymentUuid;
    }

    private void updateOrderWithFullPayment(UUID orderUuid, BigDecimal nuevoTotalPaid, BigDecimal totalAmount) {
        // Actualizar totalPaid
        orderServicePort.updateOrderTotalPaid(orderUuid, nuevoTotalPaid);

        // Actualizar estado de pago a FULLY_PAID
        orderServicePort.updateOrderPaymentStatus(orderUuid, PaymentStatus.FULLY_PAID);

        // Actualizar estado operacional solo si la orden está en AWAITING_PAYMENT
        orderServicePort.updateOrderOperationalStatusToPendingIfAwaiting(orderUuid);
    }

    private PaymentStatus determinePaymentStatus(BigDecimal totalAmount, BigDecimal totalPaid) {
        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
            return PaymentStatus.UNPAID;
        } else if (totalPaid.compareTo(totalAmount) == 0) {
            return PaymentStatus.FULLY_PAID;
        } else {
            return PaymentStatus.PARTIALLY_PAID;
        }
    }

    private void processPaymentByMethod(Payment payment) {
        // Cambiar estado según método de pago
        if (isVirtualPayment(payment.getPaymentMethod())) {
            if (!verifyTransactionId(payment.getTransactionId())) {
                throw new PaymentTransactionVerificationException("Transaction ID inválido: " + payment.getTransactionId());
            }
            setPaymentStateCompleted(payment);
        } else if (isCashPayment(payment.getPaymentMethod())) {
            setPaymentStateCompleted(payment);
        }
        // Si no es virtual ni cash, se deja en pending por defecto
    }

    private boolean isVirtualPayment(PaymentMethod method) {
        // Métodos que requieren verificación de transactionId
        return method != null && (method == PaymentMethod.CREDIT_CARD
                || method == PaymentMethod.DEBIT_CARD
                || method == PaymentMethod.TRANSFER
                || method == PaymentMethod.NEQUI
                || method == PaymentMethod.DAVIPLATA);
    }

    private boolean isCashPayment(PaymentMethod method) {
        return method != null && method == PaymentMethod.CASH;
    }

    private boolean verifyTransactionId(String transactionId) {
        // Verificación mockeada: debe tener más de 5 caracteres para ser válido
        return transactionId != null && transactionId.trim().length() > 5;
    }

    private void setPaymentStateCompleted(Payment payment) {
        payment.setState(COMPLETED);
    }
}
