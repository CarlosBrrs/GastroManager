package com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.entity;

import com.kaiho.gastromanager.infrastructure.cashregistersession.output.jpa.entity.CashRegisterSessionEntity;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cash_registers")
@Filter(name = "restaurantFilter", condition = "restaurant_uuid = :restaurantUuid")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CashRegisterEntity extends Auditable implements Serializable {

    @ManyToOne
    @JoinColumn(name = "restaurant_uuid", referencedColumnName = "uuid", nullable = false)
    private RestaurantEntity restaurant;

    @Column(nullable = false, length = 100)
    private String name;

    private String location;

    private String description;

    @Column(name = "device_id")
    private String deviceId;

    @OneToMany(mappedBy = "cashRegister", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CashRegisterSessionEntity> sessions = new ArrayList<>();

    public void addSession(CashRegisterSessionEntity session) {
        sessions.add(session);
        session.setCashRegister(this);
    }

}