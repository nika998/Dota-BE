CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    price DECIMAL(10, 2),
    type VARCHAR(255),
    size VARCHAR(255),
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE product_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    color VARCHAR(255),
    quantity INT,
    info VARCHAR(255),
    deleted BOOLEAN DEFAULT FALSE,
    product_id BIGINT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE product_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    display BOOLEAN,
    image_path VARCHAR(255),
    product_detail_id BIGINT NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (product_detail_id) REFERENCES product_details(id)
);

-- Insert data into the 'product' table
INSERT INTO product (id, name, price, type, size, deleted) VALUES
(6, 'Leather bag', 350, 'torbica', '20 x 16cm', FALSE);

-- Insert data into the 'product_details' table for the 'white' color
INSERT INTO product_details (id, color, quantity, info, deleted, product_id) VALUES
(3, 'white', 35, 'Golden details', FALSE, 6);

-- Insert data into the 'product_details' table for the 'blue' color
INSERT INTO product_details (id, color, quantity, info, deleted, product_id) VALUES
(4, 'blue', 30, 'Silver details', FALSE, 6);

-- Insert data into the 'product_image' table for the 'white' color
INSERT INTO product_image (id, display, image_path, product_detail_id, deleted) VALUES
(7, true, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Leather bag/white/a627142a-1593-43d0-a445-a4a16566d27d.jpg', 3, FALSE),
(8, false, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Leather bag/white/40d64174-1acc-4255-aa9f-c9e6a2e1a917.jpg', 3, FALSE),
(9, false, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Leather bag/white/bc6953af-7d47-493a-85c2-5811bd2b0eda.jpg', 3, FALSE);

-- Insert data into the 'product_image' table for the 'blue' color
INSERT INTO product_image (id, display, image_path, product_detail_id, deleted) VALUES
(10, true, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Leather bag/blue/52f4507e-aeb3-41b5-8089-e988774ea653.jpg', 4, FALSE),
(11, false, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Leather bag/blue/226aa306-579d-4d63-972e-26e7e61fd3c1.jpg', 4, FALSE),
(12, false, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Leather bag/blue/78e9cffa-c081-4aac-b19e-7aea5fdc783c.jpg', 4, FALSE);




