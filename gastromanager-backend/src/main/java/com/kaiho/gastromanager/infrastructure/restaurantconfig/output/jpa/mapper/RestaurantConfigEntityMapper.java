package com.kaiho.gastromanager.infrastructure.restaurantconfig.output.jpa.mapper;

import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;
import com.kaiho.gastromanager.infrastructure.restaurantconfig.output.jpa.entity.RestaurantConfigEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantConfigEntityMapper {

    public RestaurantConfigEntity toEntity(RestaurantConfig restaurantConfig) {
        if (restaurantConfig == null) {
            return null;
        }
        return RestaurantConfigEntity.builder()
                                     .payBeforeOrder(restaurantConfig.isPayBeforeOrder())
                                     .isFranchise(restaurantConfig.isFranchise())
                                     .build();
    }

    public RestaurantConfig toDomain(RestaurantConfigEntity restaurantConfigEntity) {
        if (restaurantConfigEntity == null) {
            return null;
        }
        return RestaurantConfig.builder()
                               .uuid(restaurantConfigEntity.getUuid())
                               .payBeforeOrder(restaurantConfigEntity.isPayBeforeOrder())
                               .isFranchise(restaurantConfigEntity.isFranchise())
                               .build();
    }
}
