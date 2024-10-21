package com.kaiho.gastromanager.infrastructure.config.db;

import com.kaiho.gastromanager.domain.ingredient.model.Unit;
import com.kaiho.gastromanager.domain.productitem.model.Category;
import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.domain.user.model.RoleType;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.domain.user.spi.RolePersistencePort;
import com.kaiho.gastromanager.domain.user.spi.UserPersistencePort;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.entity.IngredientEntity;
import com.kaiho.gastromanager.infrastructure.ingredient.output.jpa.repository.IngredientEntityRepository;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.entity.ProductItemEntity;
import com.kaiho.gastromanager.infrastructure.productitem.output.jpa.repository.ProductItemRepository;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.entity.ProductItemIngredientEntity;
import com.kaiho.gastromanager.infrastructure.productitemingredient.output.jpa.repository.ProductItemIngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.kaiho.gastromanager.domain.ingredient.model.Unit.GRAMS;
import static com.kaiho.gastromanager.domain.ingredient.model.Unit.MILLILITRES;
import static com.kaiho.gastromanager.domain.ingredient.model.Unit.UNITS;
import static com.kaiho.gastromanager.domain.productitem.model.Category.BREAKFAST;
import static com.kaiho.gastromanager.domain.productitem.model.Category.DESSERT;
import static com.kaiho.gastromanager.domain.productitem.model.Category.DINNER;
import static com.kaiho.gastromanager.domain.productitem.model.Category.DRINK;
import static com.kaiho.gastromanager.domain.productitem.model.Category.FAST_FOOD;
import static com.kaiho.gastromanager.domain.productitem.model.Category.FRIED_FOOD;
import static com.kaiho.gastromanager.domain.productitem.model.Category.LUNCH;
import static com.kaiho.gastromanager.domain.productitem.model.Category.SIDE_DISH;

@Component
@RequiredArgsConstructor
@Slf4j
public class DBInit implements CommandLineRunner {

    private final UserPersistencePort userPersistencePort;
    private final RolePersistencePort rolePersistencePort;
    private final PasswordEncoder passwordEncoder;
    private final IngredientEntityRepository ingredientRepository;
    private final ProductItemRepository productItemRepository;
    private final ProductItemIngredientRepository productItemIngredientRepository;

