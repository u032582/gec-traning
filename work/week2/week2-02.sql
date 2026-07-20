--1.単価が1000円以上の商品の全列
SELECT * FROM products WHERE price >= 1000;

--2.カテゴリのidが2の商品と単価
SELECT name, price FROM products WHERE category_id = 2;

--3.全商品を単価の高い順に並べ替える
SELECT name, price FROM products ORDER BY price DESC;

--4.単価の安い順に上位3つ
SELECT name, price FROM products ORDER BY price LIMIT 3;