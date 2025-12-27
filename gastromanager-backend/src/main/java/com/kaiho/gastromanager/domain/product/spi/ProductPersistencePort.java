package com.kaiho.gastromanager.domain.product.spi;

import com.kaiho.gastromanager.domain.product.model.Product;
import com.kaiho.gastromanager.domain.product.model.ProductGroupByResult;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductGroupByCriteria;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductPersistencePort {
    boolean productExistsByName(String name, UUID uuid);

    UUID createProduct(Product product);

    Page<Product> getAllProducts(ProductSearchCriteria criteria, Pageable pageable);

    List<Product> findAllByUuidInAndRestaurant(List<UUID> productUuids, UUID restaurantUuid);

    ProductGroupByResult getProductsGroupedByCategory(ProductGroupByCriteria criteria, UUID restaurantUuid);
}
