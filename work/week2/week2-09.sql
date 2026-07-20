--1.在庫を見る
SELECT id, name, stock FROM products WHERE id = 1;   -- りんごの在庫は 100 のはず

--2.トランザクション開始
BEGIN;

--3.りんご（id=1）の在庫を 100 → 0 に書き換える
UPDATE products SET stock = 0 WHERE id = 1;

--4.同じ画面でもう一度 在庫を見る（この時点では 0 に見える）
SELECT id, name, stock FROM products WHERE id = 1;   -- 0 になっている

--5.取り消し
ROLLBACK;

--6.もう一度 在庫を見る（100 に戻っている = 変更がなかったことになった）
SELECT id, name, stock FROM products WHERE id = 1;   -- 100 に戻る

--COMMITするほう

--1.トランザクション開始
BEGIN;

--2.バナナ（id=2）の在庫を 50 → 999 に書き換える
UPDATE products SET stock = 999 WHERE id = 2;

--3.確定する
COMMIT;

--4.在庫を見る（999 のまま = 確定した）
SELECT id, name, stock FROM products WHERE id = 2;   -- 999 のまま