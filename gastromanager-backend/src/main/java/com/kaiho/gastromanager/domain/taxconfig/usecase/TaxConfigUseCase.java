package com.kaiho.gastromanager.domain.taxconfig.usecase;

import com.kaiho.gastromanager.domain.taxconfig.api.TaxConfigServicePort;
import com.kaiho.gastromanager.domain.taxconfig.model.TaxConfig;
import com.kaiho.gastromanager.domain.taxconfig.spi.TaxConfigPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaxConfigUseCase implements TaxConfigServicePort {

    private final TaxConfigPersistencePort taxConfigPersistencePort;

    @Override
    public List<TaxConfig> getTaxesForRestaurant(UUID currentRestaurant) {
        return taxConfigPersistencePort.getTaxesForRestaurant(currentRestaurant);
    }
}
