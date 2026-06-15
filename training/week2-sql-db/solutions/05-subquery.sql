-- 課題05 模範解答: サブクエリ
-- ※ 先に自分で書いてから見ること。

-- 問1: 顧客4より前に登録した顧客（3行）
SELECT name, registered_at
FROM customers
WHERE registered_at < (SELECT registered_at FROM customers WHERE id = 4);

-- 問2: 一度でも注文された商品（11行）
SELECT name
FROM products
WHERE id IN (SELECT product_id FROM order_items);

-- 問3: 一度も注文されたことがない商品（1行）
SELECT name
FROM products
WHERE id NOT IN (SELECT product_id FROM order_items);

-- 問4: 東京都の顧客がした注文の注文番号（5行）
SELECT id
FROM orders
WHERE customer_id IN (SELECT id FROM customers WHERE prefecture = '東京都');
