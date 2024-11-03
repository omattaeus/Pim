CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS product_movement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movement_date DATETIME NOT NULL COMMENT 'Movement date and time',
    type VARCHAR(20) NOT NULL COMMENT 'Type of movement (Entry or Exit)',
    quantity INT NOT NULL COMMENT 'Quantity moved',
    purpose VARCHAR(50) NOT NULL COMMENT 'Purpose of movement (Purchase, Sale, or Production)',
    product_id BIGINT NOT NULL COMMENT 'ID of the associated product',
    updated_at DATETIME DEFAULT NULL COMMENT 'Last update timestamp',
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci