-- 課題09 模範解答: トランザクション（BEGIN / COMMIT / ROLLBACK）
-- ※ 操作手順そのものが解答。1つの psql セッションで上から実行する。
-- 実行前提: schema.sql を流した初期状態。

-- ===== 実験A: ROLLBACK で変更が消える =====
SELECT id, name, stock FROM products WHERE id = 1;   -- 100

BEGIN;
UPDATE products SET stock = 0 WHERE id = 1;
SELECT id, name, stock FROM products WHERE id = 1;   -- このセッション内では 0 に見える
ROLLBACK;
SELECT id, name, stock FROM products WHERE id = 1;   -- 100 に戻る（変更が消えた）

-- ===== 実験B: COMMIT で変更が残る =====
BEGIN;
UPDATE products SET stock = 999 WHERE id = 2;
COMMIT;
SELECT id, name, stock FROM products WHERE id = 2;   -- 999 のまま（確定した）

-- まとめ確認
SELECT id, name, stock FROM products WHERE id IN (1, 2) ORDER BY id;
-- 期待: id=1 りんご 100 / id=2 バナナ 999

-- 後始末: 次の課題のため初期状態に戻す
--   psql -h localhost -U postgres -d training -f schema.sql
