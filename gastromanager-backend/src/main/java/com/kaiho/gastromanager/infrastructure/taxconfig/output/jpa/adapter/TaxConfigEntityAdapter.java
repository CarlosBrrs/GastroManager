package com.kaiho.gastromanager.infrastructure.taxconfig.output.jpa.adapter;

import com.kaiho.gastromanager.domain.taxconfig.model.TaxConfig;
import com.kaiho.gastromanager.domain.taxconfig.spi.TaxConfigPersistencePort;
import com.kaiho.gastromanager.infrastructure.taxconfig.output.jpa.mapper.TaxConfigEntityMapper;
import com.kaiho.gastromanager.infrastructure.taxconfig.output.jpa.repository.TaxConfigEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class TaxConfigEntityAdapter implements TaxConfigPersistencePort {

    private final TaxConfigEntityRepository taxConfigEntityRepository;
    private final TaxConfigEntityMapper taxConfigEntityMapper;

    @Override
    public List<TaxConfig> getTaxesForRestaurant(UUID currentRestaurant) {
        return taxConfigEntityRepository.findTaxesByRestaurantUuid(currentRestaurant).stream()
                                        .map(taxConfigEntityMapper::toDomain)
                                        .toList();
    }
}
