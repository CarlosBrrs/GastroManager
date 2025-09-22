package com.kaiho.gastromanager.domain.menu.model;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Menu {
    private final UUID uuid;
    private final String name;
    private final String description;
    private final boolean isEnabled;
    private final String createdBy;
    private final Instant createdDate;
    private final String updatedBy;
    private final Instant updatedDate;
    private Restaurant restaurant;
}
