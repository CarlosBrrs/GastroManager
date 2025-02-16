package com.kaiho.gastromanager.infrastructure.productitem.output.jpa.adapter;

import com.kaiho.gastromanager.domain.ingredient.exception.IngredientDoesNotExistException;
import com.kaiho.gastromanager.domain.productitem.exception.ProductItemDoesNotExistException;
import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import com.kaiho.gastromanager.domain.productitem.spi.ProductItemPersistencePort;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository.IngredientEntityRepository;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.mapper.ProductItemEntityMapper;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.repository.ProductItemRepository;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.entity.ProductItemIngredientEntity;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.mapper.ProductItemIngredientEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class ProductItemEntityAdapter implements ProductItemPersistencePort {

    private final ProductItemRepository productItemEntityRepository;
    private final ProductItemEntityMapper productItemEntityMapper;
    private final ProductItemIngredientEntityMapper productItemIngredientEntityMapper;
    private final IngredientEntityRepository ingredientEntityRepository;

    @Override
    public List<ProductItem> findAllProductItems() {
        return productItemEntityRepository.findAll().stream()
                .map(productItemEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ProductItem> findProductItemByUuid(UUID uuid, UUID restaurantUuid) {
        Optional<ProductItemEntity> productItemEntity = productItemEntityRepository.findById(uuid, restaurantUuid);
        return productItemEntity
                .map(productItemEntityMapper::toDomain);
    }

    @Override
    public UUID saveProductItem(ProductItem productItem) {

        ProductItemEntity productItemEntity = productItemEntityMapper.toEntity(productItem);

        List<ProductItemIngredientEntity> productItemIngredientEntityList = productItem.getIngredients().stream()
                .map(productItemIngredientEntityMapper::toEntity).toList();

        productItemIngredientEntityList.forEach(productItemEntity::addProductItemIngredient);

        ProductItemEntity savedEntity = productItemEntityRepository.save(productItemEntity);
        return savedEntity.getUuid();
    }

    @Override
    public ProductItem updateProductItem(UUID uuid, ProductItem updatedProductItem) {
        ProductItemEntity existingEntity = productItemEntityRepository.findById(uuid)
                .orElseThrow(() -> new ProductItemDoesNotExistException(uuid));

        existingEntity.setName(updatedProductItem.getName());
        existingEntity.setDescription(updatedProductItem.getDescription());
        existingEntity.setCategory(updatedProductItem.getCategory());
        existingEntity.setPrice(updatedProductItem.getPrice());

        // 🔥 Manejo correcto de los ingredientes
        updateIngredients(existingEntity, updatedProductItem.getIngredients());

        ProductItemEntity saved = productItemEntityRepository.save(existingEntity);
        return productItemEntityMapper.toDomain(saved);
    }


    private void updateIngredients(ProductItemEntity existingEntity, List<ProductItemIngredient> updatedIngredients) {
        List<ProductItemIngredientEntity> currentIngredients = existingEntity.getIngredients();

        Map<UUID, ProductItemIngredient> updatedMap = updatedIngredients.stream()
                .collect(Collectors.toMap(pii -> pii.getIngredient().getUuid(), Function.identity()));


        Iterator<ProductItemIngredientEntity> iterator = currentIngredients.iterator();
        while (iterator.hasNext()) {
            ProductItemIngredientEntity existingIngredient = iterator.next();
            UUID ingredientUuid = existingIngredient.getIngredient().getUuid();

            if (updatedMap.containsKey(ingredientUuid)) {
                existingIngredient.setQuantity(updatedMap.get(ingredientUuid).getQuantity());
                updatedMap.remove(ingredientUuid);
            } else {
                iterator.remove();
            }
        }
        for (ProductItemIngredient newIngredient : updatedMap.values()) {

            IngredientEntity ingredient = ingredientEntityRepository.findById(newIngredient.getIngredient().getUuid())
                    .orElseThrow(() -> new IngredientDoesNotExistException(newIngredient.getIngredient().getUuid().toString()));

            ProductItemIngredientEntity newEntity = ProductItemIngredientEntity.builder()
                    .ingredient(ingredient)
                    .quantity(newIngredient.getQuantity()).build();

            existingEntity.addProductItemIngredient(newEntity);
        }
    }


    @Override
    public boolean existsByName(String name, UUID restaurantUuid) {
        return productItemEntityRepository.existsByName(name, restaurantUuid);
    }

}
