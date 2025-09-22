package com.kaiho.gastromanager.domain.restaurant.model;

import com.kaiho.gastromanager.domain.taxconfig.model.TaxConfig;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Builder
@Setter
@Getter
public class Restaurant {

    private UUID uuid;
    private String name;
    private String description;
    private UUID ownerUuid;
    private String address;
    private RestaurantConfig config;
    private List<TaxConfig> taxes;
}
