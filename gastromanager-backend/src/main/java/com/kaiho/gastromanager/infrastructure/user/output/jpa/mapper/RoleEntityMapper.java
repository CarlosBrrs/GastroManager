package com.kaiho.gastromanager.infrastructure.user.output.jpa.mapper;

import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleEntityMapper {
    public Role toDomain(RoleEntity roleEntity) {
        if (roleEntity == null) {
            return null;
        }

        return Role.builder()
                .uuid(roleEntity.getUuid())
                .roleType(roleEntity.getRoleType())
                .build();
    }
}
