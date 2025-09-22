package com.kaiho.gastromanager.application.recipe.dto.request;

import java.util.UUID;

public record BaseRecipeRequestDto(
        UUID uuid,
        double portion
) {
}
