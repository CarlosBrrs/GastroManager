package com.kaiho.gastromanager.infrastructure.productitem.output.jpa.adapter;

import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import com.kaiho.gastromanager.domain.productitem.spi.ProductItemPersistencePort;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.mapper.ProductItemEntityMapper;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.repository.ProductItemRepository;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.entity.ProductItemIngredientEntity;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.mapper.ProductItemIngredientEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class ProductItemEntityAdapter implements ProductItemPersistencePort {

    private final ProductItemRepository productItemEntityRepository;
    private final ProductItemEntityMapper productItemEntityMapper;
    private final ProductItemIngredientEntityMapper productItemIngredientEntityMapper;

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
        return null;
    }
/*

    @Override
    public ProductItem updateProductItem(UUID uuid, ProductItem updatedProductItem, UUID restaurantUuid) {

        ProductItemEntity existingProductItem = productItemRepository.findById(uuid).orElseThrow(
                () -> new ProductItemDoesNotExistException(uuid)
        );

        existingProductItem.setName(updatedProductItem.name());
        existingProductItem.setDescription(updatedProductItem.description());
        existingProductItem.setPrice(updatedProductItem.price());
        existingProductItem.setCategory(updatedProductItem.category());

        List<ProductItemIngredientEntity> existingIngredients = existingProductItem.getIngredients();

        for (ProductItemIngredient newIngredient : updatedProductItem.ingredients()) {
            Optional<ProductItemIngredientEntity> existingIngredientOpt = existingIngredients.stream()
                    .filter(ing -> ing.getIngredient().getUuid().equals(newIngredient.ingredientUuid()))
                    .findFirst();

            if (existingIngredientOpt.isPresent()) {
                ProductItemIngredientEntity existingIngredient = existingIngredientOpt.get();
                existingIngredient.setQuantity(newIngredient.quantity());
            } else {
                Ingredient ingredient = ingredientServicePort.getIngredientById(newIngredient.ingredientUuid());

                IngredientEntity ingredientEntity = ingredientEntityMapper.toEntity(ingredient);
                ProductItemIngredientEntity newIngredientEntity = productItemIngredientEntityMapper.toEntity(newIngredient, ingredientEntity);
                existingProductItem.addIngredient(newIngredientEntity);
            }

        }

        existingIngredients.removeIf(existingIngredient ->
                updatedProductItem.ingredients().stream().noneMatch(newIng ->
                        newIng.ingredientUuid().equals(existingIngredient.getIngredient().getUuid()))
        );

        return productItemEntityMapper.toDomain(productItemRepository.save(existingProductItem));

    }
*/

    @Override
    public boolean existsByName(String name, UUID restaurantUuid) {
        return productItemEntityRepository.existsByName(name, restaurantUuid);
    }

}
