package com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.adapter;

import com.kaiho.gastromanager.domain.cashregister.exception.CashRegisterNotFoundException;
import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSession;
import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSessionStatus;
import com.kaiho.gastromanager.domain.cashregistersession.model.PaymentMethodSummary;
import com.kaiho.gastromanager.domain.cashregistersession.spi.CashRegisterSessionPersistencePort;
import com.kaiho.gastromanager.domain.user.exception.UserDoesNotExistException;
import com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.entity.CashRegisterEntity;
import com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.repository.CashRegisterEntityRepository;
import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.entity.CashRegisterSessionEntity;
import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.mapper.CashRegisterSessionEntityMapper;
import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.repository.CashRegisterSessionEntityRepository;
import com.kaiho.gastromanager.infrastructure.payment.output.jpa.entity.PaymentEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CashRegisterSessionEntityAdapter implements CashRegisterSessionPersistencePort {

    private final CashRegisterSessionEntityRepository sessionRepository;
    private final CashRegisterEntityRepository cashRegisterRepository;
    private final CashRegisterSessionEntityMapper sessionMapper;
    private final UserEntityRepository userRepository;

    @Override
    public CashRegisterSession save(CashRegisterSession session) {
        CashRegisterSessionEntity entity = sessionMapper.toEntity(session);

        // Obtener la entidad de CashRegister para establecer la relación
        CashRegisterEntity cashRegisterEntity = cashRegisterRepository.findById(session.getCashRegister().getUuid())
                                                                      .orElseThrow(() -> new CashRegisterNotFoundException(session.getCashRegister().getUuid()));

        // Obtener la entidad de User para establecer la relación bidireccional
        UserEntity userEntity = userRepository.findById(session.getUser().getUuid())
                                              .orElseThrow(() -> new UserDoesNotExistException(session.getUser().getUuid()));

        // Establecer las relaciones bidireccionales
        cashRegisterEntity.addSession(entity);
        userEntity.addCashRegisterSession(entity);

        CashRegisterSessionEntity savedEntity = sessionRepository.save(entity);
        return sessionMapper.toDomain(savedEntity);
    }

    @Override
    public CashRegisterSession update(CashRegisterSession session) {
        CashRegisterSessionEntity existingEntity = sessionRepository.findById(session.getUuid())
                                                                    .orElseThrow(() -> new RuntimeException("Sesión no encontrada: " + session.getUuid()));

        existingEntity.setClosingTime(session.getClosingTime());
        existingEntity.setClosingAmount(session.getClosingAmount());
        existingEntity.setStatus(session.getStatus());
        existingEntity.setNotes(session.getNotes());
        existingEntity.setBalanceStatus(session.getBalanceStatus());
        existingEntity.setDifference(session.getDifference());
        existingEntity.setStatus(session.getStatus());
        existingEntity.setExpectedAmount(session.getExpectedAmount());

        CashRegisterSessionEntity updatedEntity = sessionRepository.save(existingEntity);
        return sessionMapper.toDomain(updatedEntity);
    }

    @Override
    public Optional<CashRegisterSession> findById(UUID sessionUuid) {
        return sessionRepository.findById(sessionUuid)
                                .map(sessionMapper::toDomain);
    }

/*    @Override
    public Optional<CashRegisterSession> findOpenSessionByCashRegisterUuid(UUID cashRegisterUuid) {
        return sessionRepository.findByCashRegisterUuidAndStatus(cashRegisterUuid, CashRegisterSessionStatus.OPEN)
                                .map(sessionMapper::toDomain);
    }*/

    @Override
    public boolean existsOpenSessionByCashRegisterUuid(UUID cashRegisterUuid) {
        return sessionRepository.existsByCashRegister_UuidAndStatus(cashRegisterUuid, CashRegisterSessionStatus.OPEN);
    }

    @Override
    public boolean existsCashRegisterByUuidAndRestaurant(UUID cashRegisterUuid, UUID restaurantUuid) {
        return cashRegisterRepository.existsByUuidAndRestaurantUuid(cashRegisterUuid, restaurantUuid);
    }

    @Override
    public boolean existsOpenSessionByUserUuid(UUID userUuid) {
        return sessionRepository.existsByUser_UuidAndStatus(userUuid, CashRegisterSessionStatus.OPEN);
    }

    @Override
    public Optional<CashRegisterSession> findOpenSessionByUserUuid(UUID userUuid) {
        return sessionRepository.findByUser_UuidAndStatus(userUuid, CashRegisterSessionStatus.OPEN)
                                .map(sessionMapper::toDomain);
    }

    @Override
    public BigDecimal calculateCashPaymentsTotalBySessionUuid(UUID sessionUuid) {
        return sessionRepository.findById(sessionUuid)
                                .map(session -> session.getPayments().stream()
                                                       .filter(payment -> "CASH".equals(payment.getPaymentMethod().name()))
                                                       .map(PaymentEntity::getAmount)
                                                       .reduce(BigDecimal.ZERO, BigDecimal::add))
                                .orElse(BigDecimal.ZERO);
    }


    @Override
    public List<PaymentMethodSummary> getPaymentMethodSummaryBySessionUuid(UUID sessionUuid) {
        return sessionRepository.findById(sessionUuid)
                                .map(session -> session.getPayments().stream()
                                                       .collect(Collectors.groupingBy(
                                                               PaymentEntity::getPaymentMethod,
                                                               Collectors.collectingAndThen(
                                                                       Collectors.toList(),
                                                                       payments -> PaymentMethodSummary.builder()
                                                                                                       .paymentMethodName(payments.get(0).getPaymentMethod().name())
                                                                                                       .paymentMethodDescription(payments.get(0).getPaymentMethod().getDisplayName())
                                                                                                       .totalAmount(payments.stream()
                                                                                                                            .map(PaymentEntity::getAmount)
                                                                                                                            .reduce(BigDecimal.ZERO, BigDecimal::add))
                                                                                                       .transactionCount(payments.size())
                                                                                                       .build()
                                                               )))
                                                       .values()
                                                       .stream()
                                                       .toList())
                                .orElse(List.of());
    }

    @Override
    public BigDecimal calculateCashMovementsTotalBySessionUuid(UUID sessionUuid) {
        // TODO: Implementar cuando estén disponibles los movimientos de caja
        return BigDecimal.ZERO;
    }
}
