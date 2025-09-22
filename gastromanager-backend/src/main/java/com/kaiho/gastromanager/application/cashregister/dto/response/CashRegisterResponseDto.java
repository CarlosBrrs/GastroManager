package com.kaiho.gastromanager.application.cashregister.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CashRegisterResponseDto(
    UUID uuid,
    String name,
    String location,
    String status, // "OPEN" o "CLOSED" según tenga sesión activa
    CashRegisterCurrentSessionResponseDto currentSession
) {
}
