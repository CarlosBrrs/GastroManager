package com.kaiho.gastromanager.domain.user.usecase;

import com.kaiho.gastromanager.domain.user.api.RoleServicePort;
import com.kaiho.gastromanager.domain.user.exception.RoleDoesNotExistExceptionException;
import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.domain.user.spi.RolePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleUseCase implements RoleServicePort {

    private final RolePersistencePort rolePersistencePort;

    @Override
    @Transactional(readOnly = true)
    public Set<Role> findRolesByUuids(Set<UUID> roleUuids) {
        if (roleUuids == null || roleUuids.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Role> allRolesByUuid = rolePersistencePort.findAllRolesByUuid(roleUuids);

        Set<UUID> foundUuids = allRolesByUuid.stream()
                .map(Role::uuid)
                .collect(Collectors.toSet());

        if (!foundUuids.containsAll(roleUuids)) {
            Set<UUID> missingUuids = new HashSet<>(roleUuids);
            missingUuids.removeAll(foundUuids);
            throw new RoleDoesNotExistExceptionException(missingUuids.toString());
        }
        return allRolesByUuid;
    }
}
