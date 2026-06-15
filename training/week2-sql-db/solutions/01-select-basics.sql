-- 課題01 模範解答: SELECT の基礎
-- ※ 先に自分で書いてから見ること。

-- 問1: 全行・全列
SELECT * FROM products;

-- 問2: 商品名と単価の2列だけ
SELECT name, price FROM products;

-- 問3: 商品名だけ、見出しを「商品名」にする
SELECT name AS 商品名 FROM products;
