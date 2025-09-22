package com.kaiho.gastromanager.domain.restaurant.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class UserRestaurantAccess {
    private UUID uuid;
    private String name;
    private String description;
    private String address;
    private AccessType accessType;
}
