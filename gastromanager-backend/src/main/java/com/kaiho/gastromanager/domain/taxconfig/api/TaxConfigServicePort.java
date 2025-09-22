package com.kaiho.gastromanager.domain.taxconfig.api;

import com.kaiho.gastromanager.domain.taxconfig.model.TaxConfig;

import java.util.List;
import java.util.UUID;

public interface TaxConfigServicePort {
    List<TaxConfig> getTaxesForRestaurant(UUID currentRestaurant);
}
