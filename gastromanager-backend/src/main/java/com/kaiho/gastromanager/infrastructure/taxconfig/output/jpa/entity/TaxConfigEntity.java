package com.kaiho.gastromanager.infrastructure.taxconfig.output.jpa.entity;

import com.kaiho.gastromanager.domain.taxconfig.model.TaxType;
import com.kaiho.gastromanager.infrastructure.common.model.Auditable;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tax_configs")
@Getter
@Setter
public class TaxConfigEntity extends Auditable implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private TaxType taxType; // Ej: "IVA", "IMPO_CONSUMO"

    @Column(nullable = false)
    private double taxRate; // 19.00, 8.00, etc.
    private String description;

    @ManyToMany(mappedBy = "taxConfigs", fetch = FetchType.EAGER)
    private List<RestaurantEntity> restaurants = new ArrayList<>();
}