--1.categoriesに新しいカテゴリを1件追加
INSERT INTO categories (name,id) VALUES('文房具',6);

--2.produstsに新しい商品を1件追加
INSERT INTO products (id, name, category_id, price, stock) 
VALUES (13, '消しゴム', 6, 80, 200);

--3.costomersに新しい顧客を1件追加
INSERT INTO customers(id, name, email, prefecture,registered_at) 
VALUES (7, '山本 涼', 'yamamoto@example.com', '北海道', '2025-01-20');