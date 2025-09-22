package com.kaiho.gastromanager.application.menu.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MenuRequestDto(
        @NotBlank(message = "Name is required")
        String name,
        String description
) {
}
