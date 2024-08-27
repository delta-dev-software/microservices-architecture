-- src/main/resources/db/migration/V1__Create_orders_table.sql
CREATE TABLE orders (
    id VARCHAR(255) PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    order_date TIMESTAMP NOT NULL
);
