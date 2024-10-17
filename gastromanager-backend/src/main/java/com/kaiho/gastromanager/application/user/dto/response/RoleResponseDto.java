package com.kaiho.gastromanager.application.user.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RoleResponseDto(UUID uuid, String name) {
}
