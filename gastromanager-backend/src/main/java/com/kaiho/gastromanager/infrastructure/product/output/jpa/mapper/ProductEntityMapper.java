package com.kaiho.gastromanager.infrastructure.product.output.jpa.mapper;

import com.kaiho.gastromanager.domain.product.model.Product;
import com.kaiho.gastromanager.infrastructure.product.output.jpa.entity.ProductEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class ProductEntityMapper {

    private final RestaurantEntityRepository restaurantEntityRepository;

    public ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(getCurrentRestaurant())
                                                                      .orElseThrow(() -> new IllegalArgumentException("Restaurant does not exist"));
        ProductEntity entity = ProductEntity.builder()
                                            .uuid(product.getUuid())
                                            .name(product.getName())
                                            .description(product.getDescription())
                                            .category(product.getCategory())
                                            .salePrice(product.getSalePrice())
                                            .purchasePrice(product.getPurchasePrice())
                                            .isEnabled(product.isEnabled())
                                            .restaurant(restaurantEntity)
                                            .build();
        restaurantEntity.addProduct(entity);
        return entity;
    }

    public Product toDomain(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }
        return Product.builder()
                      .uuid(productEntity.getUuid())
                      .name(productEntity.getName())
                      .description(productEntity.getDescription())
                      .category(productEntity.getCategory())
                      .salePrice(productEntity.getSalePrice())
                      .purchasePrice(productEntity.getPurchasePrice())
                      .isEnabled(productEntity.getIsEnabled())
                      .build();
    }
}
