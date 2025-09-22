package com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.mapper;

import com.kaiho.gastromanager.domain.restaurant.model.AccessType;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;
import com.kaiho.gastromanager.domain.restaurant.model.UserRestaurantAccess;
import com.kaiho.gastromanager.domain.taxconfig.model.TaxConfig;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurantconfig.output.jpa.mapper.RestaurantConfigEntityMapper;
import com.kaiho.gastromanager.infrastructure.taxconfig.output.jpa.mapper.TaxConfigEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestaurantEntityMapper {

    private final RestaurantConfigEntityMapper restaurantConfigEntityMapper;
    private final TaxConfigEntityMapper taxConfigEntityMapper;

    public Restaurant toDomain(RestaurantEntity restaurantEntity) {
        if (restaurantEntity == null) {
            return null;
        }
        List<TaxConfig> taxes = restaurantEntity.getTaxConfigs().stream()
                                                .map(taxConfigEntityMapper::toDomain)
                                                .toList();
        RestaurantConfig config = restaurantConfigEntityMapper.toDomain(restaurantEntity.getRestaurantConfig());
        return Restaurant.builder()
                         .uuid(restaurantEntity.getUuid())
                         .name(restaurantEntity.getName())
                         .description(restaurantEntity.getDescription())
                         .address(restaurantEntity.getAddress())
                         .ownerUuid(restaurantEntity.getOwner().getUuid())
                         .config(config)
                         .taxes(taxes)
                         .build();
    }

    public RestaurantEntity toEntity(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        // Map basic fields and create entity
        RestaurantEntity entity = RestaurantEntity.builder()
                               .name(restaurant.getName())
                               .description(restaurant.getDescription())
                               .address(restaurant.getAddress())
                               .build();
        entity.setConfigs(restaurantConfigEntityMapper.toEntity(restaurant.getConfig()));
        return entity;
    }

    public UserRestaurantAccess toUserRestaurantAccessFromEntity(RestaurantEntity restaurantEntity, AccessType accessType) {
        if (restaurantEntity == null) {
            return null;
        }
        return UserRestaurantAccess.builder()
                                   .uuid(restaurantEntity.getUuid())
                                   .name(restaurantEntity.getName())
                                   .description(restaurantEntity.getDescription())
                                   .address(restaurantEntity.getAddress())
                                   .accessType(accessType)
                                   .build();
    }
}
