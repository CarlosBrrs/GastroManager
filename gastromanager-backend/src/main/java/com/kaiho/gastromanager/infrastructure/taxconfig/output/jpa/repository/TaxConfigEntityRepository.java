package com.kaiho.gastromanager.infrastructure.taxconfig.output.jpa.repository;

import com.kaiho.gastromanager.domain.taxconfig.model.TaxType;
import com.kaiho.gastromanager.infrastructure.taxconfig.output.jpa.entity.TaxConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TaxConfigEntityRepository extends JpaRepository<TaxConfigEntity, UUID> {

    @Query("SELECT tc FROM TaxConfigEntity tc WHERE tc.taxType=:taxType")
    TaxConfigEntity findByTaxType(TaxType taxType);

    @Query("SELECT tc FROM TaxConfigEntity tc JOIN tc.restaurants r WHERE r.uuid = :restaurantUuid")
    List<TaxConfigEntity> findTaxesByRestaurantUuid(UUID restaurantUuid);
}
