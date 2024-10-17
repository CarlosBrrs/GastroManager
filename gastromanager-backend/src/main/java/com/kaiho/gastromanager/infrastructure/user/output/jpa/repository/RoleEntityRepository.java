package com.kaiho.gastromanager.infrastructure.user.output.jpa.repository;

import com.kaiho.gastromanager.domain.user.model.RoleType;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RoleEntityRepository extends JpaRepository<RoleEntity, UUID> {
    Set<RoleEntity> findAllByUuidIn(Set<UUID> uuids);

    Optional<RoleEntity> findByRoleType(RoleType roleName);
}
