CREATE TABLE tb_products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    price DECIMAL(10, 2),
    image_url VARCHAR(255),
    stock INT
);