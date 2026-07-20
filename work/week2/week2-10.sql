--orders_fratの問題点３つ
--重複・矛盾・表現できないデータ

--JOINすると元の表に戻る
SELECT
    o.id              AS order_id,
    c.name            AS customer_name,
    c.email           AS customer_email,
    p.name            AS product_name,
    cat.name          AS category_name,
    oi.quantity,
    oi.unit_price
FROM orders o
JOIN customers  c   ON o.customer_id = c.id
JOIN order_items oi ON oi.order_id    = o.id
JOIN products   p   ON oi.product_id  = p.id
JOIN categories cat ON p.category_id  = cat.id
ORDER BY o.id, oi.id;