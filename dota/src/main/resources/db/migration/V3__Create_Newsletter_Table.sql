CREATE TABLE newsletter (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    uuid CHAR(36) NOT NULL
);