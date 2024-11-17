DROP TABLE IF EXISTS Cars;

CREATE TABLE IF NOT EXISTS Cars (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(255),
    model VARCHAR(255),
    price DECIMAL,
    picture VARCHAR(255)
    );
