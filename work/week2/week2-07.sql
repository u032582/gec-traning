--1.商品の単価を250から280に変更・WHERE忘れたら戦犯
UPDATE products SET price = 280 WHERE id = 4;

--2.飲料カテゴリの商品すべての在庫に＋50する
UPDATE products SET stock = stock + 50 WHERE category_id = 2;

--3.注文番号1008の状態をstatusからcancelledに変更
UPDATE orders SET status = 'cancelled' WHERE id = 1008;