package com.kaiho.gastromanager.infrastructure.user.output.jpa.repository;

import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
}
