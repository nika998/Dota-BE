INSERT IGNORE INTO product (id, name, price, type, quantity, info, size, deleted)
VALUES (2, 'Cream bag', 350.0, 'torbica', 13, 'Golden details', '20 x 16cm', 0);

INSERT IGNORE INTO product_image (id, color, display, image_path, product_id, deleted)
VALUES (5, 'green', true, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Cream bag/green/3b2d4633-0c6c-46cc-80ec-447084a4a91d.jpg', 2, 0),
       (6, 'green', false, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Cream bag/green/647aa6b8-e59f-4235-b16c-8c9ce657cf15.jpg', 2, 0),
       (7, 'green', false, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Cream bag/green/7bedd151-e251-4f45-9278-5a04e1a42fc6.jpg', 2, 0),
       (8, 'blue', true, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Cream bag/blue/54e8e8f2-55d5-4808-8afe-a5c2baac43dc.jpg', 2, 0),
       (9, 'blue', false, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Cream bag/blue/da44c8d5-e195-413b-804b-de18c311a2b5.jpg', 2, 0),
       (10, 'blue', false, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Cream bag/blue/711da2ae-e8d0-45f7-bcf8-20c4e652752c.jpg', 2, 0);

INSERT IGNORE INTO product (id, name, price, type, quantity, info, size, deleted)
VALUES (1, 'Moonlight', 200.0, 'torbica', 50, 'Mini bag', '20 x 16cm', 0);

INSERT IGNORE INTO product_image (id, color, display, image_path, product_id, deleted)
VALUES
(1, 'black', true, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Moonlight/black/5e84efbc-1a67-45ed-94ff-274ce728b5aa.jpg', 1, 0),
(2, 'black', false, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Moonlight/black/fbdd3e2f-2ef8-48c6-9a90-e9351c520e7b.jpg', 1, 0),
(3, 'black', false, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Moonlight/black/5d4dc253-e3f4-4a44-a254-0e546b43b6a7.jpg', 1, 0),
(4, 'red', true, 'https://dota-bucket-test.s3.eu-central-1.amazonaws.com/images/torbica/Moonlight/red/6c51aeef-647e-47fe-9d7e-f432ef9cffdc.jpg', 1, 0);