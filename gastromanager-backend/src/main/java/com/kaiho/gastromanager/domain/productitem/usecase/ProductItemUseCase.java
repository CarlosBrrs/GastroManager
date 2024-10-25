package com.kaiho.gastromanager.domain.productitem.usecase;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.ingredient.model.Ingredient;
import com.kaiho.gastromanager.domain.productitem.api.ProductItemServicePort;
import com.kaiho.gastromanager.domain.productitem.exception.ProductItemAlreadyExistsException;
import com.kaiho.gastromanager.domain.productitem.exception.ProductItemDoesNotExistException;
import com.kaiho.gastromanager.domain.productitem.exception.UnitConflictException;
import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import com.kaiho.gastromanager.domain.productitem.spi.ProductItemPersistencePort;
import com.kaiho.gastromanager.domain.productitemingredient.exception.ProductItemIngredientEmptyException;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductItemUseCase implements ProductItemServicePort {

    private final ProductItemPersistencePort productItemPersistencePort;
    private final IngredientServicePort ingredientServicePort;

    @Override
    @Transactional(readOnly = true)
    public List<ProductItem> getAllProductItems() {
        return productItemPersistencePort.findAllProductItems();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductItem getProductItemByUUID(UUID uuid) {
        return productItemPersistencePort.findProductItemByUuid(uuid)
                .orElseThrow(() -> new ProductItemDoesNotExistException(uuid));
    }

    @Override
    @Transactional
    public UUID addProductItem(ProductItem productItem) {
        if (productItemPersistencePort.existsByName(productItem.name())) {
            throw new ProductItemAlreadyExistsException(productItem.name());
        }
        // TODO: Move this to adapter, here we validate business rules and in adapter we retrieve and map to entity
        ProductItem toSave = ProductItem.builder()
                .name(productItem.name())
                .description(productItem.description())
                .category(productItem.category())
                .price(productItem.price())
                .isEnabled(productItem.isEnabled())
                .ingredients(validateAndRetrieveIngredients(productItem.ingredients()))
                .build();
        return productItemPersistencePort.saveProductItem(toSave).uuid();
    }

    @Override
    @Transactional
    public ProductItem updateProductItem(UUID uuid, ProductItem productItem) {
        Optional<ProductItem> optionalProductItem = productItemPersistencePort.findProductItemByUuid(uuid);
        if (optionalProductItem.isEmpty()) {
            throw new ProductItemDoesNotExistException(uuid);
        } else if (!optionalProductItem.get().name().equals(productItem.name()) &&
                productItemPersistencePort.existsByName(productItem.name())) {
            throw new ProductItemAlreadyExistsException(productItem.name());
        }
        return productItemPersistencePort.updateProductItem(uuid, productItem);
    }

    @Override
    public List<ProductItem> getAllProductItemsByUuid(List<UUID> uuids) {
        return productItemPersistencePort.findAllProductItemsByUuid(uuids);
    }

    private List<ProductItemIngredient> validateAndRetrieveIngredients(List<ProductItemIngredient> ingredients) {

        if (ingredients == null || ingredients.isEmpty()) {
            throw new ProductItemIngredientEmptyException();
        }
        return ingredients.stream()
                .map(ingredient -> {

                    Ingredient existingIngredient = ingredientServicePort.getIngredientById(ingredient.ingredientUuid());

                    if (existingIngredient.unit() != ingredient.unit()) {
                        throw new UnitConflictException(existingIngredient.name(), existingIngredient.unit());
                    }
                    return ProductItemIngredient.builder()
                            .ingredientUuid(existingIngredient.uuid())
                            .quantity(ingredient.quantity())
                            .unit(ingredient.unit())
                            .build();
                })
                .toList();
    }
}
