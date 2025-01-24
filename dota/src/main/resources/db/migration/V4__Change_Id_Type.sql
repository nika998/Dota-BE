ALTER TABLE product ADD COLUMN uuid_id VARCHAR(36);
UPDATE product SET uuid_id = UUID();

ALTER TABLE product_details ADD COLUMN uuid_id VARCHAR(36);
UPDATE product_details SET uuid_id = UUID();
ALTER TABLE product_details ADD COLUMN product_uuid_id VARCHAR(36);
UPDATE product_details pd
JOIN product p ON pd.product_id = p.id
SET pd.product_uuid_id = p.uuid_id;
ALTER TABLE product_details DROP FOREIGN KEY product_details_ibfk_1;
ALTER TABLE product_details DROP COLUMN product_id;
ALTER TABLE product_details CHANGE COLUMN product_uuid_id product_id VARCHAR(36);
ALTER TABLE product_details MODIFY COLUMN product_id VARCHAR(36) NOT NULL;
CREATE INDEX idx_product_id ON product (uuid_id);
ALTER TABLE product_details
ADD CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES product(uuid_id);

ALTER TABLE product_image ADD COLUMN uuid_id VARCHAR(36);
UPDATE product_image SET uuid_id = UUID();
ALTER TABLE product_image ADD COLUMN product_detail_uuid_id VARCHAR(36);
UPDATE product_image pi
JOIN product_details pd ON pi.product_detail_id = pd.id
SET pi.product_detail_uuid_id = pd.uuid_id;
ALTER TABLE product_image DROP FOREIGN KEY product_image_ibfk_1;
ALTER TABLE product_image DROP COLUMN product_detail_id;
ALTER TABLE product_image CHANGE COLUMN product_detail_uuid_id product_detail_id VARCHAR(36);
ALTER TABLE product_image MODIFY COLUMN product_detail_id VARCHAR(36) NOT NULL;
CREATE INDEX idx_product_details_id_1 ON product_details (uuid_id);
ALTER TABLE product_image
ADD CONSTRAINT fk_product_detail_id FOREIGN KEY (product_detail_id) REFERENCES product_details(uuid_id);
ALTER TABLE product_image MODIFY COLUMN id BIGINT;
ALTER TABLE product_image DROP PRIMARY KEY;
ALTER TABLE product_image ADD PRIMARY KEY (uuid_id);
ALTER TABLE product_image DROP COLUMN id;
ALTER TABLE product_image CHANGE COLUMN uuid_id id VARCHAR(36) FIRST;
ALTER TABLE product_image MODIFY COLUMN id VARCHAR(36) NOT NULL;

ALTER TABLE order_table ADD COLUMN uuid_id VARCHAR(36);
UPDATE order_table SET uuid_id = UUID();

ALTER TABLE order_item ADD COLUMN uuid_id VARCHAR(36);
UPDATE order_item SET uuid_id = UUID();
ALTER TABLE order_item ADD COLUMN order_uuid_id VARCHAR(36);
ALTER TABLE order_item ADD COLUMN product_details_uuid_id VARCHAR(36);
UPDATE order_item oi
JOIN order_table o ON oi.order_id = o.id
SET oi.order_uuid_id = o.uuid_id;
ALTER TABLE order_item DROP FOREIGN KEY order_item_ibfk_1;
ALTER TABLE order_item DROP COLUMN order_id;
ALTER TABLE order_item CHANGE COLUMN order_uuid_id order_id VARCHAR(36);
ALTER TABLE order_item MODIFY COLUMN order_id VARCHAR(36);
CREATE INDEX idx_order_table_id ON order_table (uuid_id);
ALTER TABLE order_item
ADD CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES order_table(uuid_id);
UPDATE order_item oi
JOIN product_details pd ON oi.product_details_id = pd.id
SET oi.product_details_uuid_id = pd.uuid_id;
ALTER TABLE order_item DROP FOREIGN KEY order_item_ibfk_2;
ALTER TABLE order_item DROP COLUMN product_details_id;
ALTER TABLE order_item CHANGE COLUMN product_details_uuid_id product_details_id VARCHAR(36);
ALTER TABLE order_item MODIFY COLUMN product_details_id VARCHAR(36);
CREATE INDEX idx_product_details_id_2 ON product_details (uuid_id);
ALTER TABLE order_item
ADD CONSTRAINT fk_product_details_id FOREIGN KEY (product_details_id) REFERENCES product_details(uuid_id);
ALTER TABLE order_item MODIFY COLUMN id BIGINT;
ALTER TABLE order_item DROP PRIMARY KEY;
ALTER TABLE order_item ADD PRIMARY KEY (uuid_id);
ALTER TABLE order_item DROP COLUMN id;
ALTER TABLE order_item CHANGE COLUMN uuid_id id VARCHAR(36) FIRST;
ALTER TABLE order_item MODIFY COLUMN id VARCHAR(36) NOT NULL;

ALTER TABLE order_table MODIFY COLUMN id BIGINT;
ALTER TABLE order_table DROP PRIMARY KEY;
ALTER TABLE order_table ADD PRIMARY KEY (uuid_id);
ALTER TABLE order_table DROP COLUMN id;
ALTER TABLE order_table CHANGE COLUMN uuid_id id VARCHAR(36) FIRST;
ALTER TABLE order_table MODIFY COLUMN id VARCHAR(36) NOT NULL;

ALTER TABLE product_details MODIFY COLUMN id BIGINT;
ALTER TABLE product_details DROP PRIMARY KEY;
ALTER TABLE product_details ADD PRIMARY KEY (uuid_id);
ALTER TABLE product_details DROP COLUMN id;
ALTER TABLE product_details CHANGE COLUMN uuid_id id VARCHAR(36) FIRST;
ALTER TABLE product_details MODIFY COLUMN id VARCHAR(36) NOT NULL;

ALTER TABLE product MODIFY COLUMN id BIGINT;
ALTER TABLE product DROP PRIMARY KEY;
ALTER TABLE product ADD PRIMARY KEY (uuid_id);
ALTER TABLE product DROP COLUMN id;
ALTER TABLE product CHANGE COLUMN uuid_id id VARCHAR(36) FIRST;
ALTER TABLE product MODIFY COLUMN id VARCHAR(36) NOT NULL;

ALTER TABLE newsletter ADD COLUMN uuid_id VARCHAR(36);
UPDATE newsletter SET uuid_id = UUID();
ALTER TABLE newsletter MODIFY COLUMN id BIGINT;
ALTER TABLE newsletter DROP PRIMARY KEY;
ALTER TABLE newsletter ADD PRIMARY KEY (uuid_id);
ALTER TABLE newsletter DROP COLUMN id;
ALTER TABLE newsletter CHANGE COLUMN uuid_id id VARCHAR(36) FIRST;
ALTER TABLE newsletter MODIFY COLUMN id VARCHAR(36) NOT NULL;
