package com.kaiho.gastromanager.infrastructure.config.db;

import com.kaiho.gastromanager.domain.auth.model.Contact;
import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import com.kaiho.gastromanager.domain.productitem.model.Category;
import com.kaiho.gastromanager.domain.restaurant.spi.RestaurantPersistencePort;
import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.domain.user.model.RoleType;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.domain.user.spi.RolePersistencePort;
import com.kaiho.gastromanager.domain.user.spi.UserPersistencePort;
import com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.entity.CashRegisterEntity;
import com.kaiho.gastromanager.infrastructure.cashregister.output.jpa.repository.CashRegisterEntityRepository;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository.IngredientEntityRepository;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.entity.MenuEntity;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.repository.MenuEntityRepository;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.repository.ProductItemRepository;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.entity.RestaurantEntity;
import com.kaiho.gastromanager.infrastructure.restaurant.output.jpa.repository.RestaurantEntityRepository;
import com.kaiho.gastromanager.infrastructure.restaurantconfig.output.jpa.entity.RestaurantConfigEntity;
import com.kaiho.gastromanager.infrastructure.taxconfig.output.jpa.entity.TaxConfigEntity;
import com.kaiho.gastromanager.infrastructure.taxconfig.output.jpa.repository.TaxConfigEntityRepository;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static com.kaiho.gastromanager.domain.taxconfig.model.TaxType.IMPO_CONSUMO;
import static com.kaiho.gastromanager.domain.taxconfig.model.TaxType.IVA;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("default")
public class DBInit implements CommandLineRunner {

    private final UserEntityRepository userEntityRepository;
    private final IngredientEntityRepository ingredientEntityRepository;
    private final RestaurantEntityRepository restaurantEntityRepository;
    private final UserPersistencePort userPersistencePort;
    private final RolePersistencePort rolePersistencePort;
    private final PasswordEncoder passwordEncoder;
    private final ProductItemRepository productItemRepository;
    private final RestaurantPersistencePort restaurantPersistencePort;
    private final TaxConfigEntityRepository taxConfigEntityRepository;
    private final MenuEntityRepository menuEntityRepository;
    private final CashRegisterEntityRepository cashRegisterEntityRepository;

