-- =====================================================================
-- 週2「SQLとDBアクセス」演習用スキーマ + サンプルデータ
-- =====================================================================
-- 対象DB : training（週0で作成済み）
-- 対象RDB: PostgreSQL 16
-- 流し方 : ターミナルで次を実行する（postgres は管理ユーザー）
--           psql -h localhost -U postgres -d training -f schema.sql
--          パスワードを聞かれたら、自分が週0で設定したものを入力する。
--
-- このファイルは「何度流しても同じ状態になる」ように作ってある。
-- 冒頭で DROP TABLE IF EXISTS により既存テーブルを消してから作り直すので、
-- 演習中にデータを書き換えてしまっても、このファイルを流し直せば元に戻る。
--
-- ドメイン（題材）はECサイト（ネット通販）。
--   categories  : 商品カテゴリ（食品・飲料 など）
--   products    : 商品（どのカテゴリに属するか・価格・在庫）
--   customers   : 顧客（会員）
--   orders      : 注文（誰が・いつ・どんな状態の注文をしたか）
--   order_items : 注文明細（1注文の中で、どの商品を何個 買ったか）
-- =====================================================================

-- ---------------------------------------------------------------------
-- 後始末: 依存関係（外部キー）の逆順で消す。
-- order_items が orders / products を参照しているので、子から先に消す。
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS categories;

