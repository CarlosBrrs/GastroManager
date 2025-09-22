package com.kaiho.gastromanager.domain.product.usecase;

import com.kaiho.gastromanager.application.product.dto.response.ProductGroupByResponseDto;
import com.kaiho.gastromanager.domain.product.api.ProductServicePort;
import com.kaiho.gastromanager.domain.product.exception.ProductAlreadyExistsException;
import com.kaiho.gastromanager.domain.product.model.Product;
import com.kaiho.gastromanager.domain.product.model.ProductGroupByResult;
import com.kaiho.gastromanager.domain.product.spi.ProductPersistencePort;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductGroupByCriteria;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.criteria.ProductSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
public class ProductUseCase implements ProductServicePort {

    private final ProductPersistencePort productPersistencePort;

    @Override
    public UUID createProduct(Product product) {
        if (productPersistencePort.productExistsByName(product.getName(), product.getRestaurant().getUuid())) {
            throw new ProductAlreadyExistsException(product.getName());
        }
        return productPersistencePort.createProduct(product);
    }

    @Override
    public Page<Product> getAllProducts(ProductSearchCriteria criteria) {
        Sort sort = Sort.by(
                criteria.sortDirection().equalsIgnoreCase("desc") ? DESC : ASC,
                criteria.sortBy()
        );
        Pageable pageable = PageRequest.of(criteria.page(), criteria.size(), sort);
        return productPersistencePort.getAllProducts(criteria, pageable);
    }

    @Override
    public List<Product> findAllByUuidInAndRestaurant(List<UUID> productUuids, UUID restaurantUuid) {
        return productPersistencePort.findAllByUuidInAndRestaurant(productUuids, restaurantUuid);
    }

    @Override
    public ProductGroupByResult getProductsGroupedByCategory(ProductGroupByCriteria criteria) {
        UUID restaurantUuid = getCurrentRestaurant();
        return productPersistencePort.getProductsGroupedByCategory(criteria, restaurantUuid);
    }
}
