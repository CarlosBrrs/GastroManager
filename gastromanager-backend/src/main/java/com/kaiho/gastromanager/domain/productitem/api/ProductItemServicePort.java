package com.kaiho.gastromanager.domain.productitem.api;

import com.kaiho.gastromanager.domain.productitem.model.ProductItem;

import java.util.List;
import java.util.UUID;

public interface ProductItemServicePort {
    List<ProductItem> getAllProductItems();

    ProductItem getProductItemByUUID(UUID uuid);

    UUID addProductItem(ProductItem productItem);

    ProductItem updateProductItem(UUID uuid, ProductItem productItem);

    List<ProductItem> getAllProductItemsByUuid(List<UUID> uuids);
}
