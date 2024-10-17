package com.kaiho.gastromanager.domain.user.api;

import com.kaiho.gastromanager.domain.user.model.Role;

import java.util.Set;
import java.util.UUID;

public interface RoleServicePort {
    Set<Role> findRolesByUuids(Set<UUID> roles);
}
