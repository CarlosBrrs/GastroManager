package com.kaiho.gastromanager.domain.product.api;

import com.kaiho.gastromanager.domain.product.model.Product;
import com.kaiho.gastromanager.domain.product.model.ProductGroupByResult;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductGroupByCriteria;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductServicePort {
    UUID createProduct(Product product);

    Page<Product> getAllProducts(ProductSearchCriteria criteria);

    List<Product> findAllByUuidInAndRestaurant(List<UUID> productUuids, UUID restaurantUuid);

    ProductGroupByResult getProductsGroupedByCategory(ProductGroupByCriteria criteria);
}
