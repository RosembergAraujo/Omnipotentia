CREATE TABLE users (
    id                UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    telegram_id       VARCHAR(255) NOT NULL,
    telegram_username VARCHAR(255),
    phone_number      VARCHAR(20),
    display_name      VARCHAR(255),
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active         BOOLEAN               DEFAULT TRUE,

    CONSTRAINT uk_telegram_id UNIQUE (telegram_id)
);

CREATE INDEX idx_users_telegram_id ON users (telegram_id);
