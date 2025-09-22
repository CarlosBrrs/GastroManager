package com.kaiho.gastromanager.domain.taxconfig.spi;

import com.kaiho.gastromanager.domain.taxconfig.model.TaxConfig;

import java.util.List;
import java.util.UUID;

public interface TaxConfigPersistencePort {
    List<TaxConfig> getTaxesForRestaurant(UUID currentRestaurant);
}
