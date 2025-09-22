package com.kaiho.gastromanager.domain.submenu.domain;

import com.kaiho.gastromanager.domain.menu.model.Menu;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
public class Submenu {
    private UUID uuid;
    private String name;
    private String description;
    private boolean isEnabled;
    private Menu menu;
    private Restaurant restaurant;
    private String createdBy;
    private String updatedBy;
    private Instant createdDate;
    private Instant updatedDate;
}
