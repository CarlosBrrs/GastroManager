package com.kaiho.gastromanager.infrastructure.user.output.jpa.adapter;

import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.domain.user.spi.UserPersistencePort;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.mapper.UserEntityMapper;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class UserEntityAdapter implements UserPersistencePort {

    private final UserEntityRepository userEntityRepository;
    private final UserEntityMapper userEntityMapper;
    private final RestaurantEntityRepository restaurantEntityRepository;

    @Override
    public User createUser(User user) {
        UserEntity userToSave = userEntityMapper.toEntity(user);
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(user.restaurant().getUuid()).orElseThrow();

        restaurantEntity.addEmployee(userToSave);
        UserEntity saved = userEntityRepository.save(userToSave);
        return userEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        Optional<UserEntity> entityOptional = userEntityRepository.findByUsername(username);
        return entityOptional.map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> getUserByUuid(UUID uuid) {
        Optional<UserEntity> userEntityOptional = userEntityRepository.findById(uuid);
        return userEntityOptional.map(userEntityMapper::toDomain);
    }

    @Override
    public boolean usernameExists(String username) {
        return userEntityRepository.existsByUsername(username);
    }

    @Override
    public boolean emailExists(String email) {
        return userEntityRepository.existsByEmail(email);
    }

    @Override
    public boolean phoneExists(String phone) {
        return userEntityRepository.existsByPhone(phone);
    }
/*
    @Override
    public Optional<Restaurant> getRestaurantByUserUuid(UUID userUuid) {
        return null;
    }*/

}
