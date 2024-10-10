package com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity;

import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ingredient")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@EntityListeners(AuditingEntityListener.class)
@Builder
public class IngredientEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    private int stockLevel;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private double pricePerUnit;

    private Instant createdDateTime;

    private String createdBy;

    private Instant lastUpdated;

    private String updatedBy;

    private String updateReason;

    private int minimumStockLevel;
}
