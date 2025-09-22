package com.kaiho.gastromanager.domain.cashregistersession.usecase;

import com.kaiho.gastromanager.domain.cashregister.exception.CashRegisterNotFoundException;
import com.kaiho.gastromanager.domain.cashregistersession.api.CashRegisterSessionServicePort;
import com.kaiho.gastromanager.domain.cashregistersession.exception.CashRegisterSessionAlreadyClosedException;
import com.kaiho.gastromanager.domain.cashregistersession.exception.CashRegisterSessionAlreadyOpenException;
import com.kaiho.gastromanager.domain.cashregistersession.exception.CashRegisterSessionNotFoundException;
import com.kaiho.gastromanager.domain.cashregistersession.exception.NoOpenSessionFoundException;
import com.kaiho.gastromanager.domain.cashregistersession.exception.UserAlreadyHasOpenSessionException;
import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSession;
import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSessionBalanceStatus;
import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSessionStatus;
import com.kaiho.gastromanager.domain.cashregistersession.model.PaymentMethodSummary;
import com.kaiho.gastromanager.domain.cashregistersession.spi.CashRegisterSessionPersistencePort;
import com.kaiho.gastromanager.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashRegisterSessionUseCase implements CashRegisterSessionServicePort {

    private final CashRegisterSessionPersistencePort cashRegisterSessionPersistencePort;

    @Override
    @Transactional
    public CashRegisterSession openSession(CashRegisterSession session) {
        UUID cashRegisterUuid = session.getCashRegister().getUuid();
        UUID restaurantUuid = session.getRestaurant().getUuid();

        // Obtener el usuario actual del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        UUID currentUserUuid = currentUser.getUuid();

        // Validar que la caja registradora pertenezca al restaurante actual
        if (!cashRegisterSessionPersistencePort.existsCashRegisterByUuidAndRestaurant(cashRegisterUuid, restaurantUuid)) {
            throw new CashRegisterNotFoundException(cashRegisterUuid);
        }

        // Validar que no haya una sesión abierta para esta caja
        if (cashRegisterSessionPersistencePort.existsOpenSessionByCashRegisterUuid(cashRegisterUuid)) {
            throw new CashRegisterSessionAlreadyOpenException(cashRegisterUuid);
        }

        // Validar que el usuario no tenga otra sesión abierta
        if (cashRegisterSessionPersistencePort.existsOpenSessionByUserUuid(currentUserUuid)) {
            throw new UserAlreadyHasOpenSessionException(currentUserUuid.toString());
        }

        // Configurar datos de apertura
        session.setOpeningTime(Instant.now());
        session.setStatus(CashRegisterSessionStatus.OPEN);
        session.setUser(User.builder().uuid(currentUserUuid).build());

        // Guardar la sesión
        return cashRegisterSessionPersistencePort.save(session);
    }

    @Override
    public Optional<CashRegisterSession> findOpenSessionByUserUuid(UUID userUuid) {
        return cashRegisterSessionPersistencePort.findOpenSessionByUserUuid(userUuid);
    }

    @Override
    @Transactional
    public CashRegisterSession closeSession(CashRegisterSession sessionToClose) {
        // Buscar la sesión por UUID
        CashRegisterSession session = cashRegisterSessionPersistencePort.findById(sessionToClose.getUuid())
                                                                        .orElseThrow(() -> new CashRegisterSessionNotFoundException(sessionToClose.getUuid()));

        // validar que el usuario actual sea el mismo que abrió la sesión
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        UUID currentUserUuid = currentUser.getUuid();

        if (!session.getUser().getUuid().equals(currentUserUuid)) {
            throw new RuntimeException("El usuario actual no tiene permiso para cerrar esta sesión");
        }

        // Validar que la sesión esté abierta
        if (session.getStatus() != CashRegisterSessionStatus.OPEN) {
            throw new CashRegisterSessionAlreadyClosedException(sessionToClose.getUuid());
        }
        processCloseSession(session, sessionToClose);

        return cashRegisterSessionPersistencePort.update(session);
    }

    private void processCloseSession(CashRegisterSession session, CashRegisterSession sessionToClose) {
        // 1. Calcular el monto esperado (monto de apertura + pagos en efectivo)
        BigDecimal cashPayments = cashRegisterSessionPersistencePort.calculateCashPaymentsTotalBySessionUuid(session.getUuid());
        // TODO incluir los movimientos de caja cuando esten habilitados
        BigDecimal expectedAmount = session.getOpeningAmount().add(cashPayments);

        // 2. Calcular la diferencia (monto de cierre - monto esperado)
        BigDecimal difference = sessionToClose.getClosingAmount().subtract(expectedAmount);

        // 3. Determinar el estado del balance
        CashRegisterSessionBalanceStatus balanceStatus;
        if (difference.compareTo(BigDecimal.ZERO) == 0) {
            balanceStatus = CashRegisterSessionBalanceStatus.BALANCED;
        } else if (difference.compareTo(BigDecimal.ZERO) > 0) {
            balanceStatus = CashRegisterSessionBalanceStatus.OVER;
        } else {
            balanceStatus = CashRegisterSessionBalanceStatus.SHORT;
        }

        // 4. Actualizar los campos de la sesión
        session.setClosingTime(Instant.now());
        session.setClosingAmount(sessionToClose.getClosingAmount());
        session.setExpectedAmount(expectedAmount);
        session.setDifference(difference);
        session.setBalanceStatus(balanceStatus);
        session.setStatus(CashRegisterSessionStatus.CLOSED);
        session.setNotes(sessionToClose.getNotes());
    }

    @Override
    @Transactional(readOnly = true)
    public CashRegisterSession getCurrentSessionSummary() {
        // Obtener el usuario actual del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        UUID currentUserUuid = currentUser.getUuid();

        // Buscar la sesión abierta del usuario actual
        CashRegisterSession openSession = cashRegisterSessionPersistencePort
                .findOpenSessionByUserUuid(currentUserUuid)
                .orElseThrow(() -> new NoOpenSessionFoundException(currentUserUuid));

        // Obtener resumen de pagos por método
        List<PaymentMethodSummary> paymentSummaries = cashRegisterSessionPersistencePort
                .getPaymentMethodSummaryBySessionUuid(openSession.getUuid());

        // Calcular totales
        BigDecimal totalCashPayments = cashRegisterSessionPersistencePort
                .calculateCashPaymentsTotalBySessionUuid(openSession.getUuid());
        
        BigDecimal totalCashMovements = cashRegisterSessionPersistencePort
                .calculateCashMovementsTotalBySessionUuid(openSession.getUuid());

        BigDecimal expectedCashAmount = openSession.getOpeningAmount()
                .add(totalCashPayments)
                .add(totalCashMovements);

        // Poblar los campos adicionales para el resumen
        openSession.setTotalCashPayments(totalCashPayments);
        openSession.setTotalCashMovements(totalCashMovements);
        openSession.setExpectedAmount(expectedCashAmount);
        openSession.setPaymentMethodSummaries(paymentSummaries);

        return openSession;
    }

/*    @Override
    public CashRegisterSession getSessionById(UUID sessionUuid) {
                                                 .orElseThrow(() -> new CashRegisterSessionNotFoundException(sessionUuid));
    }*/

/*    @Override
    public boolean hasOpenSession(UUID cashRegisterUuid) {
        return cashRegisterSessionPersistencePort.existsOpenSessionByCashRegisterUuid(cashRegisterUuid);
    }*/
}
