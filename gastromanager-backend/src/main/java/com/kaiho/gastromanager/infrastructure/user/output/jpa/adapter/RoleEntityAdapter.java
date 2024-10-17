package com.kaiho.gastromanager.infrastructure.user.output.jpa.adapter;

import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.domain.user.model.RoleType;
import com.kaiho.gastromanager.domain.user.spi.RolePersistencePort;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.RoleEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.mapper.RoleEntityMapper;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.RoleEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class RoleEntityAdapter implements RolePersistencePort {

    private final RoleEntityRepository roleEntityRepository;
    private final RoleEntityMapper roleEntityMapper;

    @Override
    public Set<Role> findAllRolesByUuid(Set<UUID> uuids) {
        return roleEntityRepository.findAllByUuidIn(uuids).stream()
                .map(roleEntityMapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Role> findByName(RoleType roleName) {
        Optional<RoleEntity> roleOptional = roleEntityRepository.findByRoleType(roleName);
        return roleOptional.map(roleEntityMapper::toDomain);
    }
}
