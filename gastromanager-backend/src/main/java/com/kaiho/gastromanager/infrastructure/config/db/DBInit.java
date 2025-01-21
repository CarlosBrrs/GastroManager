package com.kaiho.gastromanager.infrastructure.config.db;

import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import com.kaiho.gastromanager.domain.ingredient.spi.IngredientPersistencePort;
import com.kaiho.gastromanager.domain.productitem.model.Category;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.restaurant.spi.RestaurantPersistencePort;
import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.domain.user.model.RoleType;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.domain.user.spi.RolePersistencePort;
import com.kaiho.gastromanager.domain.user.spi.UserPersistencePort;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository.IngredientEntityRepository;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.repository.ProductItemRepository;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.mapper.RoleEntityMapper;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DBInit implements CommandLineRunner {
    private final UserEntityRepository userEntityRepository;
    private final IngredientEntityRepository ingredientEntityRepository;
    private final RestaurantEntityRepository restaurantEntityRepository;

    private final UserPersistencePort userPersistencePort;
    private final RolePersistencePort rolePersistencePort;
    private final PasswordEncoder passwordEncoder;
    private final ProductItemRepository productItemRepository;
    private final RestaurantPersistencePort restaurantPersistencePort;

    @Override
    public void run(String... args) throws Exception {
        createUserIfNotExists("cbarrios", "Carlos", "Barrios", "+16723381837", "cbarrios", Set.of(RoleType.ROLE_SUPERUSER));
        createUserIfNotExists("owner", "Owner", "User", "+16723381838", "owner", Set.of(RoleType.ROLE_OWNER));
        createUserIfNotExists("owner2", "Owner2", "User2", "+16723381831", "owner2", Set.of(RoleType.ROLE_OWNER));
        createUserIfNotExists("manager", "Manager", "User", "+16723381839", "manager", Set.of(RoleType.ROLE_MANAGER));
        createUserIfNotExists("manager2", "Manager2", "User2", "+16723381855", "manager2", Set.of(RoleType.ROLE_MANAGER));
        createUserIfNotExists("waiter", "Waiter", "User", "+16723381840", "waiter", Set.of(RoleType.ROLE_WAITER));
        createUserIfNotExists("chef", "Chef", "User", "+16723381841", "chef", Set.of(RoleType.ROLE_CHEF));
        createUserIfNotExists("chef2", "Chef2", "User2", "+16723381844", "chef2", Set.of(RoleType.ROLE_CHEF));
        createUserIfNotExists("kitchenstaff", "Kitchen", "Staff", "+16723381842", "kitchenstaff", Set.of(RoleType.ROLE_KITCHEN_STAFF));
        createUserIfNotExists("cashier", "Cashier", "User", "+16723381843", "cashier", Set.of(RoleType.ROLE_CASHIER));
        createUserIfNotExists("cashier2", "Cashier2", "User2", "+16723331843", "cashier2", Set.of(RoleType.ROLE_CASHIER));

        createRestaurantIfNotExists("Crepes & Waffles", "Description for Crepes & Waffles rest", "Address for C&W", "owner");
        createRestaurantIfNotExists("KFC", "Description for KFC", "Address for KFC", "owner2");

        createIngredientIfNotExists("All-Purpose Flour", 10000, "GRAMS", "random_supplier", 0.0025, "Crepes & Waffles", 500);
        createIngredientIfNotExists("Granulated Sugar", 5000, "GRAMS", "random_supplier", 0.0035, "Crepes & Waffles", 300);
        createIngredientIfNotExists("Salt", 5000, "GRAMS", "random_supplier", 0.0015, "Crepes & Waffles", 200);
        createIngredientIfNotExists("Butter", 1000, "GRAMS", "random_supplier", 0.015, "Crepes & Waffles", 100);
        createIngredientIfNotExists("Eggs", 700, "UNITS", "random_supplier", 0.25, "Crepes & Waffles", 50);
        createIngredientIfNotExists("Milk", 10000, "MILLILITRES", "random_supplier", 0.01, "Crepes & Waffles", 200);
        createIngredientIfNotExists("Yeast", 2000, "GRAMS", "random_supplier", 0.005, "Crepes & Waffles", 50);
        createIngredientIfNotExists("Vanilla Extract", 1000, "MILLILITRES", "random_supplier", 0.5, "Crepes & Waffles", 20);
        createIngredientIfNotExists("Chicken Breast", 10000, "GRAMS", "random_supplier", 0.025, "Crepes & Waffles", 200);
        createIngredientIfNotExists("Cream Cheese", 1000, "GRAMS", "random_supplier", 0.03, "Crepes & Waffles", 100);
        createIngredientIfNotExists("Ground Beef", 8000, "GRAMS", "random_supplier", 0.03, "Crepes & Waffles", 150);
        createIngredientIfNotExists("Lettuce", 1000, "GRAMS", "random_supplier", 0.005, "Crepes & Waffles", 100);
        createIngredientIfNotExists("Tomato", 2000, "GRAMS", "random_supplier", 0.01, "Crepes & Waffles", 50);
        createIngredientIfNotExists("Cheese", 3000, "GRAMS", "random_supplier", 0.03, "Crepes & Waffles", 80);
        createIngredientIfNotExists("Potatoes", 10000, "GRAMS", "random_supplier", 0.01, "Crepes & Waffles", 200);
        createIngredientIfNotExists("Tomato Sauce", 6000, "GRAMS", "random_supplier", 0.03, "Crepes & Waffles", 200);
        createIngredientIfNotExists("Pepperoni", 2000, "GRAMS", "random_supplier", 0.05, "Crepes & Waffles", 100);
        createIngredientIfNotExists("Hummus", 3000, "GRAMS", "random_supplier", 0.04, "Crepes & Waffles", 50);
        createIngredientIfNotExists("Chocolate", 1000, "GRAMS", "random_supplier", 0.02, "Crepes & Waffles", 100);
        createIngredientIfNotExists("Bun", 500, "UNITS", "random_supplier", 0.5, "Crepes & Waffles", 100);
        createIngredientIfNotExists("Pasta", 3000, "GRAMS", "random_supplier", 0.01, "Crepes & Waffles", 200);
        createIngredientIfNotExists("Oil", 5000, "MILLILITRES", "random_supplier", 0.005, "Crepes & Waffles", 500);
        createIngredientIfNotExists("Onion", 5000, "GRAMS", "random_supplier", 0.005, "Crepes & Waffles", 100);
        createIngredientIfNotExists("Garlic", 1000, "GRAMS", "random_supplier", 0.001, "Crepes & Waffles", 100);
        createIngredientIfNotExists("Olive Oil", 5000, "MILLILITRES", "random_supplier", 0.005, "Crepes & Waffles", 500);
        createIngredientIfNotExists("Cucumber", 1000, "GRAMS", "random_supplier", 0.01, "Crepes & Waffles", 100);
        createIngredientIfNotExists("Caesar Dressing", 1000, "MILLILITRES", "random_supplier", 0.02, "Crepes & Waffles", 150);
        createIngredientIfNotExists("Croutons", 3000, "GRAMS", "random_supplier", 0.03, "Crepes & Waffles", 200);
        createIngredientIfNotExists("Parmesan Cheese", 3000, "GRAMS", "random_supplier", 0.05, "Crepes & Waffles", 80);
        createIngredientIfNotExists("Chocolate Ice Cream", 5000, "GRAMS", "random_supplier", 0.05, "Crepes & Waffles", 150);
        createIngredientIfNotExists("Chocolate Syrup", 1000, "MILLILITRES", "random_supplier", 0.02, "Crepes & Waffles", 100);
        createIngredientIfNotExists("Chocolate Syrup KFC", 5000, "MILLILITRES", "random_supplier KFC", 0.02, "KFC", 100);

        assignRestaurantToUser("Crepes & Waffles", "manager");
        assignRestaurantToUser("Crepes & Waffles", "waiter");
        assignRestaurantToUser("Crepes & Waffles", "chef");
        assignRestaurantToUser("Crepes & Waffles", "kitchenstaff");
        assignRestaurantToUser("Crepes & Waffles", "cashier");
        assignRestaurantToUser("KFC", "manager2");
        assignRestaurantToUser("KFC", "chef2");
        assignRestaurantToUser("KFC", "cashier2");

    }

    private void assignRestaurantToUser(String restaurantName, String username) {
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findByName(restaurantName)
                .orElseThrow(() -> {
                    log.error("Restaurant '{}' not found.", restaurantName);
                    return new IllegalStateException("Restaurant not found.");
                });

        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User '{}' not found.", username);
                    return new IllegalStateException("User not found.");
                });

        restaurantEntity.addEmployee(userEntity);

        userEntityRepository.save(userEntity);
        log.info("User with username '{}' assigned successfully to restaurant '{}'.", username, restaurantName);
    }

    private void createRestaurantIfNotExists(String name, String description, String address, String ownerUsername) {
        User owner = userPersistencePort.findUserByUsername(ownerUsername).orElseThrow(() -> {
            log.error("Owner with username '{}' not found. Cannot create restaurant.", ownerUsername);
            return new IllegalStateException("Owner not found.");
        });
        if (restaurantPersistencePort.restaurantExistsByNameAndAddressAndOwnerUuid(name, address, owner.uuid())) {
            log.info("Restaurant '{}' at address '{}' already exists for owner '{}'.", name, address, owner.getUsername());
            return;
        }
        // Crear el restaurante
        Restaurant restaurant = Restaurant.builder()
                .name(name)
                .description(description)
                .address(address)
                .ownerUuid(owner.uuid()) // Asociar al propietario
                .build();

        // Guardar el restaurante en la base de datos
        restaurantPersistencePort.createRestaurant(restaurant);
        log.info("Restaurant '{}' created successfully for owner '{}' at address '{}'.", name, owner.getUsername(), address);
    }

    private void createUserIfNotExists(String username, String name, String lastname, String phone, String password, Set<RoleType> roles) {
        if (userPersistencePort.findUserByUsername(username).isEmpty()) {
            Role role = roles.stream()
                    .map(rolePersistencePort::findByName)
                    .map(Optional::get) // Using get() here safely since we will check for existence below
                    .findFirst()
                    .orElseThrow(() -> {
                        log.error("Critical error: A role does not exist. Please create it before running the application.");
                        return new IllegalStateException("Role not found.");
                    });

            User user = User.builder()
                    .name(name)
                    .lastname(lastname)
                    .phone(phone)
                    .email(name.toLowerCase() + "@example.com") // Asignar un email único basado en el nombre
                    .username(username)
                    .restaurant(null)
                    .restaurants(new ArrayList<>())
                    .encodedPassword(passwordEncoder.encode(password))
                    .roles(Set.of(role)) // Assign the found role
                    .build();

            userPersistencePort.createUser(user);
            log.info("User '" + username + "' created successfully with role: " + role.roleType().getAuthority());
        } else {
            log.info("User '" + username + "' already exists.");
        }
    }

    private void createIngredientIfNotExists(String name, int availableStock, String unit,
                                             String supplier, double pricePerUnit, String restaurantName,
                                             int minimumStockQuantity) {
        // Validar la existencia del restaurante
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findByName(restaurantName)
                .orElseThrow(() -> {
                    log.error("Restaurant with name '{}' not found. Cannot create ingredient.", restaurantName);
                    return new IllegalStateException("Restaurant not found.");
                });

        if (ingredientEntityRepository.existsByNameAndRestaurant(name, restaurantEntity)) {
            log.info("Ingredient '{}' already exists for restaurant '{}'.", name, restaurantName);
            return;
        }

        // Validar la unidad
        if (!Set.of("GRAMS", "UNITS", "MILLILITRES").contains(unit)) {
            log.error("Invalid unit '{}' for ingredient '{}'. Valid units are: GRAMS, UNITS, MILLILITRES.", unit, name);
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }

        // Crear el ingrediente
        IngredientEntity ingredient = IngredientEntity.builder()
                .name(name)
                .availableStock(availableStock)
                .unit(Unit.valueOf(unit))
                .supplier(supplier)
                .pricePerUnit(pricePerUnit)
                .restaurant(restaurantEntity)
                .minimumStockQuantity(minimumStockQuantity)
                .build();

        // Guardar el ingrediente
        ingredientEntityRepository.save(ingredient);
        log.info("Ingredient '{}' created successfully for restaurant '{}'.", name, restaurantName);
    }

    private ProductItemEntity createProduct(String name, String description, double price, Category category) {

        ProductItemEntity productItemEntity = ProductItemEntity.builder()
                .name(name)
                .description(description)
                .price(price)
                .category(category)
                .isEnabled(true)
                .build();

        log.info("product " + name + " created in the system");
        return productItemRepository.save(productItemEntity);
    }
}
