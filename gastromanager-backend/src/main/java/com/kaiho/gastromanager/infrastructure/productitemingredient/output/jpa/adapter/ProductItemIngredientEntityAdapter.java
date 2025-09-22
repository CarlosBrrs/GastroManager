package com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.adapter;

import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import com.kaiho.gastromanager.domain.productitemingredient.spi.ProductItemIngredientPersistencePort;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.entity.ProductItemIngredientEntity;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.mapper.ProductItemIngredientEntityMapper;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.repository.ProductItemIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductItemIngredientEntityAdapter implements ProductItemIngredientPersistencePort {

    private final ProductItemIngredientRepository productItemIngredientRepository;
    private final ProductItemIngredientEntityMapper productItemIngredientEntityMapper;

    @Override
    public List<ProductItemIngredient> findByProductItemUuids(List<UUID> productItemUuids, UUID restaurantUuid) {
        // Consultar la base de datos para obtener las relaciones productItem -> ingredient // entrega 0 si no encuentra productitemingredient que coincidan
        List<ProductItemIngredientEntity> entities = productItemIngredientRepository.findByProductItemUuidIn(productItemUuids, restaurantUuid);

        // Mapear las entidades de la base de datos a objetos de dominio
        return entities.stream()
                       .map(productItemIngredientEntityMapper::toDomain)
                       .toList();

    }
}
