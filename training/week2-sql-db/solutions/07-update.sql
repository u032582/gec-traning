-- 課題07 模範解答: UPDATE
-- ※ 先に自分で書いてから見ること。
-- 実行前提: schema.sql を流した初期状態。

-- 問1: コーヒー(id=4)の単価を 280 に（UPDATE 1）
UPDATE products SET price = 280 WHERE id = 4;

-- 問2: 飲料(category_id=2)の在庫を +50（UPDATE 3）
UPDATE products SET stock = stock + 50 WHERE category_id = 2;

-- 問3: 注文1008の状態を cancelled に（UPDATE 1）
UPDATE orders SET status = 'cancelled' WHERE id = 1008;
