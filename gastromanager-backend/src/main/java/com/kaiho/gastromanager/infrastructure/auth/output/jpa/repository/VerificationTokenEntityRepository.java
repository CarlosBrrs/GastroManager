package com.kaiho.gastromanager.infrastructure.auth.output.jpa.repository;

import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.infrastructure.auth.output.jpa.entity.VerificationTokenEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;


public interface VerificationTokenEntityRepository extends JpaRepository<VerificationTokenEntity, UUID> {

    @Query("SELECT COUNT(vt) > 0 FROM VerificationTokenEntity vt WHERE vt.token = :token")
    boolean existsByToken(UUID token);

    @Query("SELECT vt FROM VerificationTokenEntity vt WHERE vt.token = :token")
    Optional<VerificationTokenEntity> findByToken(UUID token);

    @Query("SELECT vt FROM VerificationTokenEntity vt WHERE vt.user = :user")
    Optional<VerificationTokenEntity> findByUser(UserEntity user);
}
