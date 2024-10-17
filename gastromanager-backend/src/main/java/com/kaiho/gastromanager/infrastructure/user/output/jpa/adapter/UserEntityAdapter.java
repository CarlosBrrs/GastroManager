package com.kaiho.gastromanager.infrastructure.user.output.jpa.adapter;

import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.domain.user.spi.UserPersistencePort;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.mapper.UserEntityMapper;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserEntityAdapter implements UserPersistencePort {

    private final UserEntityRepository userEntityRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public User createUser(User user) {
        UserEntity userToSave = userEntityMapper.toEntity(user);
        UserEntity saved = userEntityRepository.save(userToSave);
        return userEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        Optional<UserEntity> entityOptional = userEntityRepository.findByUsername(username);
        return entityOptional.map(userEntityMapper::toDomain);
    }

}
