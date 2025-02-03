package com.kaiho.gastromanager.infrastructure.auth.output.jpa.mapper;

import com.kaiho.gastromanager.domain.auth.model.VerificationToken;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.infrastructure.auth.output.jpa.entity.VerificationTokenEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificationTokenEntityMapper {

    private final UserEntityMapper userEntityMapper;

    public VerificationTokenEntity toEntity(VerificationToken verificationToken) {
        if (verificationToken == null) {
            return null;
        }
        return VerificationTokenEntity.builder()
                .token(verificationToken.getToken())
                .expiryDate(verificationToken.getExpiryDate()).build();
    }

    public VerificationToken toDomain(VerificationTokenEntity verificationTokenEntity) {
        if (verificationTokenEntity == null) {
            return null;
        }
        User user = userEntityMapper.toDomain(verificationTokenEntity.getUser());
        return VerificationToken.builder()
                .uuid(verificationTokenEntity.getUuid())
                .user(user)
                .token(verificationTokenEntity.getToken())
                .expiryDate(verificationTokenEntity.getExpiryDate()).build();
    }
}
