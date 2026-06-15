-- 課題03 模範解答: JOIN
-- ※ 先に自分で書いてから見ること。

-- 問1: 商品名 + カテゴリ名（12行）
SELECT products.name AS 商品名, categories.name AS カテゴリ名
FROM products
JOIN categories ON products.category_id = categories.id;

-- 問2: 注文番号 + 顧客名 + 注文日 + 状態（10行）
SELECT orders.id, customers.name, orders.ordered_at, orders.status
FROM orders
JOIN customers ON orders.customer_id = customers.id;

-- 問3: 注文1006の明細（商品名・個数・注文時単価）（3行）
SELECT products.name, order_items.quantity, order_items.unit_price
FROM order_items
JOIN products ON order_items.product_id = products.id
WHERE order_items.order_id = 1006;

-- 問4: 全顧客と注文番号。注文ゼロの顧客も残す = LEFT JOIN（11行）
SELECT customers.name, orders.id
FROM customers
LEFT JOIN orders ON customers.id = orders.customer_id;
