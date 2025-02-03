package com.kaiho.gastromanager.domain.auth.spi;

import com.kaiho.gastromanager.domain.auth.model.VerificationToken;
import com.kaiho.gastromanager.domain.user.model.User;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenPersistencePort {
    boolean tokenExists(UUID token);

    void createVerificationToken(VerificationToken verificationToken);

    Optional<VerificationToken> findByToken(UUID token);

    void deleteToken(VerificationToken uuid);

    boolean isVerificationPending(User user);
}
