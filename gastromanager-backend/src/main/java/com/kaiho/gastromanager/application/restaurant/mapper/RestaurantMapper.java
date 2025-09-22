package com.kaiho.gastromanager.application.restaurant.mapper;

import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantCreateRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.request.RestaurantRequestDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantConfigResponseDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantDetailResponseDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.RestaurantResponseDto;
import com.kaiho.gastromanager.application.restaurant.dto.response.UserRestaurantAccessResponseDto;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.restaurant.model.RestaurantConfig;
import com.kaiho.gastromanager.domain.restaurant.model.UserRestaurantAccess;
import com.kaiho.gastromanager.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RestaurantMapper {

    private final RestaurantConfigMapper restaurantConfigMapper;

    public RestaurantResponseDto toResponse(Restaurant restaurant) {
        return null;
    }

    public Restaurant toDomain(RestaurantRequestDto restaurantRequestDto, UUID ownerUuid) {
        if (restaurantRequestDto == null) {
            return null;
        }
        return Restaurant.builder()
                         .name(restaurantRequestDto.name())
                         .address(restaurantRequestDto.address().street())
                         .description(restaurantRequestDto.description())
                         .ownerUuid(ownerUuid)
                         .build();
    }

    public Restaurant toDomain(RestaurantCreateRequestDto restaurantConfigRequestDto) {
        if (restaurantConfigRequestDto == null) {
            return null;
        }
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        RestaurantConfig configs = restaurantConfigMapper.toDomain(restaurantConfigRequestDto.configs());
        return Restaurant.builder()
                         .name(restaurantConfigRequestDto.name())
                         .address(restaurantConfigRequestDto.address().street())
                         .description(restaurantConfigRequestDto.description())
                         .ownerUuid(principal.getUuid())
                         .taxes(new ArrayList<>())
                         .config(configs)
                         .build();
    }

    public RestaurantConfigResponseDto toResponse(RestaurantConfig restaurantConfig) {
        if (restaurantConfig == null) {
            return null;
        }
        return RestaurantConfigResponseDto.builder()
                                          .uuid(restaurantConfig.getUuid())
                                          .requirePaymentBeforeOrder(restaurantConfig.isPayBeforeOrder())
                                          .build();
    }

    public UserRestaurantAccessResponseDto toUserRestaurantAccessResponse(UserRestaurantAccess userRestaurantAccess) {
        if (userRestaurantAccess == null) {
            return null;
        }
        return UserRestaurantAccessResponseDto.builder()
                                              .uuid(userRestaurantAccess.getUuid())
                                              .name(userRestaurantAccess.getName())
                                              .description(userRestaurantAccess.getDescription())
                                              .address(userRestaurantAccess.getAddress())
                                              .accessType(userRestaurantAccess.getAccessType().name())
                                              .build();
    }

    public RestaurantDetailResponseDto toRestaurantDetailResponse(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        return RestaurantDetailResponseDto.builder()
                                          .uuid(restaurant.getUuid())
                                          .name(restaurant.getName())
                                          .description(restaurant.getDescription())
                                          .address(restaurant.getAddress())
                                          .ownerUuid(restaurant.getOwnerUuid())
                                          .config(toResponse(restaurant.getConfig()))
                                          .build();
    }
}
