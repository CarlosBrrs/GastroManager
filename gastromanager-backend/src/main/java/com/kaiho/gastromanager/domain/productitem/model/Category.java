package com.kaiho.gastromanager.domain.productitem.model;

import lombok.Getter;

@Getter
public enum Category {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    DRINK("Drink"),
    SIDE_DISH("Side dish"),
    FRIED_FOOD("Fried food"),
    FAST_FOOD("Fast food"),
    DESSERT("Dessert");

    private final String name;

    Category(String name) {
        this.name = name;
    }
}