    @Override
    public void run(String... args) {
        createUserIfNotExists("cbarrios", "Carlos", "Barrios", "+16723381837", "cbarrios", Set.of(RoleType.ROLE_SUPERUSER));
        createUserIfNotExists("owner", "Owner", "User", "+16723381838", "owner", Set.of(RoleType.ROLE_OWNER));
        createUserIfNotExists("manager", "Manager", "User", "+16723381839", "manager", Set.of(RoleType.ROLE_MANAGER));
        createUserIfNotExists("waiter", "Waiter", "User", "+16723381840", "waiter", Set.of(RoleType.ROLE_WAITER));
        createUserIfNotExists("chef", "Chef", "User", "+16723381841", "chef", Set.of(RoleType.ROLE_CHEF));
        createUserIfNotExists("kitchenstaff", "Kitchen", "Staff", "+16723381842", "kitchenstaff", Set.of(RoleType.ROLE_KITCHEN_STAFF));
        createUserIfNotExists("cashier", "Cashier", "User", "+16723381843", "cashier", Set.of(RoleType.ROLE_CASHIER));
        createUserIfNotExists("owner2", "Owner2", "User2", "+16723381831", "owner2", Set.of(RoleType.ROLE_OWNER));
        createUserIfNotExists("manager2", "Manager2", "User2", "+16723381855", "manager2", Set.of(RoleType.ROLE_MANAGER));
        createUserIfNotExists("chef2", "Chef2", "User2", "+16723381844", "chef2", Set.of(RoleType.ROLE_CHEF));
        createUserIfNotExists("cashier2", "Cashier2", "User2", "+16723331843", "cashier2", Set.of(RoleType.ROLE_CASHIER));

        // Crear 2 restaurantes automáticamente
        createRestaurantIfNotExists("Crepes & Waffles", "Description for Crepes & Waffles rest", "Address for C&W", "owner", false, false);
        createRestaurantIfNotExists("KFC", "Description for KFC", "Address for KFC", "owner2", false, true);

        // Ingredientes para Crepes & Waffles (restaurante de comida variada)
        createIngredientIfNotExists("Harina de trigo", 10000, "GRAMS", "Distribuidora La Cosecha", BigDecimal.valueOf(4.5), "Crepes & Waffles", 2000);
        createIngredientIfNotExists("Huevos", 200, "UNITS", "Granja Santa Fe", BigDecimal.valueOf(500), "Crepes & Waffles", 50);
        createIngredientIfNotExists("Leche", 15000, "MILLILITRES", "Alpina", BigDecimal.valueOf(3.8), "Crepes & Waffles", 3000);
        createIngredientIfNotExists("Mantequilla", 2000, "GRAMS", "Alpina", BigDecimal.valueOf(28), "Crepes & Waffles", 500);
        createIngredientIfNotExists("Azúcar", 5000, "GRAMS", "Ingenio del Cauca", BigDecimal.valueOf(4.2), "Crepes & Waffles", 1000);

        // Ingredientes para KFC (restaurante de pollo frito)
        createIngredientIfNotExists("Pechuga de pollo", 15000, "GRAMS", "Pollos El Bucanero", BigDecimal.valueOf(18.5), "KFC", 3000);
        createIngredientIfNotExists("Harina de maíz", 8000, "GRAMS", "Molinos del Valle", BigDecimal.valueOf(5.2), "KFC", 2000);
        createIngredientIfNotExists("Papas", 20000, "GRAMS", "Finca La Esperanza", BigDecimal.valueOf(3.5), "KFC", 5000);
        createIngredientIfNotExists("Aceite vegetal", 10000, "MILLILITRES", "Aceites La Favorita", BigDecimal.valueOf(9.8), "KFC", 2000);
        createIngredientIfNotExists("Sal", 3000, "GRAMS", "Sal Marina del Pacífico", BigDecimal.valueOf(2.8), "KFC", 500);

        assignRestaurantToUser("Crepes & Waffles", "manager");
        assignRestaurantToUser("Crepes & Waffles", "waiter");
        assignRestaurantToUser("Crepes & Waffles", "chef");
        assignRestaurantToUser("Crepes & Waffles", "kitchenstaff");
        assignRestaurantToUser("Crepes & Waffles", "cashier");
        
        // Restaurante del owner2 (KFC) - payBeforeOrder = true
        assignRestaurantToUser("KFC", "manager2");
        assignRestaurantToUser("KFC", "chef2");
        assignRestaurantToUser("KFC", "cashier2");

        // Crear 3 cajas registradoras para cada restaurante
        createCashRegistersIfNotExists("Crepes & Waffles");
        createCashRegistersIfNotExists("KFC");

/*
        createMenusIfNotExists("Crepes & Waffles", "Main Menu", "Main menu for Crepes & Waffles");
        createMenusIfNotExists("Crepes & Waffles", "Breakfast", "Breakfast menu for Crepes & Waffles");
        createMenusIfNotExists("KFC", "Main Menu", "Main menu for KFC");
        createMenusIfNotExists("KFC", "Breakfast", "Breakfast menu for KFC");
*/

    }

