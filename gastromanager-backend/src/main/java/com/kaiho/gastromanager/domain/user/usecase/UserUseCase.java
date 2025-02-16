package com.kaiho.gastromanager.domain.user.usecase;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.user.api.UserServicePort;
import com.kaiho.gastromanager.domain.user.exception.UserDoesNotExistException;
import com.kaiho.gastromanager.domain.user.exception.UsernameDoesNotExistException;
import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.domain.user.spi.UserPersistencePort;
import com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserUseCase implements UserServicePort {

    private final UserPersistencePort userPersistencePort;

    @Override
    public User getUserByUuid(UUID uuid) {
        return userPersistencePort.getUserByUuid(uuid)
                .orElseThrow(() -> new UsernameDoesNotExistException(uuid));
    }

    @Override
    @Transactional
    public User createUser(User user) {
        validateUserCreation(user);
        return userPersistencePort.createUser(user);
    }

    @Override
    public boolean hasAccessToRestaurant(User user, Restaurant restaurant) {
        return isEmployeeForRestaurant(user, restaurant) || hasOwnership(user, restaurant);
    }

    @Override
    public boolean isUserAlreadyRegistered(User user) {
        return userPersistencePort.isUserAlreadyRegistered(user);
    }

    @Override
    public User updateUser(User user) {
        User existingUser = userPersistencePort.getUserByUuid(user.getUuid()).orElseThrow(() -> new UserDoesNotExistException(user.getUuid()));

        if (!existingUser.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("No puedes cambiar el nombre de usuario.");
        }
        if (!existingUser.getContact().getEmail().equals(user.getContact().getEmail())) {
            throw new IllegalArgumentException("No puedes cambiar el correo electrónico.");
        }
        if (RestaurantContext.getCurrentRestaurant() != null) {
            // Validaciones para cuando este logueado
        }

        return userPersistencePort.updateUser(user);
    }

    @Override
    public Optional<User> getUserByUuidOrEmail(String username, String email) {
        return userPersistencePort.findUserByUsernameOrEmail(username, email);
    }

    private boolean hasOwnership(User user, Restaurant restaurant) {
        return restaurant.getOwnerUuid().equals(user.getUuid());
    }

    private boolean isEmployeeForRestaurant(User user, Restaurant restaurant) {
        Restaurant userRestaurant = user.getRestaurant();
        return userRestaurant != null && userRestaurant.getUuid().equals(restaurant.getUuid());
    }

    private void validateUserCreation(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        validateRoleHierarchyCreation(principal.getRoles(), user);

        validateUniqueFields(user);
    }

    private void validateUniqueFields(User user) {
        if (userPersistencePort.usernameExists(user.getUsername())) {
            throw new ValidationException("Username is already taken.");
        }
        if (userPersistencePort.emailExists(user.getContact().getEmail())) {
            throw new ValidationException("Email is already taken.");
        }
        if (userPersistencePort.phoneExists(user.getContact().getPhone())) {
            throw new ValidationException("Phone number is already taken.");
        }
    }

    private void validateRoleHierarchyCreation(Set<Role> principalRoles, User userToCreate) {
        final String OWNER_ROLE_NAME = "ROLE_OWNER";
        final String SUPERUSER_ROLE_NAME = "ROLE_SUPERUSER";
        final String ADMIN_ROLE_NAME = "ROLE_MANAGER";
        if (principalRoles.stream().anyMatch(role -> role.roleType().name().equals(OWNER_ROLE_NAME))) {
            if (userToCreate.getRoles().stream().anyMatch(role -> role.roleType().name().equals(SUPERUSER_ROLE_NAME))) {
                throw new ValidationException("An owner cannot create a superuser.");
            }
            if (userToCreate.getRoles().stream().anyMatch(role -> role.roleType().name().equals(OWNER_ROLE_NAME))) {
                throw new ValidationException("An owner cannot create another owner.");
            }
        }
        if (principalRoles.stream().anyMatch(role -> role.roleType().name().equals(ADMIN_ROLE_NAME))) {
            if (userToCreate.getRoles().stream().anyMatch(role -> role.roleType().name().equals(SUPERUSER_ROLE_NAME))) {
                throw new ValidationException("An admin cannot create a superuser.");
            }
            if (userToCreate.getRoles().stream().anyMatch(role -> role.roleType().name().equals(OWNER_ROLE_NAME))) {
                throw new ValidationException("An admin cannot create an owner.");
            }
            if (userToCreate.getRoles().stream().anyMatch(role -> role.roleType().name().equals(ADMIN_ROLE_NAME))) {
                throw new ValidationException("An admin cannot create another admin.");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userPersistencePort.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " does not exist."));
    }
}
