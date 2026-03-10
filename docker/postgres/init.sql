-- docker/postgres/init.sql
-- The database 'shopping_list_db' is created by the POSTGRES_DB env var.
-- Each service manages its own tables via Flyway migrations:
--   culmen            → users
--   shopping-service  → shopping_lists, shopping_list_items, product_prices
\c shopping_list_db;
