package com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.entity;

import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSessionBalanceStatus;
import com.kaiho.gastromanager.domain.cashregistersession.model.CashRegisterSessionStatus;
import com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.entity.CashRegisterEntity;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.payment.output.jpa.entity.PaymentEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cash_register_sessions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CashRegisterSessionEntity extends Auditable implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cash_register_uuid", referencedColumnName = "uuid", nullable = false)
    private CashRegisterEntity cashRegister;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid", nullable = false)
    private UserEntity user;

    @Column(name = "opening_time", nullable = false)
    private Instant openingTime;

    private String notes;

    @Column(name = "closing_time")
    private Instant closingTime;

    @Column(name = "opening_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal openingAmount;

    @Column(name = "closing_amount", precision = 12, scale = 2)
    private BigDecimal closingAmount;

    @Column(name = "expected_amount", precision = 12, scale = 2)
    private BigDecimal expectedAmount;

    @Column(name = "difference", precision = 12, scale = 2)
    private BigDecimal difference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CashRegisterSessionStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "balance_status", length = 20)
    private CashRegisterSessionBalanceStatus balanceStatus;

    @OneToMany(mappedBy = "cashRegisterSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentEntity> payments = new ArrayList<>();

    // Método de conveniencia para obtener el UUID del usuario
    public UUID getUserUuid() {
        return user != null ? user.getUuid() : null;
    }

    // Método de conveniencia para obtener el UUID de la caja registradora
    public UUID getCashRegisterUuid() {
        return cashRegister != null ? cashRegister.getUuid() : null;
    }

    // Método helper para mantener la bidireccionalidad con pagos
    public void addPayment(PaymentEntity payment) {
        payments.add(payment);
        payment.setCashRegisterSession(this);
    }

    public void removePayment(PaymentEntity payment) {
        payments.remove(payment);
        payment.setCashRegisterSession(null);
    }
}
