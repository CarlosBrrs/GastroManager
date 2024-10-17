package com.kaiho.gastromanager.application.user.dto.response;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record UserResponseDto(UUID uuid, String name, String lastname, String email, String username,
                              Set<RoleResponseDto> roles) {

}