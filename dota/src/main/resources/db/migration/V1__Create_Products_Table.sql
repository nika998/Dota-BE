-- Create the product table
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    price DOUBLE,
    type VARCHAR(255),
    quantity INT,
    info VARCHAR(255),
    size VARCHAR(255),
    deleted BOOLEAN DEFAULT FALSE
);

-- Create the product_image table
CREATE TABLE product_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    color VARCHAR(255),
    display BOOLEAN,
    image_path VARCHAR(255),
    product_id BIGINT NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (product_id) REFERENCES product(id)
);