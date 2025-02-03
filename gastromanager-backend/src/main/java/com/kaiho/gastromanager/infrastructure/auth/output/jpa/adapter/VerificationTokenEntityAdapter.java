package com.kaiho.gastromanager.infrastructure.auth.output.jpa.adapter;

import com.kaiho.gastromanager.domain.auth.exception.VerificationTokenDoesNotExistException;
import com.kaiho.gastromanager.domain.auth.model.VerificationToken;
import com.kaiho.gastromanager.domain.auth.spi.VerificationTokenPersistencePort;
import com.kaiho.gastromanager.domain.user.exception.UserDoesNotExistException;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.infrastructure.auth.output.jpa.entity.VerificationTokenEntity;
import com.kaiho.gastromanager.infrastructure.auth.output.jpa.mapper.VerificationTokenEntityMapper;
import com.kaiho.gastromanager.infrastructure.auth.output.jpa.repository.VerificationTokenEntityRepository;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VerificationTokenEntityAdapter implements VerificationTokenPersistencePort {

    private final VerificationTokenEntityMapper verificationTokenEntityMapper;
    private final VerificationTokenEntityRepository verificationTokenEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Override
    public boolean tokenExists(UUID token) {
        return verificationTokenEntityRepository.existsByToken(token);
    }

    @Override
    public void createVerificationToken(VerificationToken verificationToken) {
        VerificationTokenEntity verificationTokenEntity = verificationTokenEntityMapper.toEntity(verificationToken);
        UserEntity userEntity = userEntityRepository.findById(verificationToken.getUser().getUuid()).orElseThrow(() -> new UserDoesNotExistException(verificationToken.getUser().getUuid()));
        userEntity.assignVerificationToken(verificationTokenEntity);
        verificationTokenEntityMapper.toDomain(verificationTokenEntityRepository.save(verificationTokenEntity));

    }

    @Override
    public Optional<VerificationToken> findByToken(UUID token) {
        return verificationTokenEntityRepository.findByToken(token).map(verificationTokenEntityMapper::toDomain);
    }

    @Override
    public void deleteToken(VerificationToken verificationToken) {
        VerificationTokenEntity verificationTokenEntity = verificationTokenEntityRepository.findById(verificationToken.getUuid()).orElseThrow(() -> new VerificationTokenDoesNotExistException(verificationToken.getToken().toString()));
        verificationTokenEntity.getUser().removeVerificationToken();
        verificationTokenEntityRepository.deleteById(verificationTokenEntity.getUuid());
    }

    @Override
    public boolean isVerificationPending(User user) {
        // Buscar el token de verificación asociado al usuario
        Optional<VerificationTokenEntity> tokenEntityOptional = verificationTokenEntityRepository.findByUser(userEntityRepository.findByUsername(user.getUsername()).orElseThrow(() -> new UserDoesNotExistException(user.getUuid())));

        // Si el token está presente y no ha expirado, consideramos que la verificación está pendiente
        return tokenEntityOptional.map(token -> !token.getExpiryDate().isBefore(Instant.now())).orElse(false);
    }


}
