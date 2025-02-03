package com.kaiho.gastromanager.domain.auth.model;

import com.kaiho.gastromanager.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
public class VerificationToken {

    private final UUID uuid;
    private final UUID token;
    private final User user;
    private final Instant expiryDate;

    public boolean isExpired() {
        return expiryDate.isBefore(Instant.now());
    }
}
