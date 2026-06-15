-- 課題06 模範解答: INSERT
-- ※ 先に自分で書いてから見ること。
-- 実行前提: schema.sql を流した初期状態。問1 → 問2 の順で（外部キーのため）。

-- 問1: カテゴリ「文房具」(id=6) を追加
INSERT INTO categories (id, name) VALUES (6, '文房具');

-- 問2: 商品「消しゴム」(id=13, category_id=6) を追加
INSERT INTO products (id, name, category_id, price, stock)
VALUES (13, '消しゴム', 6, 80, 200);

-- 問3: 顧客「山本 涼」(id=7) を追加
INSERT INTO customers (id, name, email, prefecture, registered_at)
VALUES (7, '山本 涼', 'yamamoto@example.com', '北海道', '2025-01-20');
