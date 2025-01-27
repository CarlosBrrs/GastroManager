package com.kaiho.gastromanager.infrastructure.config.db;

import com.kaiho.gastromanager.domain.restaurant.spi.RestaurantPersistencePort;
import com.kaiho.gastromanager.domain.user.model.RoleType;
import com.kaiho.gastromanager.domain.user.spi.RolePersistencePort;
import com.kaiho.gastromanager.domain.user.spi.UserPersistencePort;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository.IngredientEntityRepository;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.repository.ProductItemRepository;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.RoleEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class DBDevInit implements CommandLineRunner {
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createUserIfNotExists();

    }

    private void createUserIfNotExists() {
        Optional<UserEntity> cbarrios = userEntityRepository.findByUsername("cbarrios");
        if (cbarrios.isPresent()) {
            cbarrios.get().setEncodedPassword(passwordEncoder.encode("cbarrios"));
            cbarrios.get().setRoles(Set.of(RoleEntity.builder()
                    .roleType(RoleType.ROLE_OWNER)
                    .uuid(UUID.randomUUID())
                    .build()));
            userEntityRepository.save(cbarrios.get());
        } else {
            log.error("Critical error: The user cbarrios does not exists. Please contact admin");
        }
    }

}
