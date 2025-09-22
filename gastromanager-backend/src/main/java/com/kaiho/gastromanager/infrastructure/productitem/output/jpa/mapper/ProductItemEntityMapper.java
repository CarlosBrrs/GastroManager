package com.kaiho.gastromanager.infrastructure.productitem.output.jpa.mapper;

import com.kaiho.gastromanager.domain.productitem.model.ProductItem;
import com.kaiho.gastromanager.domain.productitemingredient.model.ProductItemIngredient;
import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.mapper.ProductItemIngredientEntityMapper;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.mapper.RestaurantEntityMapper;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductItemEntityMapper {

    private final RestaurantEntityMapper restaurantEntityMapper;
    private final RestaurantEntityRepository restaurantEntityRepository;
    private final ProductItemIngredientEntityMapper productItemIngredientEntityMapper;

    public ProductItem toDomain(ProductItemEntity productItemEntity) {
        if (productItemEntity == null) {
            return null;
        }

        Restaurant restaurant = restaurantEntityMapper.toDomain(productItemEntity.getRestaurant());

        List<ProductItemIngredient> ingredients = productItemEntity.getIngredients().stream()
                                                                   .map(productItemIngredientEntityMapper::toDomain)
                                                                   .toList();

        return ProductItem.builder()
                          .uuid(productItemEntity.getUuid())
                          .name(productItemEntity.getName())
                          .description(productItemEntity.getDescription())
                          .category(productItemEntity.getCategory())
                          .price(productItemEntity.getPrice())
                          .isEnabled(productItemEntity.isEnabled())
                          .restaurant(restaurant)
                          .ingredients(ingredients)
                          .createdBy(productItemEntity.getCreatedBy())
                          .createdDate(productItemEntity.getCreatedDate())
                          .updatedBy(productItemEntity.getUpdatedBy())
                          .updatedDate(productItemEntity.getUpdatedDate())
                          .build();
    }

    public ProductItemEntity toEntity(ProductItem productItem) {
        if (productItem == null) {
            return null;
        }

        RestaurantEntity restaurant = restaurantEntityRepository.findById(productItem.getRestaurant().getUuid())
                                                                .orElseThrow(() -> new RestaurantDoesNotExistException(productItem.getRestaurant().getUuid().toString()));

        return ProductItemEntity.builder()
                                .uuid(productItem.getUuid())
                                .name(productItem.getName())
                                .description(productItem.getDescription())
                                .category(productItem.getCategory())
                                .price(productItem.getPrice())
                                .ingredients(new ArrayList<>())
                                .isEnabled(productItem.isEnabled())
                                .createdBy(productItem.getCreatedBy())
                                .createdDate(productItem.getCreatedDate())
                                .restaurant(restaurant)
                                .build();

    }

    /*private ProductItemIngredient toProductItemIngredientDomain(ProductItemIngredientEntity ingredientEntity) {
        if (ingredientEntity == null) {
            return null;
        }

        return ProductItemIngredient.builder()
                .uuid(ingredientEntity.getUuid())
                .productItem(toProductItemDomain(ingredientEntity.getProductItem()))
                .ingredient(ingredientEntityMapper.toDomain(ingredientEntity.getIngredient()))
                .quantity(ingredientEntity.getQuantity())
                .build();
    }

    // Método auxiliar para mapear ProductItemEntity a ProductItem
    private ProductItem toProductItemDomain(ProductItemEntity productItemEntity) {
        if (productItemEntity == null) {
            return null;
        }

        return ProductItem.builder()
                .uuid(productItemEntity.getUuid())
                .name(productItemEntity.getName())
                .price(productItemEntity.getPrice())
                .build();
    }
*/
}
