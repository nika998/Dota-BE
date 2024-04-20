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

INSERT INTO product (name, price, type, size, deleted)
VALUES ('Moonlight', 200.0, 'torbica', '20 x 16cm', FALSE),
       ('Cream bag', 350.0, 'torbica', '20 x 16cm', FALSE);

INSERT INTO product_details (color, quantity, info, deleted, product_id)
VALUES ('black',40, 'Mini bag', FALSE, 1);

SET @product_detail_id_moonlight = LAST_INSERT_ID();

INSERT INTO product_image (display, image_path, product_detail_id, deleted)
VALUES
(TRUE, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Moonlight/black/5e84efbc-1a67-45ed-94ff-274ce728b5aa.jpg', @product_detail_id_moonlight, FALSE),
(FALSE, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Moonlight/black/fbdd3e2f-2ef8-48c6-9a90-e9351c520e7b.jpg', @product_detail_id_moonlight, FALSE),
(FALSE, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Moonlight/black/5d4dc253-e3f4-4a44-a254-0e546b43b6a7.jpg.jpg', @product_detail_id_moonlight, FALSE);

INSERT INTO product_details (color, quantity, info, deleted, product_id)
VALUES ('green', 10, 'Golden details', FALSE, 2);

SET @product_detail_id_cream_bag = LAST_INSERT_ID();

INSERT INTO product_image (display, image_path, product_detail_id, deleted)
VALUES
(TRUE, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Cream bag/green/3b2d4633-0c6c-46cc-80ec-447084a4a91d.jpg', @product_detail_id_cream_bag, FALSE),
(FALSE, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Cream bag/green/647aa6b8-e59f-4235-b16c-8c9ce657cf15.jpg', @product_detail_id_cream_bag, FALSE),
(FALSE, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Cream bag/green/7bedd151-e251-4f45-9278-5a04e1a42fc6.jpg', @product_detail_id_cream_bag, FALSE);
