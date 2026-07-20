--1.注文1005の明細を削除.WHERE忘れたら大戦犯
DELETE FROM order_items WHERE order_id = 1005;

--2.注文1005本体を削除
DELETE FROM orders WHERE id = 1005;

--3.商品（洗剤）だけ削除
DELETE FROM products WHERE id = 7;