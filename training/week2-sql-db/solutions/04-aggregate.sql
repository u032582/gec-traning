-- 課題04 模範解答: GROUP BY / COUNT / SUM / AVG / HAVING
-- ※ 先に自分で書いてから見ること。

-- 問1: カテゴリごとの商品数（カテゴリ名つき・カテゴリ番号順）（5行）
SELECT categories.name, COUNT(*)
FROM products
JOIN categories ON products.category_id = categories.id
GROUP BY categories.id, categories.name
ORDER BY categories.id;

-- 問2: 注文ごとの売上合計（注文番号順）（10行）
SELECT order_id, SUM(quantity * unit_price)
FROM order_items
GROUP BY order_id
ORDER BY order_id;

-- 問3: カテゴリごとの平均単価（四捨五入して整数・カテゴリ番号順）（5行）
SELECT category_id, ROUND(AVG(price))
FROM products
GROUP BY category_id
ORDER BY category_id;

-- 問4: 商品が3個以上のカテゴリ（2行）
SELECT category_id, COUNT(*)
FROM products
GROUP BY category_id
HAVING COUNT(*) >= 3;
