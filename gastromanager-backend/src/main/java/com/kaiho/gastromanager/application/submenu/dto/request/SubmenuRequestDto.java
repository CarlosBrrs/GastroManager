package com.kaiho.gastromanager.application.submenu.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record SubmenuRequestDto(String name, String description, UUID menuUuid) {
    public SubmenuRequestDto {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (menuUuid == null) {
            throw new IllegalArgumentException("Menu UUID cannot be null");
        }
    }
}