    private void createMenusIfNotExists(String restaurantName, String name, String description) {
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findByName(restaurantName)
                                                                      .orElseThrow(() -> {
                                                                          log.error("Restaurant with name '{}' not found. Cannot create menu.", restaurantName);
                                                                          return new IllegalStateException("Restaurant not found.");
                                                                      });
        if (menuEntityRepository.existsByNameAndRestaurant(name, restaurantEntity)) {
            log.info("Menu '{}' already exists for restaurant '{}'.", name, restaurantName);
            return;
        }

        // Crear el menu
        MenuEntity menu = MenuEntity.builder()
                                    .name(name)
                                    .description(description)
                                    .isEnabled(true)
                                    .restaurant(restaurantEntity)
                                    .build();
        // Guardar el ingrediente
        restaurantEntity.addMenu(menu);
        menuEntityRepository.save(menu);
        log.info("Menu '{}' created successfully for restaurant '{}'.", name, restaurantName);
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

    private void createRestaurantIfNotExists(String name, String description, String address, String ownerUsername, boolean isFranchise, boolean payBeforeOrder) {
        UserEntity owner = userEntityRepository.findByUsername(ownerUsername).orElseThrow(() -> {
            log.error("Owner with username '{}' not found. Cannot create restaurant.", ownerUsername);
            return new IllegalStateException("Owner not found.");
        });

        if (restaurantPersistencePort.restaurantExistsByNameAndAddressAndOwnerUuid(name, address, owner.getUuid())) {
            log.info("Restaurant '{}' at address '{}' already exists for owner '{}'.", name, address, owner.getUsername());
            return;
        }

        // Paso 1: Crear la configuración del restaurante primero
        RestaurantConfigEntity configs = RestaurantConfigEntity.builder()
                                                               .payBeforeOrder(payBeforeOrder)
                                                               .isFranchise(isFranchise)
                                                               .enableDelivery(false)
                                                               .build();

        // Paso 2: Crear el restaurante con todos los campos requeridos
        RestaurantEntity restaurant = RestaurantEntity.builder()
                                                      .name(name)
                                                      .description(description)
                                                      .address(address)
                                                      .owner(owner)
                                                      .restaurantConfig(configs)
                                                      .build();

        // Establecer la relación bidireccional
        configs.setRestaurant(restaurant);

        // Paso 3: Obtener y asignar la configuración de impuestos
        TaxConfigEntity tax = isFranchise ?
                taxConfigEntityRepository.findByTaxType(IVA) :
                taxConfigEntityRepository.findByTaxType(IMPO_CONSUMO);

        restaurant.addTaxConfiguration(tax);

        // Paso 4: Guardar el restaurante (esto guardará también la configuración por cascada)
        restaurantEntityRepository.save(restaurant);
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
            Contact contact = Contact.builder().phone(phone)
                                     .email(name.toLowerCase() + "@example.com").build(); // Asignar un email único basado en el nombre.build()
            User user = User.builder()
                            .name(name)
                            .lastname(lastname)
                            .contact(contact)
                            .username(username)
                            .restaurant(null)
                            .verified(true)
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
                                             String supplier, BigDecimal pricePerUnit, String restaurantName,
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

    private void createCashRegistersIfNotExists(String restaurantName) {
        RestaurantEntity restaurantEntity = restaurantEntityRepository.findByName(restaurantName)
                                                                      .orElseThrow(() -> {
                                                                          log.error("Restaurant with name '{}' not found. Cannot create cash registers.", restaurantName);
                                                                          return new IllegalStateException("Restaurant not found.");
                                                                      });

        // Crear 3 cajas registradoras por defecto
        String[] cashRegisterNames = {"Caja Principal", "Caja Barra", "Caja Terraza"};
        String[] locations = {"Área Principal", "Área de Barra", "Terraza"};
        String[] descriptions = {
            "Caja registradora principal del restaurante",
            "Caja registradora del área de barra",
            "Caja registradora de la terraza"
        };

        for (int i = 0; i < 3; i++) {
            String registerName = cashRegisterNames[i];
            if (cashRegisterEntityRepository.existsByNameAndRestaurant(registerName, restaurantEntity)) {
                log.info("Cash register '{}' already exists for restaurant '{}'.", registerName, restaurantName);
                continue;
            }

            CashRegisterEntity cashRegister = CashRegisterEntity.builder()
                                                                .name(registerName)
                                                                .location(locations[i])
                                                                .description(descriptions[i])
                                                                .deviceId("DEV-" + restaurantName.replaceAll("\\s+", "").toUpperCase() + "-" + (i + 1))
                                                                .restaurant(restaurantEntity)
                                                                .build();

            cashRegisterEntityRepository.save(cashRegister);
            log.info("Cash register '{}' created successfully for restaurant '{}' at location '{}'.", registerName, restaurantName, locations[i]);
        }
    }
}
