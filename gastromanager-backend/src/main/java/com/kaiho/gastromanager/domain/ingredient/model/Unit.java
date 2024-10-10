package com.kaiho.gastromanager.domain.ingredient.model;

import lombok.Getter;

@Getter
public enum Unit {
    GRAMS("g"),
    UNITS("units"),
    MILLILITRES("mL");

    private final String symbol;

    Unit(String symbol) {
        this.symbol = symbol;
    }

}
