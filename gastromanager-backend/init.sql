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
    price        DOUBLE PRECISION   NOT NULL, -- Precio mensual //todo cambiar a DECIMAL(10, 5) y cambiar el mapeo de entidad a BigDecimal
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

DROP TABLE IF EXISTS restaurants CASCADE;

CREATE TABLE restaurants
(
    uuid                   UUID PRIMARY KEY,
    name                   VARCHAR(255) NOT NULL,
    description            VARCHAR(255),
    owner_uuid             UUID         NOT NULL,
    address                VARCHAR(255),
    restaurant_config_uuid UUID UNIQUE  NOT NULL,
    created_date           TIMESTAMP    NOT NULL,
    created_by             VARCHAR(50)  NOT NULL,
    updated_date           TIMESTAMP,
    updated_by             VARCHAR(50),

    FOREIGN KEY (owner_uuid) REFERENCES _users (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS restaurant_configs CASCADE;

CREATE TABLE restaurant_configs
(
    uuid             UUID PRIMARY KEY,
    is_franchise     BOOLEAN     NOT NULL DEFAULT false,
    pay_before_order BOOLEAN     NOT NULL DEFAULT false,
    enable_delivery  BOOLEAN     NOT NULL DEFAULT false,
    created_date     TIMESTAMP   NOT NULL,
    created_by       VARCHAR(50) NOT NULL,
    updated_date     TIMESTAMP,
    updated_by       VARCHAR(50)
);

DROP TABLE IF EXISTS ingredients CASCADE;

CREATE TABLE ingredients
(
    uuid                   UUID PRIMARY KEY,
    name                   VARCHAR(255)     NOT NULL,
    available_stock        DOUBLE PRECISION NOT NULL,
    unit                   VARCHAR(20)      NOT NULL CHECK (unit IN ('GRAMS', 'UNITS', 'MILLILITRES')),
    supplier               VARCHAR(255)     NOT NULL,
    price_per_unit         DECIMAL(10, 2)   NOT NULL,
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

DROP TABLE IF EXISTS products CASCADE;

CREATE TABLE products
(
    uuid            UUID PRIMARY KEY,
    name            VARCHAR(255)   NOT NULL,
    description     TEXT,
    category        VARCHAR(20),
    purchase_price  DECIMAL(10, 2) NOT NULL,
    sale_price      DECIMAL(10, 2) NOT NULL,
    is_enabled      BOOLEAN        NOT NULL,
    restaurant_uuid UUID           NOT NULL,
    mode            VARCHAR(10)    NOT NULL DEFAULT 'BASIC' CHECK (mode IN ('BASIC', 'ADVANCED')),
    created_date    TIMESTAMP      NOT NULL,
    created_by      VARCHAR(50)    NOT NULL,
    updated_date    TIMESTAMP,
    updated_by      VARCHAR(50),
    FOREIGN KEY (restaurant_uuid) REFERENCES restaurants (uuid) ON DELETE CASCADE
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

ALTER TABLE IF EXISTS restaurants
    ADD CONSTRAINT fk_restaurant_config FOREIGN KEY (restaurant_config_uuid) REFERENCES restaurant_configs (uuid) ON DELETE SET NULL;

DROP TABLE IF EXISTS tax_configs CASCADE;

CREATE TABLE tax_configs
(
    uuid         UUID PRIMARY KEY,
    tax_type     VARCHAR(50)      NOT NULL UNIQUE CHECK (tax_type IN ('IVA', 'IMPO_CONSUMO')),
    tax_rate     DOUBLE PRECISION NOT NULL,
    description  TEXT,
    created_date TIMESTAMP        NOT NULL,
    created_by   VARCHAR(50)      NOT NULL,
    updated_date TIMESTAMP,
    updated_by   VARCHAR(50)
);

DROP TABLE IF EXISTS restaurant_taxes CASCADE;

CREATE TABLE restaurant_taxes
(
    restaurant_uuid        UUID REFERENCES restaurants (uuid) ON DELETE CASCADE,
    tax_configuration_uuid UUID REFERENCES tax_configs (uuid) ON DELETE CASCADE,
    PRIMARY KEY (restaurant_uuid, tax_configuration_uuid)
);

DROP TABLE IF EXISTS menus CASCADE;

CREATE TABLE menus
(
    uuid            UUID PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(255) NOT NULL,
    is_enabled      BOOLEAN      NOT NULL,
    restaurant_uuid UUID         NOT NULL,
    created_date    TIMESTAMP    NOT NULL,
    created_by      VARCHAR(50)  NOT NULL,
    updated_date    TIMESTAMP,
    updated_by      VARCHAR(50),
    FOREIGN KEY (restaurant_uuid) REFERENCES restaurants (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS submenus CASCADE;

CREATE TABLE submenus
(
    uuid            UUID PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(255) NOT NULL,
    is_enabled      BOOLEAN      NOT NULL,
    restaurant_uuid UUID         NOT NULL,
    menu_uuid       UUID         NOT NULL,
    created_date    TIMESTAMP    NOT NULL,
    created_by      VARCHAR(50)  NOT NULL,
    updated_date    TIMESTAMP,
    updated_by      VARCHAR(50),

    FOREIGN KEY (restaurant_uuid) REFERENCES restaurants (uuid) ON DELETE CASCADE,
    FOREIGN KEY (menu_uuid) REFERENCES menus (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS orders CASCADE;

CREATE TABLE orders
(
    uuid                          UUID PRIMARY KEY,
    code                          VARCHAR(14)                            NOT NULL UNIQUE,
    restaurant_uuid               UUID                                   NOT NULL,
--     user_uuid          UUID                                        NOT NULL,
    total_amount                  DECIMAL(10, 2)                         NOT NULL,
    total_paid                    DECIMAL(10, 2)                         NOT NULL DEFAULT 0,
    requires_payment_before_order BOOLEAN                                NOT NULL,
    customer_notes                VARCHAR(255),
    table_number                  VARCHAR(50),
    customer_name                 VARCHAR(255),
    operational_status            VARCHAR(20) DEFAULT 'AWAITING_PAYMENT' NOT NULL CHECK (operational_status IN
                                                                                         ('AWAITING_PAYMENT', 'PENDING',
                                                                                          'COMPLETED', 'PREPARING',
                                                                                          'CANCELLED',
                                                                                          'READY', 'SERVED')),
    payment_status                VARCHAR(20) DEFAULT 'UNPAID'           NOT NULL CHECK (payment_status IN
                                                                                         ('UNPAID', 'PARTIALLY_PAID',
                                                                                          'FULLY_PAID', 'REFUNDED')),
    invoicing_status              VARCHAR(20) DEFAULT 'NOT_INVOICED'     NOT NULL CHECK (invoicing_status IN
                                                                                         ('NOT_INVOICED',
                                                                                          'PARTIALLY_INVOICED',
                                                                                          'FULLY_INVOICED')),
    created_date                  TIMESTAMP                              NOT NULL,
    created_by                    VARCHAR(50)                            NOT NULL,
    updated_date                  TIMESTAMP,
    updated_by                    VARCHAR(50),
--     FOREIGN KEY (user_uuid) REFERENCES _users (uuid) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_uuid) REFERENCES restaurants (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS recipes CASCADE;

CREATE TABLE recipes
(
    uuid                UUID PRIMARY KEY,
    name                VARCHAR(255)   NOT NULL,
    description         VARCHAR(255)   NOT NULL,
    cost                DECIMAL(10, 2) NOT NULL,
    base_recipe_uuid    UUID,
    base_recipe_portion DOUBLE PRECISION,
    restaurant_uuid     UUID           NOT NULL,
    is_enabled          BOOLEAN        NOT NULL DEFAULT TRUE,
    created_date        TIMESTAMP      NOT NULL,
    created_by          VARCHAR(50)    NOT NULL,
    updated_date        TIMESTAMP,
    updated_by          VARCHAR(50),
    FOREIGN KEY (restaurant_uuid) REFERENCES restaurants (uuid) ON DELETE CASCADE,
    FOREIGN KEY (base_recipe_uuid) REFERENCES recipes (uuid) ON DELETE SET NULL
);

DROP TABLE IF EXISTS recipes_ingredients CASCADE;

CREATE TABLE recipes_ingredients
(
    uuid            UUID PRIMARY KEY,
    recipe_uuid     UUID             NOT NULL,
--     restaurant_uuid   UUID             NOT NULL,
    ingredient_uuid UUID             NOT NULL,
    quantity        DOUBLE PRECISION NOT NULL,
    created_date    TIMESTAMP        NOT NULL,
    created_by      VARCHAR(50)      NOT NULL,
    updated_date    TIMESTAMP,
    updated_by      VARCHAR(50),
    UNIQUE (recipe_uuid, ingredient_uuid),

    FOREIGN KEY (recipe_uuid) REFERENCES recipes (uuid) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_uuid) REFERENCES ingredients (uuid) ON DELETE RESTRICT
);

DROP TABLE IF EXISTS product_recipes CASCADE;

CREATE TABLE product_recipes
(
    uuid                UUID PRIMARY KEY,
    product_uuid        UUID NOT NULL,
    recipe_uuid         UUID NOT NULL,
    quantity_multiplier DOUBLE PRECISION DEFAULT 1,
    last_synced_date    TIMESTAMP,
    created_date    TIMESTAMP        NOT NULL,
    created_by      VARCHAR(50)      NOT NULL,
    updated_date    TIMESTAMP,
    updated_by      VARCHAR(50),
    UNIQUE (product_uuid, recipe_uuid),
    FOREIGN KEY (product_uuid) REFERENCES products (uuid) ON DELETE CASCADE,
    FOREIGN KEY (recipe_uuid) REFERENCES recipes (uuid) ON DELETE RESTRICT
);

DROP TABLE IF EXISTS product_ingredients CASCADE;

CREATE TABLE product_ingredients
(
    uuid            UUID PRIMARY KEY,
    product_uuid    UUID             NOT NULL,
    ingredient_uuid UUID             NOT NULL,
    quantity        DOUBLE PRECISION NOT NULL,
    unit            VARCHAR(20),
    created_date    TIMESTAMP        NOT NULL,
    created_by      VARCHAR(50)      NOT NULL,
    updated_date    TIMESTAMP,
    updated_by      VARCHAR(50),
    UNIQUE (product_uuid, ingredient_uuid),
    FOREIGN KEY (product_uuid) REFERENCES products (uuid) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_uuid) REFERENCES ingredients (uuid) ON DELETE RESTRICT
);

DROP TABLE IF EXISTS order_items;

CREATE TABLE order_items
(
    uuid                UUID PRIMARY KEY,
    order_uuid          UUID           NOT NULL,
    product_uuid        UUID           NOT NULL,
--     restaurant_uuid   UUID             NOT NULL,
    quantity            INT            NOT NULL,
    unit_price          DECIMAL(10, 2) NOT NULL,-- precio en el momento de la orden
    subtotal            DECIMAL(10, 2) NOT NULL, -- cantidad * precio
    discount_percentage DOUBLE PRECISION DEFAULT 0,
    customer_notes      VARCHAR(500), -- notas del cliente para este item
    created_date        TIMESTAMP      NOT NULL,
    created_by          VARCHAR(50)    NOT NULL,
    updated_date        TIMESTAMP,
    updated_by          VARCHAR(50),
    UNIQUE (order_uuid, product_uuid),

    FOREIGN KEY (order_uuid) REFERENCES orders (uuid) ON DELETE CASCADE,
    FOREIGN KEY (product_uuid) REFERENCES products (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS invoices;

CREATE TABLE invoices
(
    uuid            UUID PRIMARY KEY,
    order_uuid      UUID                             NOT NULL,
    restaurant_uuid UUID                             NOT NULL,
    customer_name   VARCHAR(255)                     NOT NULL,
    customer_phone  VARCHAR(255)                     NOT NULL,
    customer_email  VARCHAR(255)                     NOT NULL,
    subtotal        DECIMAL(10, 2)                   NOT NULL,
    tax_amount      DECIMAL(10, 2)                   NOT NULL,
    tax_rate        DECIMAL(10, 2)                   NOT NULL,
    tip_amount      DECIMAL(10, 2) DEFAULT 0,
    amount          DECIMAL(10, 2)                   NOT NULL, -- Total a pagar (subtotal + impuestos + tip, o según se calcule)
    discount        DECIMAL(10, 2) DEFAULT 0,
    payment_status  VARCHAR(20)    DEFAULT 'PENDING' NOT NULL
        CHECK (payment_status IN ('PENDING', 'PAID', 'PARTIALLY_PAID', 'CANCELED')),
    created_date    TIMESTAMP                        NOT NULL,
    created_by      VARCHAR(50)                      NOT NULL,
    updated_date    TIMESTAMP,
    updated_by      VARCHAR(50),
    FOREIGN KEY (order_uuid) REFERENCES orders (uuid) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_uuid) REFERENCES restaurants (uuid) ON DELETE CASCADE
);

DROP TABLE IF EXISTS invoice_items;

CREATE TABLE invoice_items
(
    uuid            UUID PRIMARY KEY,
    invoice_uuid    UUID             NOT NULL,
    order_item_uuid UUID             NOT NULL,
    quantity        INT              NOT NULL CHECK (quantity > 0),
    unit_price      DECIMAL(10, 2)   NOT NULL CHECK (unit_price > 0),
    discount        DECIMAL(10, 2) DEFAULT 0,
    tax_amount      DECIMAL(10, 2)   NOT NULL,
    tax_rate        DOUBLE PRECISION NOT NULL,
    created_date    TIMESTAMP        NOT NULL,
    created_by      VARCHAR(50)      NOT NULL,
    updated_date    TIMESTAMP,
    updated_by      VARCHAR(50),
    FOREIGN KEY (invoice_uuid) REFERENCES invoices (uuid) ON DELETE CASCADE,
    FOREIGN KEY (order_item_uuid) REFERENCES order_items (uuid) ON DELETE RESTRICT
);

DROP TABLE IF EXISTS cash_registers;

CREATE TABLE cash_registers
(
    uuid            UUID PRIMARY KEY,
    restaurant_uuid UUID         NOT NULL REFERENCES restaurants (uuid),
    name            VARCHAR(100) NOT NULL, -- Ej: "Caja 1 Barra"
    location        VARCHAR(100),          -- opcional
    description     TEXT,
    device_id       VARCHAR(100),          -- opcional
    created_by      VARCHAR(50)  NOT NULL,
    created_date    TIMESTAMP    NOT NULL,
    updated_by      VARCHAR(50)  NOT NULL,
    updated_date    TIMESTAMP    NOT NULL
);

DROP TABLE IF EXISTS cash_register_sessions;

CREATE TABLE cash_register_sessions
(
    uuid               UUID PRIMARY KEY,
    cash_register_uuid UUID           NOT NULL REFERENCES cash_registers (uuid),
    user_uuid          UUID           NOT NULL REFERENCES _users (uuid),
    opening_time       TIMESTAMP      NOT NULL,
    closing_time       TIMESTAMP,
    notes              TEXT,
    opening_amount     NUMERIC(12, 2) NOT NULL,
    closing_amount     NUMERIC(12, 2),
    expected_amount    NUMERIC(12, 2),
    difference         NUMERIC(12, 2),
    balance_status     VARCHAR(20) CHECK (balance_status IN ('BALANCED', 'SHORT', 'OVER')),
    status             VARCHAR(20)    NOT NULL CHECK (status IN ('OPEN', 'CLOSED')),
    created_by         VARCHAR(50)    NOT NULL,
    created_date       TIMESTAMP      NOT NULL,
    updated_by         VARCHAR(50)    NOT NULL,
    updated_date       TIMESTAMP      NOT NULL
);

DROP TABLE IF EXISTS cash_register_movements;

CREATE TABLE cash_register_movements
(
    uuid                       UUID PRIMARY KEY,
    cash_register_session_uuid UUID           NOT NULL REFERENCES cash_register_sessions (uuid),
    movement_type              VARCHAR(20)    NOT NULL CHECK (movement_type IN ('INCOME', 'EXPENSE', 'ADJUSTMENT')),
    adjustment_direction       VARCHAR(10) CHECK (adjustment_direction IN ('INCREASE', 'DECREASE')),
    amount                     NUMERIC(12, 2) NOT NULL,
    reason                     TEXT,         -- descripción del movimiento: "Ingreso adicional", "Pago a proveedor", etc.
    reference                  VARCHAR(100), -- opcional: número de factura, comprobante, etc.
    created_by                 VARCHAR(50)    NOT NULL,
    created_date               TIMESTAMP      NOT NULL,
    updated_by                 VARCHAR(50)    NOT NULL,
    updated_date               TIMESTAMP      NOT NULL
);

DROP TABLE IF EXISTS payments CASCADE;

CREATE TABLE payments
(
    uuid                       UUID PRIMARY KEY,
    order_uuid                 UUID           NOT NULL REFERENCES orders (uuid),
    restaurant_uuid            UUID           NOT NULL REFERENCES restaurants (uuid),
    amount                     NUMERIC(12, 2) NOT NULL,
    payment_method             VARCHAR(50)    NOT NULL,
    tip_amount                 NUMERIC(12, 2) NOT NULL DEFAULT 0,
    notes                      TEXT,
    state                      VARCHAR(20)    NOT NULL,
    created_by                 VARCHAR(50)    NOT NULL,
    created_date               TIMESTAMP      NOT NULL,
    updated_by                 VARCHAR(50)    NOT NULL,
    updated_date               TIMESTAMP      NOT NULL,
    transaction_id             VARCHAR(100),
    cash_register_session_uuid UUID           NOT NULL REFERENCES cash_register_sessions (uuid)
);

DROP TABLE IF EXISTS inventory_movements;

CREATE TABLE inventory_movements
(
    uuid            UUID PRIMARY KEY,
    ingredient_uuid UUID             NOT NULL,
    restaurant_uuid UUID             NOT NULL,
    change_quantity DOUBLE PRECISION NOT NULL,
    reason          VARCHAR(255)     NOT NULL,
    created_date    TIMESTAMP        NOT NULL,
    created_by      VARCHAR(50)      NOT NULL,

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
VALUES (uuid_generate_v4(), '5e2cb774-bcf0-4e26-aedf-622eb6f35501', 'max_restaurants', '3'),
       (uuid_generate_v4(), '5e2cb774-bcf0-4e26-aedf-622eb6f35501', 'max_employees', '10'),
       (uuid_generate_v4(), '58347f11-d555-4b73-95a1-6344b26aee51', 'max_restaurants', '10'),
       (uuid_generate_v4(), '58347f11-d555-4b73-95a1-6344b26aee51', 'max_employees', '50'),
       (uuid_generate_v4(), '58347f11-d555-4b73-95a1-6344b26aee51', 'advanced_features', 'true'),
       (uuid_generate_v4(), '61184460-4c95-476e-94ce-23f676ee94f4', 'max_restaurants', 'unlimited'),
       (uuid_generate_v4(), '61184460-4c95-476e-94ce-23f676ee94f4', 'max_employees', 'unlimited'),
       (uuid_generate_v4(), '61184460-4c95-476e-94ce-23f676ee94f4', 'priority_support', 'true');

INSERT INTO tax_configs (uuid, tax_type, tax_rate, description, created_date, created_by, updated_by, updated_date)
VALUES (uuid_generate_v4(), 'IVA', 0.19, 'Impuesto valor agregado', now(), 'admin', 'admin',
        now()), -- IVA para franquicias
       (uuid_generate_v4(), 'IMPO_CONSUMO', 0.08, 'Impuesto al consumo', now(), 'admin', 'admin', now());
-- Impoconsumo para no franquicias
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
        '2024-10-09T00:00:00Z', 'cbarrios');
*/