-- ---------------------------------------------------------------------
-- categories: 商品カテゴリ
--   id   : カテゴリ番号（主キー = この表の中で重複しない目印）
--   name : カテゴリ名
-- ---------------------------------------------------------------------
CREATE TABLE categories (
    id   INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO categories (id, name) VALUES
    (1, '食品'),
    (2, '飲料'),
    (3, '日用品'),
    (4, '家電'),
    (5, '書籍');

-- ---------------------------------------------------------------------
-- products: 商品
--   id          : 商品番号（主キー）
--   name        : 商品名
--   category_id : どのカテゴリに属するか（categories.id を指す = 外部キー）
--   price       : 単価（円）
--   stock       : 在庫数
-- ---------------------------------------------------------------------
CREATE TABLE products (
    id          INT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    category_id INT NOT NULL REFERENCES categories(id),
    price       INT NOT NULL,
    stock       INT NOT NULL
);

INSERT INTO products (id, name, category_id, price, stock) VALUES
    ( 1, 'りんご',             1,    150, 100),
    ( 2, 'バナナ',             1,    100,  50),
    ( 3, '緑茶',               2,    130, 200),
    ( 4, 'コーヒー',           2,    250,  80),
    ( 5, 'ミネラルウォーター', 2,     90, 300),
    ( 6, 'ティッシュ',         3,    300,  40),
    ( 7, '洗剤',               3,    400,   0),
    ( 8, 'ドライヤー',         4,   3500,  15),
    ( 9, '電気ケトル',         4,   4200,   8),
    (10, 'SQL入門書',          5,   2800,  25),
    (11, 'Java入門書',         5,   3200,  30),
    (12, 'ノートPC',           4,  98000,   5);

-- ---------------------------------------------------------------------
-- customers: 顧客（会員）
--   id           : 顧客番号（主キー）
--   name         : 氏名
--   email        : メールアドレス（重複させない = UNIQUE）
--   prefecture   : 都道府県（住所の都道府県だけ持たせている）
--   registered_at: 会員登録日
-- ---------------------------------------------------------------------
CREATE TABLE customers (
    id            INT PRIMARY KEY,
    name          VARCHAR(50)  NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    prefecture    VARCHAR(20)  NOT NULL,
    registered_at DATE         NOT NULL
);

INSERT INTO customers (id, name, email, prefecture, registered_at) VALUES
    (1, '佐藤 太郎',  'sato@example.com',     '東京都',   '2023-01-15'),
    (2, '鈴木 花子',  'suzuki@example.com',   '大阪府',   '2023-03-22'),
    (3, '高橋 一郎',  'takahashi@example.com', '東京都',   '2023-05-10'),
    (4, '田中 美咲',  'tanaka@example.com',   '愛知県',   '2024-02-01'),
    (5, '渡辺 健',    'watanabe@example.com', '福岡県',   '2024-06-18'),
    (6, '伊藤 さくら', 'ito@example.com',      '東京都',   '2024-11-30');

-- ---------------------------------------------------------------------
-- orders: 注文
--   id          : 注文番号（主キー）
--   customer_id : 誰の注文か（customers.id を指す = 外部キー）
--   ordered_at  : 注文日
--   status      : 注文の状態（'completed'=完了 / 'shipped'=発送済 / 'pending'=処理中 / 'cancelled'=キャンセル）
-- ---------------------------------------------------------------------
CREATE TABLE orders (
    id          INT PRIMARY KEY,
    customer_id INT NOT NULL REFERENCES customers(id),
    ordered_at  DATE NOT NULL,
    status      VARCHAR(20) NOT NULL
);

INSERT INTO orders (id, customer_id, ordered_at, status) VALUES
    (1001, 1, '2024-01-10', 'completed'),
    (1002, 1, '2024-03-05', 'completed'),
    (1003, 2, '2024-03-20', 'completed'),
    (1004, 3, '2024-04-01', 'shipped'),
    (1005, 2, '2024-05-12', 'cancelled'),
    (1006, 4, '2024-06-08', 'completed'),
    (1007, 1, '2024-07-19', 'shipped'),
    (1008, 5, '2024-08-25', 'pending'),
    (1009, 3, '2024-09-30', 'completed'),
    (1010, 4, '2024-10-14', 'completed');
-- 注意: 顧客6（伊藤 さくら）は登録だけで、まだ1度も注文していない。
--       これは「注文のない顧客」を JOIN の練習で扱うために わざと用意している。

-- ---------------------------------------------------------------------
-- order_items: 注文明細（1注文の中の1商品ぶん）
--   id         : 明細番号（主キー）
--   order_id   : どの注文の明細か（orders.id を指す = 外部キー）
--   product_id : どの商品か（products.id を指す = 外部キー）
--   quantity   : 個数
--   unit_price : 注文時点の単価（円）
-- メモ: なぜ products.price があるのに unit_price を別に持つのか？
--   商品の価格は将来 値上げ・値下げされる。注文した「その時の値段」を
--   明細に固定して残すのが現場の定石。だから集計では unit_price を使う。
-- ---------------------------------------------------------------------
CREATE TABLE order_items (
    id         INT PRIMARY KEY,
    order_id   INT NOT NULL REFERENCES orders(id),
    product_id INT NOT NULL REFERENCES products(id),
    quantity   INT NOT NULL,
    unit_price INT NOT NULL
);

INSERT INTO order_items (id, order_id, product_id, quantity, unit_price) VALUES
    -- 注文1001（佐藤）: りんご3 + 緑茶2
    ( 1, 1001,  1, 3,  150),
    ( 2, 1001,  3, 2,  130),
    -- 注文1002（佐藤）: コーヒー1 + SQL入門書1
    ( 3, 1002,  4, 1,  250),
    ( 4, 1002, 10, 1, 2800),
    -- 注文1003（鈴木）: ティッシュ5
    ( 5, 1003,  6, 5,  300),
    -- 注文1004（高橋）: ドライヤー1 + ミネラルウォーター6
    ( 6, 1004,  8, 1, 3500),
    ( 7, 1004,  5, 6,   90),
    -- 注文1005（鈴木・キャンセル）: ノートPC1
    ( 8, 1005, 12, 1, 98000),
    -- 注文1006（田中）: Java入門書1 + SQL入門書1 + コーヒー2
    ( 9, 1006, 11, 1, 3200),
    (10, 1006, 10, 1, 2800),
    (11, 1006,  4, 2,  250),
    -- 注文1007（佐藤）: バナナ4
    (12, 1007,  2, 4,  100),
    -- 注文1008（渡辺・処理中）: 電気ケトル1
    (13, 1008,  9, 1, 4200),
    -- 注文1009（高橋）: 緑茶3 + りんご2
    (14, 1009,  3, 3,  130),
    (15, 1009,  1, 2,  150),
    -- 注文1010（田中）: ノートPC1 + ドライヤー1
    (16, 1010, 12, 1, 98000),
    (17, 1010,  8, 1, 3500);

-- =====================================================================
-- 投入確認用（任意）: 件数が下と一致すれば正しく入っている。
--   categories  : 5
--   products    : 12
--   customers   : 6
--   orders      : 10
--   order_items : 17
-- 確認コマンド例:
--   SELECT 'categories'  AS t, COUNT(*) FROM categories
--   UNION ALL SELECT 'products',    COUNT(*) FROM products
--   UNION ALL SELECT 'customers',   COUNT(*) FROM customers
--   UNION ALL SELECT 'orders',      COUNT(*) FROM orders
--   UNION ALL SELECT 'order_items', COUNT(*) FROM order_items;
-- =====================================================================
