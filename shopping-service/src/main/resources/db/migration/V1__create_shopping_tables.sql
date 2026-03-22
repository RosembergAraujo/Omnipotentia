CREATE TABLE shopping_lists (
    id         UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(255) NOT NULL,
    owner_id   UUID         NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE shopping_list_items (
    id       UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    list_id  UUID           NOT NULL REFERENCES shopping_lists(id) ON DELETE CASCADE,
    name     VARCHAR(255)   NOT NULL,
    quantity NUMERIC(10, 3) NOT NULL DEFAULT 1,
    unit     VARCHAR(50),
    checked  BOOLEAN        NOT NULL DEFAULT FALSE,
    added_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_prices (
    id           UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    product_name VARCHAR(255)   NOT NULL,
    store_name   VARCHAR(255)   NOT NULL,
    price        NUMERIC(10, 2) NOT NULL,
    reported_by  UUID           NOT NULL,
    reported_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_shopping_lists_owner_id       ON shopping_lists(owner_id);
CREATE INDEX idx_shopping_list_items_list_id   ON shopping_list_items(list_id);
CREATE INDEX idx_product_prices_product_name   ON product_prices(product_name);
CREATE INDEX idx_product_prices_store_reported ON product_prices(product_name, store_name, reported_at DESC);
