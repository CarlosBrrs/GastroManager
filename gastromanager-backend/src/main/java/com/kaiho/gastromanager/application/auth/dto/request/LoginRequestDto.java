package com.kaiho.gastromanager.application.auth.dto.request;

import lombok.Builder;

@Builder
public record LoginRequestDto(String username, String password) {
}
