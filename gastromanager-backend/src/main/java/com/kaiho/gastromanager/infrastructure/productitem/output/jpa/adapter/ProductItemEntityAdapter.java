package com.kaiho.gastromanager.infrastructure.productitem.output.jpa.adapter;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.productitem.exception.ProductItemDoesNotExistException;
import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import com.kaiho.gastromanager.domain.productitem.spi.ProductItemPersistencePort;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.mapper.IngredientEntityMapper;
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

    private final ProductItemRepository productItemRepository;
    private final ProductItemEntityMapper productItemEntityMapper;
    private final ProductItemIngredientEntityMapper productItemIngredientEntityMapper;
    private final IngredientServicePort ingredientServicePort;
    private final IngredientEntityMapper ingredientEntityMapper;

    @Override
    public List<ProductItem> findAllProductItems() {
        return productItemRepository.findAll().stream()
                .map(productItemEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ProductItem> findProductItemByUuid(UUID uuid) {
        return productItemRepository.findById(uuid)
                .map(productItemEntityMapper::toDomain);
    }

    @Override
    public ProductItem saveProductItem(ProductItem productItem) {

        //TODO: Revisar y reformular esta logica, aqui solo se debe mapear a entidades y operar base de datos
        List<IngredientEntity> dbIngredientEntityList = productItem.ingredients().stream()
                .map(productItemIngredient -> ingredientEntityMapper.toEntity(ingredientServicePort.getIngredientById(productItemIngredient.ingredientUuid()))).toList();

        ProductItemEntity productItemEntity = productItemEntityMapper.toEntity(productItem);

        List<ProductItemIngredientEntity> productItemIngredientEntityList = productItem.ingredients().stream()
                .map(productItemIngredient -> {
                    IngredientEntity ingredientEntity = dbIngredientEntityList.stream()
                            .filter(ing -> ing.getUuid().equals(productItemIngredient.ingredientUuid()))
                            .findFirst()
                            .get();
                    return productItemIngredientEntityMapper.toEntity(productItemIngredient, ingredientEntity);
                }).toList();

        productItemIngredientEntityList.forEach(productItemEntity::addIngredient);

        ProductItemEntity savedEntity = productItemRepository.save(productItemEntity);
        return productItemEntityMapper.toDomain(savedEntity);
    }

    @Override
    public ProductItem updateProductItem(UUID uuid, ProductItem updatedProductItem) {

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
                existingIngredient.setUnit(newIngredient.unit());
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

    @Override
    public boolean existsByName(String name) {
        return productItemRepository.existsByName(name);
    }

    @Override
    public List<ProductItem> findAllProductItemsByUuid(List<UUID> uuids) {
        return productItemRepository.findByUuidIn(uuids).stream().map(productItemEntityMapper::toDomain).toList();
    }

}
