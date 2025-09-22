package com.kaiho.gastromanager.application.product.mapper;

import com.kaiho.gastromanager.application.product.dto.request.ProductRequestDto;
import com.kaiho.gastromanager.application.product.dto.response.ProductSummaryResponseDto;
import com.kaiho.gastromanager.domain.product.model.Product;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
public class ProductMapper {

    public Product toDomain(ProductRequestDto productRequestDto) {
        if (productRequestDto == null) {
            return null;
        }
        return Product.builder()
                      .name(productRequestDto.name())
                      .description(productRequestDto.description())
                      .category(productRequestDto.category())
                      .salePrice(BigDecimal.valueOf(productRequestDto.salePrice()))
                      .purchasePrice(BigDecimal.valueOf(productRequestDto.purchasePrice()))
                      .restaurant(Restaurant.builder().uuid(getCurrentRestaurant()).build())
                      .isEnabled(true)
                      .build();
    }

    public ProductSummaryResponseDto toResponseSummary(Product product) {
        if (product == null) {
            return null;
        }return ProductSummaryResponseDto.builder()
                .uuid(product.getUuid())
                .name(product.getName())
                .description(product.getDescription())
                .purchasePrice(product.getPurchasePrice().doubleValue())
                .salePrice(product.getSalePrice().doubleValue())
                .build();
    }
}
