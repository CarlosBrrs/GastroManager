package com.kaiho.gastromanager.infrastructure.user.output.jpa.adapter;

import com.kaiho.gastromanager.domain.restaurant.exception.RestaurantDoesNotExistException;
import com.kaiho.gastromanager.domain.user.exception.UserDoesNotExistException;
import com.kaiho.gastromanager.domain.user.model.RoleType;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.domain.user.spi.UserPersistencePort;
import com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.mapper.RoleEntityMapper;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.mapper.UserEntityMapper;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class UserEntityAdapter implements UserPersistencePort {

    private final UserEntityRepository userEntityRepository;
    private final UserEntityMapper userEntityMapper;
    private final RestaurantEntityRepository restaurantEntityRepository;
    private final RoleEntityMapper roleEntityMapper;

    @Override
    public User createUser(User user) {
        UserEntity userToSave = userEntityMapper.toEntity(user);
        if (user.getRoles().size() > 1 &&
                user.getRoles().stream()
                    .findFirst()
                    .stream()
                    .noneMatch(role -> role.roleType().equals(RoleType.ROLE_OWNER))) {

            RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(user.getRestaurant().getUuid()).orElseThrow();
            restaurantEntity.addEmployee(userToSave);

        }
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

    @Override
    public boolean isUserAlreadyRegistered(User user) {
        Optional<UserEntity> userEntityOptional = userEntityRepository.findByUsernameOrEmail(user.getUsername(), user.getContact().getEmail());

        return userEntityOptional.isPresent();
    }

    @Override
    public User updateUser(User updatedUser) {


        UserEntity existingUserEntity = userEntityRepository.findById(updatedUser.getUuid())
                                                            .orElseThrow(() -> new UserDoesNotExistException(updatedUser.getUuid()));

        existingUserEntity.setName(updatedUser.getName() != null ? updatedUser.getName() : existingUserEntity.getName());
        existingUserEntity.setLastname(updatedUser.getLastname() != null ? updatedUser.getLastname() : existingUserEntity.getLastname());
        existingUserEntity.setEmail(updatedUser.getContact() != null ? updatedUser.getContact().getEmail() : existingUserEntity.getEmail());
        existingUserEntity.setPhone(updatedUser.getContact() != null ? updatedUser.getContact().getPhone() : existingUserEntity.getPhone());
        existingUserEntity.setVerified(updatedUser.isVerified());

        if (RestaurantContext.getCurrentRestaurant() != null) { // todo si estoy logueado, es decir si estoy modificando roles de un usuario
            existingUserEntity.setRoles(updatedUser.getRoles().stream()
                                                   .map(roleEntityMapper::toEntity)
                                                   .collect(Collectors.toSet()));
        }

        if (updatedUser.getRestaurant() != null) {
            RestaurantEntity restaurantEntity = restaurantEntityRepository.findById(updatedUser.getRestaurant().getUuid())
                                                                          .orElseThrow(() -> new RestaurantDoesNotExistException(updatedUser.getRestaurant().getUuid().toString()));
            existingUserEntity.setRestaurant(restaurantEntity);
        }

        if (updatedUser.getRestaurants() != null) {
            List<RestaurantEntity> restaurantEntities = updatedUser.getRestaurants().stream()
                                                                   .map(r -> restaurantEntityRepository.findById(r.getUuid())
                                                                                                       .orElseThrow(() -> new RestaurantDoesNotExistException(r.getUuid().toString())))
                                                                   .collect(Collectors.toCollection(ArrayList::new));
            existingUserEntity.setRestaurants(restaurantEntities);
        }

        UserEntity savedUser = userEntityRepository.save(existingUserEntity);
        return userEntityMapper.toDomain(savedUser);
    }

    @Override
    public Optional<User> findUserByUsernameOrEmail(String username, String email) {
        return userEntityRepository.findByUsernameOrEmail(username, email).map(userEntityMapper::toDomain);
    }
}
