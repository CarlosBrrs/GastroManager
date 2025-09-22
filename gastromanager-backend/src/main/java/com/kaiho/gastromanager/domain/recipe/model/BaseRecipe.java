package com.kaiho.gastromanager.domain.recipe.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BaseRecipe {
    private final Recipe recipe;
    private final Double portion;
}