    @Override
    public void run(String... args) throws Exception {
        createUserIfNotExists("cbarrios", "Carlos", "Barrios", "+16723381837", "cbarrios", Set.of(RoleType.ROLE_SUPERUSER));
        createUserIfNotExists("owner", "Owner", "User", "+16723381838", "owner", Set.of(RoleType.ROLE_OWNER));
        createUserIfNotExists("manager", "Manager", "User", "+16723381839", "manager", Set.of(RoleType.ROLE_MANAGER));
        createUserIfNotExists("waiter", "Waiter", "User", "+16723381840", "waiter", Set.of(RoleType.ROLE_WAITER));
        createUserIfNotExists("chef", "Chef", "User", "+16723381841", "chef", Set.of(RoleType.ROLE_CHEF));
        createUserIfNotExists("kitchenstaff", "Kitchen", "Staff", "+16723381842", "kitchenstaff", Set.of(RoleType.ROLE_KITCHEN_STAFF));
        createUserIfNotExists("cashier", "Cashier", "User", "+16723381843", "cashier", Set.of(RoleType.ROLE_CASHIER));
        productItemRepository.deleteAll();
        List<IngredientEntity> ingredients = ingredientRepository.findAll();

        ProductItemEntity pancakes = createProduct("Pancakes", "Fluffy breakfast pancakes", 5.99, BREAKFAST);
        ProductItemEntity burger = createProduct("Classic Burger", "Beef burger with lettuce, tomato, and cheese", 9.99, LUNCH);
        ProductItemEntity bolognese = createProduct("Spaghetti Bolognese", "Spaghetti with ground beef and tomato sauce", 11.99, DINNER);
        ProductItemEntity salad = createProduct("Chicken Caesar Salad", "Grilled chicken with Caesar dressing and croutons", 7.99, LUNCH);
        ProductItemEntity milkshake = createProduct("Chocolate Milkshake", "Thick chocolate milkshake", 3.99, DRINK);
        ProductItemEntity fries = createProduct("French Fries", "Crispy fried potatoes", 2.99, SIDE_DISH);
        ProductItemEntity chicken = createProduct("Fried Chicken", "Crispy fried chicken", 8.99, FRIED_FOOD);
        ProductItemEntity pizza = createProduct("Pepperoni Pizza", "Pizza with pepperoni, cheese, and tomato sauce", 12.99, FAST_FOOD);
        ProductItemEntity cheesecake = createProduct("Cheesecake", "Creamy cheesecake with a graham cracker crust", 4.99, DESSERT);
        ProductItemEntity wrap = createProduct("Veggie Wrap", "Healthy wrap with fresh vegetables and hummus", 6.99, LUNCH);

        //pancakes
        List<String> pancakesIngredients = List.of("All-Purpose Flour", "Eggs", "Milk");
        List<Double> pancakesQuantities = List.of(200.0, 2.0, 250.0);
        List<Unit> pancakesUnits = List.of(GRAMS, UNITS, MILLILITRES);
        createRelation(pancakes, pancakesIngredients, pancakesQuantities, pancakesUnits, ingredients);

        //burger
        List<String> burgerIngredients = List.of("Bun", "Ground Beef", "Lettuce", "Tomato", "Cheese", "Tomato Sauce");
        List<Double> burgerQuantities = List.of(1.0, 120.0, 10.0, 5.0, 50.0, 3.0);
        List<Unit> burgerUnits = List.of(UNITS, GRAMS, GRAMS, GRAMS, GRAMS, GRAMS);
        createRelation(burger, burgerIngredients, burgerQuantities, burgerUnits, ingredients);

        //bolognese
        List<String> bologneseIngredients = List.of("Pasta", "Ground Beef", "Tomato Sauce", "Onion", "Garlic", "Olive Oil", "Salt", "Pepper");
        List<Double> bologneseQuantities = List.of(100.0, 100.0, 150.0, 50.0, 5.0, 15.0, 2.0, 1.0); // Cantidades más realistas para una porción
        List<Unit> bologneseUnits = List.of(GRAMS, GRAMS, GRAMS, GRAMS, GRAMS, MILLILITRES, GRAMS, GRAMS);
        createRelation(bolognese, bologneseIngredients, bologneseQuantities, bologneseUnits, ingredients);

        //salad
        List<String> saladIngredients = List.of("Lettuce", "Tomato", "Cucumber", "Chicken Breast", "Caesar Dressing", "Croutons", "Parmesan Cheese");
        List<Double> saladQuantities = List.of(50.0, 100.0, 50.0, 75.0, 30.0, 20.0, 10.0); // Cantidades más realistas para una porción
        List<Unit> saladUnits = List.of(GRAMS, GRAMS, GRAMS, GRAMS, MILLILITRES, GRAMS, GRAMS);
        createRelation(salad, saladIngredients, saladQuantities, saladUnits, ingredients);

        //milkshake
        List<String> milkshakeIngredients = List.of("Milk", "Chocolate Ice Cream", "Granulated Sugar", "Chocolate Syrup", "Whipped Cream");
        List<Double> milkshakeQuantities = List.of(200.0, 150.0, 10.0, 15.0, 20.0); // Cantidades realistas para un milkshake
        List<Unit> milkshakeUnits = List.of(MILLILITRES, GRAMS, GRAMS, MILLILITRES, GRAMS);
        createRelation(milkshake, milkshakeIngredients, milkshakeQuantities, milkshakeUnits, ingredients);

        //fries
        List<String> friesIngredients = List.of("Potatoes", "Oil", "Salt");
        List<Double> friesQuantities = List.of(200.0, 30.0, 5.0); // Cantidades realistas para French Fries
        List<Unit> friesUnits = List.of(GRAMS, MILLILITRES, GRAMS);
        createRelation(fries, friesIngredients, friesQuantities, friesUnits, ingredients);

        //chicken
        List<String> chickenIngredients = List.of("Chicken Breast", "All-Purpose Flour", "Eggs", "Breadcrumbs", "Salt", "Pepper", "Oil");
        List<Double> chickenQuantities = List.of(150.0, 50.0, 1.0, 30.0, 3.0, 1.0, 20.0); // Cantidades realistas para Fried Chicken
        List<Unit> chickenUnits = List.of(GRAMS, GRAMS, UNITS, GRAMS, GRAMS, GRAMS, MILLILITRES);
        createRelation(chicken, chickenIngredients, chickenQuantities, chickenUnits, ingredients);

        //pizza
        List<String> pizzaIngredients = List.of("All-Purpose Flour", "Granulated Sugar", "Salt", "Butter", "Yeast", "Milk", "Tomato Sauce", "Cheese", "Pepperoni");
        List<Double> pizzaQuantities = List.of(500.0, 10.0, 5.0, 30.0, 7.0, 150.0, 200.0, 200.0, 100.0);
        List<Unit> pizzaUnits = List.of(GRAMS, GRAMS, GRAMS, GRAMS, GRAMS, MILLILITRES, GRAMS, GRAMS, GRAMS);
        createRelation(pizza, pizzaIngredients, pizzaQuantities, pizzaUnits, ingredients);

        //cheescake
        List<String> cheesecakeIngredients = List.of("Digestive Biscuits", "Butter", "Cream Cheese", "Granulated Sugar", "Eggs", "Vanilla Extract", "Milk");
        List<Double> cheesecakeQuantities = List.of(200.0, 100.0, 400.0, 100.0, 3.0, 10.0, 50.0);
        List<Unit> cheesecakeUnits = List.of(GRAMS, GRAMS, GRAMS, GRAMS, UNITS, MILLILITRES, MILLILITRES);
        createRelation(cheesecake, cheesecakeIngredients, cheesecakeQuantities, cheesecakeUnits, ingredients);

        //wrap
        List<String> wrapIngredients = List.of("Wraps", "Chicken Breast", "Lettuce", "Tomato", "Cheese", "Olive Oil");
        List<Double> wrapQuantities = List.of(2.0, 150.0, 50.0, 50.0, 50.0, 10.0);
        List<Unit> wrapUnits = List.of(UNITS, GRAMS, GRAMS, GRAMS, GRAMS, MILLILITRES);
        createRelation(wrap, wrapIngredients, wrapQuantities, wrapUnits, ingredients);
    }

