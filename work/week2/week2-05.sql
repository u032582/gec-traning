--1.顧客番号4より前に登録した顧客の氏名と登録日を表示
SELECT name,registered_at 
FROM customers 
WHERE registered_at < (SELECT registered_at FROM customers WHERE id = 4 );

--2.1度目でも注文された商品の商品名を表示
SELECT name FROM products WHERE id IN(SELECT product_id FROM order_items);

--3.1度も注文されていない商品の商品名を表示
SELECT name FROM products WHERE id NOT IN(SELECT product_id FROM order_items);

--4.東京都の顧客が注文した注文番号を表示
SELECT id FROM orders 
WHERE customer_id IN(SELECT id FROM customers WHERE prefecture = '東京都');