-- 課題08 模範解答: DELETE
-- ※ 先に自分で書いてから見ること。
-- 実行前提: schema.sql を流した初期状態。問1 → 問2 の順で（外部キーのため）。

-- 問1: 注文1005の明細を削除（子テーブルが先）（DELETE 1）
DELETE FROM order_items WHERE order_id = 1005;

-- 問2: 注文1005本体を削除（親テーブルは子を消した後）（DELETE 1）
DELETE FROM orders WHERE id = 1005;

-- 問3: 一度も注文されていない商品「洗剤」(id=7)を削除（DELETE 1）
DELETE FROM products WHERE id = 7;
