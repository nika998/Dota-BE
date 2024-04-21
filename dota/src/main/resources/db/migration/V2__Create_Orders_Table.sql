-- Create the order_table table
CREATE TABLE order_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255),
    city VARCHAR(255),
    postal_code VARCHAR(255),
    address VARCHAR(255),
    flat_number VARCHAR(255),
    phone VARCHAR(255),
    description VARCHAR(255),
    deleted BOOLEAN DEFAULT FALSE,
    wait_reserved BOOLEAN DEFAULT TRUE,
    total_price DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the order_item table
CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_details_id BIGINT NOT NULL,
    quantity INT,
    deleted BOOLEAN DEFAULT FALSE,
    available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (order_id) REFERENCES order_table(id),
    FOREIGN KEY (product_details_id) REFERENCES product_details(id)
);