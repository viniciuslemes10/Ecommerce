ALTER TABLE tb_products
ADD COLUMN category_id INTEGER;

ALTER TABLE tb_products
ADD CONSTRAINT fk_product_category
FOREIGN KEY (category_id)
REFERENCES tb_categories(id);