--1.商品名とカテゴリ名を表示・JOINの書き方
SELECT products.name,categories.name 
FROM products 
JOIN categories 
ON products.category_id = categories.id;

--2.注文番号・顧客名・注文日・状態を表示
SELECT orders.id,customers.name,orders.ordered_at,orders.status 
FROM orders 
JOIN customers 
ON orders.customer_id = customers.id;

--3.注文番号1006の明細の表品名・個数・注文時単価を表示
SELECT products.name,order_items.quantity,order_items.unit_price 
FROM order_items 
JOIN products 
ON order_items.product_id = products.id 
WHERE order_items.order_id = 1006;

--4.顧客名とその注文番号を表示　但し1度も注文していない人も表示（null)
SELECT customers.name,orders.id 
FROM customers 
LEFT JOIN orders 
ON orders.customer_id = customers.id;