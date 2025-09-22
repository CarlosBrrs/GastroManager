package com.kaiho.gastromanager.application.restaurant.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class UserRestaurantAccessResponseDto {
    private UUID uuid;
    private String name;
    private String description;
    private String address;
    private String accessType;
}
