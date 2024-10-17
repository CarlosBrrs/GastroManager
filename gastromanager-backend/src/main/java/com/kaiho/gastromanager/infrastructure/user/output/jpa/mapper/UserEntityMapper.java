package com.kaiho.gastromanager.infrastructure.user.output.jpa.mapper;

import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.RoleEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserEntityMapper {

    public User toDomain(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return User.builder()
                .uuid(userEntity.getUuid())
                .name(userEntity.getName())
                .lastname(userEntity.getLastname())
                .phone(userEntity.getPhone())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .encodedPassword(userEntity.getEncodedPassword())
                .roles(userEntity.getRoles().stream()
                        .map(roleEntity -> Role.builder()
                                .uuid(roleEntity.getUuid())
                                .roleType(roleEntity.getRoleType())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        return UserEntity.builder()
                .name(user.name())
                .lastname(user.lastname())
                .phone(user.phone())
                .email(user.email())
                .username(user.username())
                .encodedPassword(user.getPassword())
                .roles(user.roles().stream()
                        .map(role -> RoleEntity.builder()
                                .uuid(role.uuid())
                                .roleType(role.roleType())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }

}
