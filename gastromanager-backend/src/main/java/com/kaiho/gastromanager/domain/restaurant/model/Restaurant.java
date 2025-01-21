package com.kaiho.gastromanager.domain.restaurant.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Setter
@Getter
public class Restaurant {

    private UUID uuid;
    private String name;
    private UUID ownerUuid;
    private String address;
    private String description;
}
