DROP TABLE IF EXISTS _users CASCADE;

CREATE TABLE _users
(
    uuid             UUID PRIMARY KEY,
    name             VARCHAR(255)        NOT NULL,
    lastname         VARCHAR(255)        NOT NULL,
    email            VARCHAR(255) UNIQUE NOT NULL,
    phone            VARCHAR(20) UNIQUE  NOT NULL,
    restaurant_uuid  UUID,
    username         VARCHAR(50) UNIQUE  NOT NULL,
    encoded_password VARCHAR(255)        NOT NULL,
--     subscription_plan_uuid UUID NOT NULL,
    verified         BOOLEAN             NOT NULL,
    created_date     TIMESTAMP           NOT NULL,
    created_by       VARCHAR(50)         NOT NULL,
    updated_date     TIMESTAMP,
    updated_by       VARCHAR(50)
);

DROP TABLE IF EXISTS roles CASCADE;

CREATE TABLE roles
(
    uuid UUID PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

DROP TABLE IF EXISTS _users_roles;

CREATE TABLE _users_roles
(
    user_uuid UUID NOT NULL,
    role_uuid UUID NOT NULL,
    PRIMARY KEY (user_uuid, role_uuid),
    FOREIGN KEY (user_uuid) REFERENCES _users (uuid) ON DELETE CASCADE,
    FOREIGN KEY (role_uuid) REFERENCES roles (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS verification_tokens;

CREATE TABLE verification_tokens
(
    uuid        UUID PRIMARY KEY,
    token       UUID UNIQUE NOT NULL,
    user_uuid   UUID        NOT NULL,
    expiry_date TIMESTAMP   NOT NULL,
    FOREIGN KEY (user_uuid) REFERENCES _users (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS subscription_plans;

CREATE TABLE subscription_plans
(
    uuid         UUID PRIMARY KEY,
    name         VARCHAR(50) UNIQUE NOT NULL, -- Ej: "Basic", "Premium"
    description  TEXT,                        -- Descripción del plan
    price        DOUBLE PRECISION   NOT NULL, -- Precio mensual
    created_date TIMESTAMP          NOT NULL DEFAULT NOW()
);

DROP TABLE IF EXISTS plan_features;

CREATE TABLE plan_features
(
    uuid          UUID PRIMARY KEY,
    plan_uuid     UUID        NOT NULL,
    feature_type  VARCHAR(50) NOT NULL,
    feature_value VARCHAR(255),
    valid_from    TIMESTAMP   NOT NULL DEFAULT NOW(),
    valid_to      TIMESTAMP
);

DROP TABLE IF EXISTS ingredients CASCADE;

CREATE TABLE ingredients
(
    uuid                   UUID PRIMARY KEY,
    name                   VARCHAR(255)     NOT NULL,
    available_stock        INT              NOT NULL,
    unit                   VARCHAR(20)      NOT NULL CHECK (unit IN ('GRAMS', 'UNITS', 'MILLILITRES')),
    supplier               VARCHAR(255)     NOT NULL,
    price_per_unit         DOUBLE PRECISION NOT NULL,
    restaurant_uuid        UUID             NOT NULL,
    minimum_stock_quantity INT              NOT NULL,
    is_enabled             BOOLEAN          NOT NULL DEFAULT TRUE,
    created_date           TIMESTAMP        NOT NULL,
    created_by             VARCHAR(50)      NOT NULL,
    updated_date           TIMESTAMP,
    updated_by             VARCHAR(50)
);

DROP TABLE IF EXISTS product_items CASCADE;

CREATE TABLE product_items
(
    uuid            UUID PRIMARY KEY,
    name            VARCHAR(255) UNIQUE NOT NULL,
    description     TEXT,
    price           DOUBLE PRECISION    NOT NULL,
    category        VARCHAR(20)         NOT NULL CHECK (category IN
                                                        ('BREAKFAST', 'LUNCH', 'DINNER', 'DRINK', 'SIDE_DISH',
                                                         'FRIED_FOOD', 'FAST_FOOD', 'DESSERT')),
    is_enabled      BOOLEAN             NOT NULL DEFAULT TRUE,
    restaurant_uuid UUID                NOT NULL,
    created_date    TIMESTAMP           NOT NULL,
    created_by      VARCHAR(50)         NOT NULL,
    updated_date    TIMESTAMP,
    updated_by      VARCHAR(50)
);

DROP TABLE IF EXISTS product_item_ingredients;

CREATE TABLE product_item_ingredients
(
    uuid              UUID PRIMARY KEY,
    product_item_uuid UUID             NOT NULL,
--     restaurant_uuid   UUID             NOT NULL,
    ingredient_uuid   UUID             NOT NULL,
    quantity          DOUBLE PRECISION NOT NULL,
    created_date      TIMESTAMP        NOT NULL,
    created_by        VARCHAR(50)      NOT NULL,
    updated_date      TIMESTAMP,
    updated_by        VARCHAR(50),
    UNIQUE (product_item_uuid, ingredient_uuid),

    FOREIGN KEY (product_item_uuid) REFERENCES product_items (uuid) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_uuid) REFERENCES ingredients (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS restaurants CASCADE;

CREATE TABLE restaurants
(
    uuid         UUID PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(255),
    owner_uuid   UUID         NOT NULL,
    address      VARCHAR(255),
    created_date TIMESTAMP    NOT NULL,
    created_by   VARCHAR(50)  NOT NULL,
    updated_date TIMESTAMP,
    updated_by   VARCHAR(50),

    FOREIGN KEY (owner_uuid) REFERENCES _users (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS restaurant_partners;

CREATE TABLE restaurant_partners
(
    user_uuid       UUID      NOT NULL,
    restaurant_uuid UUID      NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_uuid, restaurant_uuid)
);
ALTER TABLE IF EXISTS _users
    ADD CONSTRAINT fk_restaurant FOREIGN KEY (restaurant_uuid) REFERENCES restaurants (uuid) ON DELETE SET NULL;
ALTER TABLE IF EXISTS ingredients
    ADD CONSTRAINT fk_restaurant FOREIGN KEY (restaurant_uuid) REFERENCES restaurants (uuid) ON DELETE SET NULL;
ALTER TABLE IF EXISTS product_items
    ADD CONSTRAINT fk_restaurant FOREIGN KEY (restaurant_uuid) REFERENCES restaurants (uuid) ON DELETE SET NULL;

DROP TABLE IF EXISTS restaurant_configs CASCADE;

CREATE TABLE restaurant_configs
(
    uuid                         UUID PRIMARY KEY,
    restaurant_uuid              UUID        NOT NULL UNIQUE,
    require_payment_before_order BOOLEAN     NOT NULL,
    created_date                 TIMESTAMP   NOT NULL,
    created_by                   VARCHAR(50) NOT NULL,
    updated_date                 TIMESTAMP,
    updated_by                   VARCHAR(50),
    FOREIGN KEY (restaurant_uuid) REFERENCES restaurants (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS orders CASCADE;

CREATE TABLE orders
(
    uuid            UUID PRIMARY KEY,
    code            VARCHAR(14)                                 NOT NULL UNIQUE,
    restaurant_uuid UUID                                        NOT NULL,
    user_uuid       UUID                                        NOT NULL,
    total_price     DOUBLE PRECISION                            NOT NULL,
    total_paid      DOUBLE PRECISION DEFAULT 0,
    customer_notes  VARCHAR(255),
    status          VARCHAR(20)      DEFAULT 'AWAITING_PAYMENT' NOT NULL CHECK (status IN
                                                                                ('AWAITING_PAYMENT', 'PENDING',
                                                                                 'COMPLETED', 'PREPARING', 'CANCELLED',
                                                                                 'READY', 'ON_TABLE')),
    created_date    TIMESTAMP                                   NOT NULL,
    created_by      VARCHAR(50)                                 NOT NULL,
    updated_date    TIMESTAMP,
    updated_by      VARCHAR(50),
    FOREIGN KEY (user_uuid) REFERENCES _users (uuid) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_uuid) REFERENCES restaurants (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS payments CASCADE;

CREATE TABLE payments
(
    uuid                UUID PRIMARY KEY,
    order_uuid          UUID                          NOT NULL,
    restaurant_uuid     UUID                          NOT NULL,
    amount              DOUBLE PRECISION              NOT NULL,
    status              VARCHAR(20) DEFAULT 'PENDING' NOT NULL CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED')),
    payment_date        TIMESTAMP                     NOT NULL,
    payment_method      VARCHAR(50),
    transaction_id      VARCHAR(100),
    is_partial          BOOLEAN     DEFAULT FALSE,
    parent_payment_uuid UUID,
    FOREIGN KEY (order_uuid) REFERENCES orders (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS order_items;

CREATE TABLE order_items
(
    uuid              UUID PRIMARY KEY,
    order_uuid        UUID             NOT NULL,
    product_item_uuid UUID             NOT NULL,
--     restaurant_uuid   UUID             NOT NULL,
    quantity          INT              NOT NULL,
    unit_price        DOUBLE PRECISION NOT NULL,
    created_date      TIMESTAMP        NOT NULL,
    created_by        VARCHAR(50)      NOT NULL,
    updated_date      TIMESTAMP,
    updated_by        VARCHAR(50),
    UNIQUE (order_uuid, product_item_uuid),

    FOREIGN KEY (order_uuid) REFERENCES orders (uuid) ON DELETE CASCADE,
    FOREIGN KEY (product_item_uuid) REFERENCES product_items (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS inventory_movements;

CREATE TABLE inventory_movements
(
    uuid            UUID PRIMARY KEY,
    ingredient_uuid UUID         NOT NULL,
    restaurant_uuid UUID         NOT NULL,
    change_quantity INT          NOT NULL,
    reason          VARCHAR(255) NOT NULL,
    created_date    TIMESTAMP    NOT NULL,
    created_by      VARCHAR(50)  NOT NULL,

    FOREIGN KEY (ingredient_uuid) REFERENCES ingredients (uuid) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_uuid) REFERENCES restaurants (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS price_history;

CREATE TABLE price_history
(
    uuid              UUID PRIMARY KEY,
    product_item_uuid UUID             NOT NULL,
    restaurant_uuid   UUID             NOT NULL,
    price             DOUBLE PRECISION NOT NULL,
    effective_date    TIMESTAMP        NOT NULL,
    end_date          TIMESTAMP,

    FOREIGN KEY (product_item_uuid) REFERENCES product_items (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS order_change_logs;

CREATE TABLE order_change_logs
(
    uuid            UUID PRIMARY KEY,
    order_uuid      UUID        NOT NULL,
    restaurant_uuid UUID        NOT NULL,
    previous_status VARCHAR(50),
    new_status      VARCHAR(50) NOT NULL,
    changed_by      UUID        NOT NULL,
    changed_at      TIMESTAMP   NOT NULL,

    FOREIGN KEY (order_uuid) REFERENCES orders (uuid) ON DELETE CASCADE
);


CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO roles
VALUES (uuid_generate_v4(), 'ROLE_SUPERUSER'),
       (uuid_generate_v4(), 'ROLE_OWNER'),
       (uuid_generate_v4(), 'ROLE_MANAGER'),
       (uuid_generate_v4(), 'ROLE_WAITER'),
       (uuid_generate_v4(), 'ROLE_CHEF'),
       (uuid_generate_v4(), 'ROLE_KITCHEN_STAFF'),
       (uuid_generate_v4(), 'ROLE_CASHIER');

INSERT INTO subscription_plans (uuid, name, description, price)
VALUES ('5e2cb774-bcf0-4e26-aedf-622eb6f35501', 'Basic', '3 restaurantes, 10 empleados', 29.99),
       ('58347f11-d555-4b73-95a1-6344b26aee51', 'Pro', '10 restaurantes, 50 empleados + Reportes', 99.99),
       ('61184460-4c95-476e-94ce-23f676ee94f4', 'Enterprise', 'Ilimitado + Soporte premium', 199.99);

INSERT INTO plan_features (uuid, plan_uuid, feature_type, feature_value)
VALUES (uuid_generate_v4(),'5e2cb774-bcf0-4e26-aedf-622eb6f35501', 'max_restaurants', '3'),
       (uuid_generate_v4(),'5e2cb774-bcf0-4e26-aedf-622eb6f35501', 'max_employees', '10'),
       (uuid_generate_v4(),'58347f11-d555-4b73-95a1-6344b26aee51', 'max_restaurants', '10'),
       (uuid_generate_v4(),'58347f11-d555-4b73-95a1-6344b26aee51', 'max_employees', '50'),
       (uuid_generate_v4(),'58347f11-d555-4b73-95a1-6344b26aee51', 'advanced_features', 'true'),
       (uuid_generate_v4(),'61184460-4c95-476e-94ce-23f676ee94f4', 'max_restaurants', 'unlimited'),
       (uuid_generate_v4(),'61184460-4c95-476e-94ce-23f676ee94f4', 'max_employees', 'unlimited'),
       (uuid_generate_v4(),'61184460-4c95-476e-94ce-23f676ee94f4', 'priority_support', 'true');
--product items
/*INSERT INTO product_items (uuid, name, description, price, category, is_enabled, created_date, created_by, updated_date,
                           updated_by)
VALUES
--1. Pancakes
(uuid_generate_v4(), 'Pancakes', 'Fluffy breakfast pancakes', 5.99, 'BREAKFAST', TRUE, '2024-10-09T00:00:00Z',
 'cbarrios', '2024-10-09T00:00:00Z', 'cbarrios'),

--2. Classic Burger
(uuid_generate_v4(), 'Classic Burger', 'Beef burger with lettuce, tomato, and cheese', 9.99, 'LUNCH', TRUE,
 '2024-10-09T00:00:00Z', 'cbarrios', '2024-10-09T00:00:00Z', 'cbarrios'),

--3. Spaghetti Bolognese
(uuid_generate_v4(), 'Spaghetti Bolognese', 'Spaghetti with ground beef and tomato sauce', 11.99, 'DINNER', TRUE,
 '2024-10-09T00:00:00Z', 'cbarrios', '2024-10-09T00:00:00Z', 'cbarrios'),

--4. Chicken Caesar Salad
(uuid_generate_v4(), 'Chicken Caesar Salad', 'Grilled chicken with Caesar dressing and croutons', 7.99, 'LUNCH', TRUE,
 '2024-10-09T00:00:00Z', 'cbarrios', '2024-10-09T00:00:00Z', 'cbarrios'),

--5. Chocolate Milkshake
(uuid_generate_v4(), 'Chocolate Milkshake', 'Thick chocolate milkshake', 3.99, 'DRINK', TRUE, '2024-10-09T00:00:00Z',
 'cbarrios', '2024-10-09T00:00:00Z', 'cbarrios'),

--6. French Fries
(uuid_generate_v4(), 'French Fries', 'Crispy fried potatoes', 2.99, 'SIDE_DISH', TRUE, '2024-10-09T00:00:00Z',
 'cbarrios', '2024-10-09T00:00:00Z', 'cbarrios'),

--7. Fried Chicken
(uuid_generate_v4(), 'Fried Chicken', 'Crispy fried chicken', 8.99, 'FRIED_FOOD', TRUE, '2024-10-09T00:00:00Z',
 'cbarrios', '2024-10-09T00:00:00Z', 'cbarrios'),

--8. Pepperoni Pizza
(uuid_generate_v4(), 'Pepperoni Pizza', 'Pizza with pepperoni, cheese, and tomato sauce', 12.99, 'FAST_FOOD', TRUE,
 '2024-10-09T00:00:00Z', 'cbarrios', '2024-10-09T00:00:00Z', 'cbarrios'),

--9. Cheesecake
(uuid_generate_v4(), 'Cheesecake', 'Creamy cheesecake with a graham cracker crust', 4.99, 'DESSERT', TRUE,
 '2024-10-09T00:00:00Z', 'cbarrios', '2024-10-09T00:00:00Z', 'cbarrios'),

--10. Chicken Wrap
(uuid_generate_v4(), 'Chicken Wrap', 'Healthy wrap with chciken breast and fresh vegetables', 6.99, 'LUNCH', TRUE,
 '2024-10-09T00:00:00Z', 'cbarrios', '2024-10-09T00:00:00Z', 'cbarrios');
*/
-- recipes
--pancakes
/*DELETE
FROM product_item_ingredients
WHERE product_item_uuid = (SELECT uuid FROM product_items WHERE name = 'Pancakes');

INSERT INTO product_item_ingredients (uuid, product_item_uuid, ingredient_uuid, quantity, created_date, created_by,
                                      updated_date, updated_by)
VALUES (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Pancakes'),
        (SELECT uuid FROM ingredients WHERE name = 'All-Purpose Flour'), 200.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Pancakes'),
        (SELECT uuid FROM ingredients WHERE name = 'Eggs'), 2.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Pancakes'),
        (SELECT uuid FROM ingredients WHERE name = 'Milk'), 250.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios');

-- Burger
DELETE
FROM product_item_ingredients
WHERE product_item_uuid = (SELECT uuid FROM product_items WHERE name = 'Classic Burger');

INSERT INTO product_item_ingredients (uuid, product_item_uuid, ingredient_uuid, quantity, created_date, created_by,
                                      updated_date, updated_by)
VALUES (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Classic Burger'),
        (SELECT uuid FROM ingredients WHERE name = 'Bun'), 1.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Classic Burger'),
        (SELECT uuid FROM ingredients WHERE name = 'Ground Beef'), 120.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Classic Burger'),
        (SELECT uuid FROM ingredients WHERE name = 'Lettuce'), 10.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Classic Burger'),
        (SELECT uuid FROM ingredients WHERE name = 'Tomato'), 5.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Classic Burger'),
        (SELECT uuid FROM ingredients WHERE name = 'Cheese'), 50.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Classic Burger'),
        (SELECT uuid FROM ingredients WHERE name = 'Tomato Sauce'), 3.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios');

-- Bolognese
DELETE
FROM product_item_ingredients
WHERE product_item_uuid = (SELECT uuid FROM product_items WHERE name = 'Spaghetti Bolognese');

INSERT INTO product_item_ingredients (uuid, product_item_uuid, ingredient_uuid, quantity, created_date, created_by,
                                      updated_date, updated_by)
VALUES (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Spaghetti Bolognese'),
        (SELECT uuid FROM ingredients WHERE name = 'Pasta'), 100.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Spaghetti Bolognese'),
        (SELECT uuid FROM ingredients WHERE name = 'Ground Beef'), 100.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Spaghetti Bolognese'),
        (SELECT uuid FROM ingredients WHERE name = 'Tomato Sauce'), 150.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Spaghetti Bolognese'),
        (SELECT uuid FROM ingredients WHERE name = 'Onion'), 50.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Spaghetti Bolognese'),
        (SELECT uuid FROM ingredients WHERE name = 'Garlic'), 5.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Spaghetti Bolognese'),
        (SELECT uuid FROM ingredients WHERE name = 'Olive Oil'), 15.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Spaghetti Bolognese'),
        (SELECT uuid FROM ingredients WHERE name = 'Salt'), 2.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Spaghetti Bolognese'),
        (SELECT uuid FROM ingredients WHERE name = 'Pepper'), 1.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios');

-- Salad
DELETE
FROM product_item_ingredients
WHERE product_item_uuid = (SELECT uuid FROM product_items WHERE name = 'Chicken Caesar Salad');

INSERT INTO product_item_ingredients (uuid, product_item_uuid, ingredient_uuid, quantity, created_date, created_by,
                                      updated_date, updated_by)
VALUES (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chicken Caesar Salad'),
        (SELECT uuid FROM ingredients WHERE name = 'Lettuce'), 50.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chicken Caesar Salad'),
        (SELECT uuid FROM ingredients WHERE name = 'Tomato'), 100.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chicken Caesar Salad'),
        (SELECT uuid FROM ingredients WHERE name = 'Cucumber'), 50.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chicken Caesar Salad'),
        (SELECT uuid FROM ingredients WHERE name = 'Chicken Breast'), 75.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chicken Caesar Salad'),
        (SELECT uuid FROM ingredients WHERE name = 'Caesar Dressing'), 30.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chicken Caesar Salad'),
        (SELECT uuid FROM ingredients WHERE name = 'Croutons'), 20.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chicken Caesar Salad'),
        (SELECT uuid FROM ingredients WHERE name = 'Parmesan Cheese'), 10.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios');

-- milkshake
DELETE
FROM product_item_ingredients
WHERE product_item_uuid = (SELECT uuid FROM product_items WHERE name = 'Chocolate Milkshake');

INSERT INTO product_item_ingredients (uuid, product_item_uuid, ingredient_uuid, quantity, created_date, created_by,
                                      updated_date, updated_by)
VALUES (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chocolate Milkshake'),
        (SELECT uuid FROM ingredients WHERE name = 'Milk'), 200.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chocolate Milkshake'),
        (SELECT uuid FROM ingredients WHERE name = 'Chocolate Ice Cream'), 150.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chocolate Milkshake'),
        (SELECT uuid FROM ingredients WHERE name = 'Granulated Sugar'), 10.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chocolate Milkshake'),
        (SELECT uuid FROM ingredients WHERE name = 'Chocolate Syrup'), 15.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chocolate Milkshake'),
        (SELECT uuid FROM ingredients WHERE name = 'Whipped Cream'), 20.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios');

-- fries
DELETE
FROM product_item_ingredients
WHERE product_item_uuid = (SELECT uuid FROM product_items WHERE name = 'French Fries');

INSERT INTO product_item_ingredients (uuid, product_item_uuid, ingredient_uuid, quantity, created_date, created_by,
                                      updated_date, updated_by)
VALUES (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'French Fries'),
        (SELECT uuid FROM ingredients WHERE name = 'Potatoes'), 200.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'French Fries'),
        (SELECT uuid FROM ingredients WHERE name = 'Oil'), 30.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'French Fries'),
        (SELECT uuid FROM ingredients WHERE name = 'Salt'), 5.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios');

-- chicken
DELETE
FROM product_item_ingredients
WHERE product_item_uuid = (SELECT uuid FROM product_items WHERE name = 'Fried Chicken');

INSERT INTO product_item_ingredients (uuid, product_item_uuid, ingredient_uuid, quantity, created_date, created_by,
                                      updated_date, updated_by)
VALUES (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Fried Chicken'),
        (SELECT uuid FROM ingredients WHERE name = 'Chicken Breast'), 150.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Fried Chicken'),
        (SELECT uuid FROM ingredients WHERE name = 'All-Purpose Flour'), 50.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Fried Chicken'),
        (SELECT uuid FROM ingredients WHERE name = 'Eggs'), 1.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Fried Chicken'),
        (SELECT uuid FROM ingredients WHERE name = 'Breadcrumbs'), 30.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Fried Chicken'),
        (SELECT uuid FROM ingredients WHERE name = 'Salt'), 3.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Fried Chicken'),
        (SELECT uuid FROM ingredients WHERE name = 'Pepper'), 1.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Fried Chicken'),
        (SELECT uuid FROM ingredients WHERE name = 'Oil'), 20.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios');

-- pizza
DELETE
FROM product_item_ingredients
WHERE product_item_uuid = (SELECT uuid FROM product_items WHERE name = 'Pepperoni Pizza');

INSERT INTO product_item_ingredients (uuid, product_item_uuid, ingredient_uuid, quantity, created_date, created_by,
                                      updated_date, updated_by)
VALUES (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Pepperoni Pizza'),
        (SELECT uuid FROM ingredients WHERE name = 'All-Purpose Flour'), 500.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Pepperoni Pizza'),
        (SELECT uuid FROM ingredients WHERE name = 'Granulated Sugar'), 10.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Pepperoni Pizza'),
        (SELECT uuid FROM ingredients WHERE name = 'Salt'), 5.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Pepperoni Pizza'),
        (SELECT uuid FROM ingredients WHERE name = 'Butter'), 30.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Pepperoni Pizza'),
        (SELECT uuid FROM ingredients WHERE name = 'Yeast'), 7.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Pepperoni Pizza'),
        (SELECT uuid FROM ingredients WHERE name = 'Milk'), 150.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Pepperoni Pizza'),
        (SELECT uuid FROM ingredients WHERE name = 'Tomato Sauce'), 200.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Pepperoni Pizza'),
        (SELECT uuid FROM ingredients WHERE name = 'Cheese'), 200.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Pepperoni Pizza'),
        (SELECT uuid FROM ingredients WHERE name = 'Pepperoni'), 100.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios');

-- cheesecake
DELETE
FROM product_item_ingredients
WHERE product_item_uuid = (SELECT uuid FROM product_items WHERE name = 'Cheesecake');

INSERT INTO product_item_ingredients (uuid, product_item_uuid, ingredient_uuid, quantity, created_date, created_by,
                                      updated_date, updated_by)
VALUES (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Cheesecake'),
        (SELECT uuid FROM ingredients WHERE name = 'Digestive Biscuits'), 200.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Cheesecake'),
        (SELECT uuid FROM ingredients WHERE name = 'Butter'), 100.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Cheesecake'),
        (SELECT uuid FROM ingredients WHERE name = 'Cream Cheese'), 400.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Cheesecake'),
        (SELECT uuid FROM ingredients WHERE name = 'Granulated Sugar'), 100.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Cheesecake'),
        (SELECT uuid FROM ingredients WHERE name = 'Eggs'), 3.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Cheesecake'),
        (SELECT uuid FROM ingredients WHERE name = 'Vanilla Extract'), 10.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Cheesecake'),
        (SELECT uuid FROM ingredients WHERE name = 'Milk'), 50.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios');

-- wrap
DELETE
FROM product_item_ingredients
WHERE product_item_uuid = (SELECT uuid FROM product_items WHERE name = 'Chicken Wrap');

INSERT INTO product_item_ingredients (uuid, product_item_uuid, ingredient_uuid, quantity, created_date, created_by,
                                      updated_date, updated_by)
VALUES (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chicken Wrap'),
        (SELECT uuid FROM ingredients WHERE name = 'Wraps'), 2.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chicken Wrap'),
        (SELECT uuid FROM ingredients WHERE name = 'Chicken Breast'), 150.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chicken Wrap'),
        (SELECT uuid FROM ingredients WHERE name = 'Lettuce'), 50.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chicken Wrap'),
        (SELECT uuid FROM ingredients WHERE name = 'Tomato'), 50.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chicken Wrap'),
        (SELECT uuid FROM ingredients WHERE name = 'Cheese'), 50.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios'),
       (uuid_generate_v4(), (SELECT uuid FROM product_items WHERE name = 'Chicken Wrap'),
        (SELECT uuid FROM ingredients WHERE name = 'Olive Oil'), 10.0, '2024-10-09T00:00:00Z', 'cbarrios',
        '2024-10-09T00:00:00Z', 'cbarrios');*/