package com.kaiho.gastromanager.domain.user.spi;

import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.domain.user.model.RoleType;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RolePersistencePort {
    Set<Role> findAllRolesByUuid(Set<UUID> roleUuids);

    Optional<Role> findByName(RoleType roleName);
}
