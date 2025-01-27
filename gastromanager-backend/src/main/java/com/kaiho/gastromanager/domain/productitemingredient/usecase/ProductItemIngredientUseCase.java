package com.kaiho.gastromanager.domain.productitemingredient.usecase;

import com.kaiho.gastromanager.domain.productitemingredient.api.ProductItemIngredientServicePort;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import com.kaiho.gastromanager.domain.productitemingredient.spi.ProductItemIngredientPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductItemIngredientUseCase implements ProductItemIngredientServicePort {

    private final ProductItemIngredientPersistencePort productItemIngredientPersistencePort;

    @Override
    public List<ProductItemIngredient> getByProductItemUuids(List<UUID> productItemUuids, UUID restaurantUuid) {
        return productItemIngredientPersistencePort.findByProductItemUuids(productItemUuids, restaurantUuid);
    }
}
