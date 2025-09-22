package com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.repository;
import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSessionStatus;
import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.entity.CashRegisterSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CashRegisterSessionEntityRepository extends JpaRepository<CashRegisterSessionEntity, UUID> {

    @Query("SELECT s FROM CashRegisterSessionEntity s WHERE s.cashRegister.uuid = :cashRegisterUuid AND s.status = :status")
    Optional<CashRegisterSessionEntity> findByCashRegisterUuidAndStatus(
        @Param("cashRegisterUuid") UUID cashRegisterUuid,
        @Param("status") CashRegisterSessionStatus status
    );

    boolean existsByCashRegister_UuidAndStatus(UUID cashRegisterUuid, CashRegisterSessionStatus status);

    boolean existsByUser_UuidAndStatus(UUID userUuid, CashRegisterSessionStatus status);

    Optional<CashRegisterSessionEntity> findByUser_UuidAndStatus(UUID userUuid, CashRegisterSessionStatus status);
}