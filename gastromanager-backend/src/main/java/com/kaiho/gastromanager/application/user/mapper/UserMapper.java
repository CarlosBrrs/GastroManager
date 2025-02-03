package com.kaiho.gastromanager.application.user.mapper;

import com.kaiho.gastromanager.application.user.dto.request.UserRequestDto;
import com.kaiho.gastromanager.application.user.dto.response.RoleResponseDto;
import com.kaiho.gastromanager.application.user.dto.response.UserResponseDto;
import com.kaiho.gastromanager.domain.auth.model.Contact;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.user.api.RoleServicePort;
import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext.getCurrentRestaurant;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final RestaurantServicePort restaurantServicePort;
    private final RoleServicePort roleServicePort;

    public User toDomain(UserRequestDto userRequestDto) {

        if (userRequestDto == null) {
            return null;
        }
        Restaurant restaurant = null;
        Set<Role> roleSet;
        boolean isVerified = false;
        if (userRequestDto.roles().isEmpty()) {
            roleSet = Set.of(roleServicePort.findOwnerRole());

        } else {
            restaurant = restaurantServicePort.getRestaurantById(getCurrentRestaurant());
            roleSet = roleServicePort.findRolesByUuids(userRequestDto.roles());
            isVerified = true;
        }
        Contact contact = Contact.builder()
                .email(userRequestDto.email())
                .phone(userRequestDto.phone())
                .build();

        return User.builder()
                .name(userRequestDto.name())
                .lastname(userRequestDto.lastname())
                .contact(contact)
                .username(userRequestDto.username())
                .restaurant(restaurant)
                .encodedPassword(passwordEncoder.encode(userRequestDto.password()))
                .verified(isVerified)
                .roles(roleSet)
                .build();
    }

    public UserResponseDto toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseDto.builder()
                .uuid(user.getUuid())
                .name(user.getName())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .email(user.getContact().getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> RoleResponseDto.builder()
                                .uuid(role.uuid())
                                .name(role.roleType().name())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
