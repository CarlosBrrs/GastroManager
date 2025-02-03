package com.kaiho.gastromanager.domain.auth.usecase;

import com.kaiho.gastromanager.domain.auth.api.VerificationTokenServicePort;
import com.kaiho.gastromanager.domain.auth.exception.TokenDoesNotExistException;
import com.kaiho.gastromanager.domain.auth.model.VerificationToken;
import com.kaiho.gastromanager.domain.auth.spi.VerificationTokenPersistencePort;
import com.kaiho.gastromanager.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class VerificationTokenUseCase implements VerificationTokenServicePort {

    private final VerificationTokenPersistencePort verificationTokenPersistencePort;

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenPersistencePort.findByToken(UUID.fromString(token))
                .orElseThrow(() -> new TokenDoesNotExistException(token));
    }

    @Override
    public void deleteToken(VerificationToken verificationToken) {
        verificationTokenPersistencePort.deleteToken(verificationToken);
    }

    private UUID generateValidVerificationToken() {
        UUID token;
        do {
            token = UUID.randomUUID();
        } while (verificationTokenPersistencePort.tokenExists(token));
        return token;
    }

    public void createVerificationToken(User user) {
        UUID token = this.generateValidVerificationToken();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plusSeconds(300))
                .build();
        verificationTokenPersistencePort.createVerificationToken(verificationToken);
    }

    @Override
    public boolean isVerificationPending(User user) {
        return verificationTokenPersistencePort.isVerificationPending(user);

    }
}
