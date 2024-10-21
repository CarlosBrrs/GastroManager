package com.kaiho.gastromanager.domain.productitem.spi;

import com.kaiho.gastromanager.domain.productitem.model.ProductItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductItemPersistencePort {
    List<ProductItem> findAllProductItems();

    Optional<ProductItem> findProductItemByUuid(UUID uuid);

    ProductItem saveProductItem(ProductItem productItem);

    ProductItem updateProductItem(UUID uuid, ProductItem updatedProductItem);

    boolean existsByName(String name);
}
