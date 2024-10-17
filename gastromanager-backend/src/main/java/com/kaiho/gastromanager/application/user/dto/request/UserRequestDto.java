package com.kaiho.gastromanager.application.user.dto.request;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record UserRequestDto(String name, String lastname, String phone, String email, String username,
                             String password, Set<UUID> roles) {

}
