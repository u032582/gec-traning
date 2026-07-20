--1.カテゴリごとに表示する商品の数・カテゴリ名と商品数の2列
SELECT categories.name, COUNT(*) 
FROM products 
JOIN categories 
ON products.category_id = categories.id 
GROUP BY categories.name,categories.id
ORDER BY categories.id;

--2.注文ごとの売上合計金額を表示・注文番号と合計金額の2列
SELECT order_id,SUM(quantity*unit_price) 
FROM order_items 
GROUP BY order_id 
ORDER BY order_id;

--3.カテゴリごとの平均単価を表示、但し平均単価は四捨五入して整数・カテゴリ番号と平均単価の2列
SELECT category_id,ROUND(AVG(price)) 
FROM products 
GROUP BY category_id 
ORDER BY category_id;

--4.所属する商品が3個以上あるカテゴリのカテゴリ番号と商品数を表示
SELECT category_id,COUNT(*) 
FROM products 
GROUP BY category_id 
HAVING COUNT(*) >= 3 ;