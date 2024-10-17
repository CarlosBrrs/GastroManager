package com.kaiho.gastromanager.application.user.mapper;

import com.kaiho.gastromanager.application.user.dto.request.UserRequestDto;
import com.kaiho.gastromanager.application.user.dto.response.RoleResponseDto;
import com.kaiho.gastromanager.application.user.dto.response.UserResponseDto;
import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toDomain(UserRequestDto userRequestDto, Set<Role> roleSet) {

        if (userRequestDto == null) {
            return null;
        }
        return User.builder()
                .name(userRequestDto.name())
                .lastname(userRequestDto.lastname())
                .email(userRequestDto.email())
                .phone(userRequestDto.phone())
                .username(userRequestDto.username())
                .encodedPassword(passwordEncoder.encode(userRequestDto.password()))
                .roles(roleSet)
                .build();
    }

    public UserResponseDto toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseDto.builder()
                .uuid(user.uuid())
                .name(user.name())
                .lastname(user.lastname())
                .username(user.username())
                .email(user.email())
                .roles(user.roles().stream()
                        .map(role -> RoleResponseDto.builder()
                                .uuid(role.uuid())
                                .name(role.roleType().name())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
