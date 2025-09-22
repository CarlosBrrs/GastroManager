package com.kaiho.gastromanager.infrastructure.cashregistermovement.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.entity.CashRegisterSessionEntity;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "cash_register_movements")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CashRegisterMovementEntity extends Auditable implements Serializable {

    @ManyToOne
    @JoinColumn(name = "cash_register_session_uuid", referencedColumnName = "uuid", nullable = false)
    private CashRegisterSessionEntity cashRegisterSession;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private CashRegisterMovementType movementType;

    @Enumerated(EnumType.STRING)
    @Column(name = "adjustment_direction")
    private AdjustmentDirection adjustmentDirection;

    @Column(nullable = false)
    private BigDecimal amount;

    private String reason;

    private String reference;
}

enum CashRegisterMovementType {
   INCOME, EXPENSE, ADJUSTMENT
}

enum AdjustmentDirection {
    INCREASE, DECREASE
}
