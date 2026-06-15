-- 課題02 模範解答: WHERE / ORDER BY / LIMIT
-- ※ 先に自分で書いてから見ること。

-- 問1: 単価1000円以上の商品の全列（5行）
SELECT * FROM products WHERE price >= 1000;

-- 問2: 飲料カテゴリ(category_id=2)の商品名と単価（3行）
SELECT name, price FROM products WHERE category_id = 2;

-- 問3: 全商品を単価の高い順（降順）に（12行）
SELECT name, price FROM products ORDER BY price DESC;

-- 問4: 単価の安い順トップ3（3行）
SELECT name, price FROM products ORDER BY price ASC LIMIT 3;
