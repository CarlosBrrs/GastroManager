package com.kaiho.gastromanager.domain.productitem.usecase;

import com.kaiho.gastromanager.domain.ingredient.api.IngredientServicePort;
import com.kaiho.gastromanager.domain.productitem.api.ProductItemServicePort;
import com.kaiho.gastromanager.domain.productitem.exception.ProductItemAlreadyExistsException;
import com.kaiho.gastromanager.domain.productitem.exception.ProductItemDoesNotExistException;
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
    public ProductItem getProductItemByUUID(UUID uuid, UUID restaurantUuid) {
        return productItemPersistencePort.findProductItemByUuid(uuid, restaurantUuid)
                                         .orElseThrow(() -> new ProductItemDoesNotExistException(uuid));
    }

    @Override
    @Transactional
    public UUID addProductItem(ProductItem productItem) {
        if (productItemPersistencePort.existsByName(productItem.getName(), productItem.getRestaurant().getUuid())) {
            throw new ProductItemAlreadyExistsException(productItem.getName());
        }
        validateIngredients(productItem.getIngredients());

        return productItemPersistencePort.saveProductItem(productItem);
    }

    @Override
    @Transactional
    public ProductItem updateProductItem(UUID uuid, ProductItem productItem) {
        Optional<ProductItem> optionalProductItem = productItemPersistencePort.findProductItemByUuid(uuid, productItem.getRestaurant().getUuid());
        if (optionalProductItem.isEmpty()) {
            throw new ProductItemDoesNotExistException(uuid);
        } else if (!optionalProductItem.get().getName().equals(productItem.getName()) &&
                productItemPersistencePort.existsByName(productItem.getName(), productItem.getRestaurant().getUuid())) {
            throw new ProductItemAlreadyExistsException(productItem.getName());
        }
        return productItemPersistencePort.updateProductItem(uuid, productItem);
    }


    private void validateIngredients(List<ProductItemIngredient> ingredients) {

        if (ingredients == null || ingredients.isEmpty()) {
            throw new ProductItemIngredientEmptyException();
        }
        ingredients.forEach(ingredient ->
                ingredientServicePort.getIngredientById(ingredient.getIngredient().getUuid())
        );
    }
}
