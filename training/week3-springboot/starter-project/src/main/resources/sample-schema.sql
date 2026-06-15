-- =====================================================================
-- 週3 雛形プロジェクト用 フォールバックSQL（sample-schema.sql）
-- ---------------------------------------------------------------------
-- 【これは何？】
--   本来このアプリは「週2で作る schema.sql」を training DB に流した状態で動かします。
--   しかし、まだ週2のスキーマを投入していない／手元に無い場合でも、
--   このファイルを流せば最低限のテーブルとデータが入り、週3の課題を進められます。
--
-- 【流し方（ターミナルで1回だけ）】
--   psql -h localhost -U postgres -d training -f sample-schema.sql
--   ※ パスワードを聞かれたら、自分のPostgreSQLのパスワードを入力。
--
-- 【入るもの】
--   - customers（顧客）   … 雛形のサンプルAPIと課題3で使う
--   - categories（分類）   … 課題2で使う
--   - products（商品）     … 課題1・課題2で使う
--   何度流しても同じ状態になるように、毎回作り直す（DROP→CREATE）形にしてあります。
-- =====================================================================

-- ---- いったん消してから作り直す（順番に注意：参照されている側を後で消す）----
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS customers;

-- ---- 顧客テーブル ----
CREATE TABLE customers (
    customer_id BIGSERIAL PRIMARY KEY,          -- 顧客ID（自動で連番が振られる）
    name        VARCHAR(100) NOT NULL,          -- 氏名
    email       VARCHAR(255) NOT NULL UNIQUE,   -- メールアドレス（重複不可）
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 登録日時
);

-- ---- 商品分類テーブル ----
CREATE TABLE categories (
    category_id BIGSERIAL PRIMARY KEY,          -- カテゴリID
    name        VARCHAR(100) NOT NULL           -- カテゴリ名
);

-- ---- 商品テーブル ----
CREATE TABLE products (
    product_id  BIGSERIAL PRIMARY KEY,          -- 商品ID
    name        VARCHAR(200) NOT NULL,          -- 商品名
    price       INTEGER      NOT NULL,          -- 価格（円・税抜）
    category_id BIGINT       NOT NULL REFERENCES categories(category_id)  -- どの分類か
);

-- ---- 初期データ：顧客 ----
INSERT INTO customers (name, email) VALUES
    ('山田 太郎', 'taro.yamada@example.com'),
    ('佐藤 花子', 'hanako.sato@example.com'),
    ('鈴木 一郎', 'ichiro.suzuki@example.com');

-- ---- 初期データ：カテゴリ ----
INSERT INTO categories (name) VALUES
    ('書籍'),       -- category_id = 1
    ('家電'),       -- category_id = 2
    ('食品');       -- category_id = 3

-- ---- 初期データ：商品 ----
INSERT INTO products (name, price, category_id) VALUES
    ('Java入門書',        2800, 1),
    ('SQL実践ガイド',      3200, 1),
    ('ワイヤレスイヤホン', 8900, 2),
    ('USB充電器',         1500, 2),
    ('有機コーヒー豆',     1200, 3);