    private void createRelation(ProductItemEntity product, List<String> usedIngredients, List<Double> usedQuantities, List<Unit> usedUnits, List<IngredientEntity> ingredients) {
        if (usedIngredients.size() == usedQuantities.size() && usedIngredients.size() == usedUnits.size()) {
            for (int i = 0; i < usedIngredients.size(); i++) {
                log.info("Creating relation for " + product.getName() + " with " + usedIngredients.get(i));
                int finalI = i;
                ProductItemIngredientEntity productItemIngredient = ProductItemIngredientEntity.builder()
                        .productItem(product)
                        .ingredient(ingredients.stream().filter(ing -> ing.getName().equals(usedIngredients.get(finalI))).findFirst().orElse(null))
                        .quantity(usedQuantities.get(finalI))
                        .unit(usedUnits.get(finalI))
                        .build();
                productItemIngredientRepository.save(productItemIngredient);
            }

        } else {
            log.info("Dimensions are not equals");
        }
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
                    .encodedPassword(passwordEncoder.encode(password))
                    .roles(Set.of(role)) // Assign the found role
                    .build();

            userPersistencePort.createUser(user);
            log.info("User '" + username + "' created successfully with role: " + role.roleType().getAuthority());
        } else {
            log.info("User '" + username + "' already exists.");
        }
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
