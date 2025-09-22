package com.kaiho.gastromanager.infrastructure.user.output.jpa.mapper;

import com.kaiho.gastromanager.domain.auth.model.Contact;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.mapper.RestaurantEntityMapper;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.RoleEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserEntityMapper {

    private final RestaurantEntityMapper restaurantEntityMapper;

    public User toDomain(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        Contact contact = Contact.builder()
                                 .phone(userEntity.getPhone())
                                 .email(userEntity.getEmail())
                                 .build();
        return User.builder()
                   .uuid(userEntity.getUuid())
                   .name(userEntity.getName())
                   .lastname(userEntity.getLastname())
                   .username(userEntity.getUsername())
                   .contact(contact)
                   .verified(userEntity.isVerified())
                   .encodedPassword(userEntity.getEncodedPassword())
                   .restaurant(restaurantEntityMapper.toDomain(userEntity.getRestaurant()))
                   .restaurants(userEntity.getRestaurants().stream()
                                          .map(restEntity -> Restaurant.builder()
                                                                       .uuid(restEntity.getUuid())
                                                                       .address(restEntity.getAddress())
                                                                       .name(restEntity.getName())
                                                                       .description(restEntity.getDescription())
                                                                       .ownerUuid(restEntity.getOwner().getUuid()).build())
                                          .toList())
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
        List<RestaurantEntity> restaurants = new ArrayList<>();
        if (user.getRestaurants() != null) {
            restaurants = user.getRestaurants().stream().map(restaurantEntityMapper::toEntity).toList();
        }

        return UserEntity.builder()
                         .name(user.getName())
                         .lastname(user.getLastname())
                         .phone(user.getContact().getPhone())
                         .email(user.getContact().getEmail())
                         .username(user.getUsername())
                         .verified(user.isVerified())
                         .restaurants(restaurants)
                         .encodedPassword(user.getPassword())
                         .roles(user.getRoles().stream()
                                    .map(role -> RoleEntity.builder()
                                                           .uuid(role.uuid())
                                                           .roleType(role.roleType())
                                                           .build())
                                    .collect(Collectors.toSet()))
                         .build();
    }

}
