-- =====================================================================
-- 題材リポ「図書貸出管理API」のテーブル定義
-- ---------------------------------------------------------------------
-- 対象DB : training（週0で作成済み）
-- 流し方 : psql -h localhost -U postgres -d training -f db/schema.sql
--          パスワードを聞かれたら、週0で設定したものを入力する。
--
-- 何度流しても同じ状態になるよう、先に DROP してから作り直す。
-- lendings が books / members を参照するので、DROP は依存の子から先に。
-- 動作確認しやすいよう、サンプルデータを入れている。
-- =====================================================================

DROP TABLE IF EXISTS lendings;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS members;

-- ---------------------------------------------------------------------
-- members : 利用者（本を借りる人）
-- ---------------------------------------------------------------------
CREATE TABLE members (
    id          BIGSERIAL PRIMARY KEY,
    -- name : 氏名。必須。
    name        VARCHAR(100)  NOT NULL,
    -- email : 会員を識別するメール。必須・重複不可。
    email       VARCHAR(255)  NOT NULL UNIQUE,
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- books : 蔵書（同じ本を複数冊持つことがある）
-- ---------------------------------------------------------------------
CREATE TABLE books (
    id               BIGSERIAL PRIMARY KEY,
    -- title / author / category : 書誌情報。すべて必須。
    title            VARCHAR(200) NOT NULL,
    author           VARCHAR(100) NOT NULL,
    category         VARCHAR(50)  NOT NULL,
    -- total_count     : 蔵書数（この本を全部で何冊持っているか）。
    total_count      INT          NOT NULL CHECK (total_count >= 0),
    -- available_count : いま貸出可能な残り冊数。貸出で減り、返却で増える。
    --                   「状態の整合」の要。0 なら貸せない。
    available_count  INT          NOT NULL CHECK (available_count >= 0),
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- lendings : 貸出記録（誰が・どの本を・いつ借りて・いつ返したか）
-- ---------------------------------------------------------------------
CREATE TABLE lendings (
    id           BIGSERIAL PRIMARY KEY,
    -- book_id / member_id : どの本を誰が借りたか。存在する行しか指せないようFK。
    book_id      BIGINT   NOT NULL REFERENCES books(id),
    member_id    BIGINT   NOT NULL REFERENCES members(id),
    -- lent_at  : 貸出日。 due_date : 返却期限。
    lent_at      DATE     NOT NULL,
    due_date     DATE     NOT NULL,
    -- returned_at : 返却日。NULL なら「まだ貸出中」を意味する。
    returned_at  DATE
);

-- =====================================================================
-- サンプルデータ（動作確認用）
-- =====================================================================

INSERT INTO members (name, email) VALUES
    ('田中 花子', 'hanako.tanaka@example.com'),
    ('佐藤 健',   'ken.sato@example.com'),
    ('鈴木 一郎', 'ichiro.suzuki@example.com');

-- available_count は「total から、いま貸出中の冊数を引いた値」を初期値にしている。
-- （下の lendings と辻褄が合うようにしてある）
INSERT INTO books (title, author, category, total_count, available_count) VALUES
    ('リーダブルコード',           'Dustin Boswell', '技術書', 3, 2),  -- 1冊貸出中
    ('達人プログラマー',           'Andrew Hunt',    '技術書', 2, 2),
    ('人月の神話',                 'Fred Brooks',    '技術書', 1, 0),  -- 唯一の1冊が貸出中＝在庫0
    ('ノルウェイの森',             '村上 春樹',      '小説',   2, 2),
    ('コンビニ人間',               '村田 沙耶香',    '小説',   1, 1),
    ('サピエンス全史',             'Yuval Harari',   '教養',   2, 1);  -- 1冊貸出中

-- 貸出記録（上の available_count と整合させてある）。
-- returned_at が NULL の行が「貸出中」。
INSERT INTO lendings (book_id, member_id, lent_at, due_date, returned_at) VALUES
    (1, 1, '2026-07-01', '2026-07-15', NULL),          -- 田中がリーダブルコードを貸出中
    (3, 2, '2026-06-20', '2026-07-04', NULL),          -- 佐藤が人月の神話を貸出中（在庫0の理由）
    (6, 1, '2026-07-05', '2026-07-19', NULL),          -- 田中がサピエンス全史を貸出中
    (4, 3, '2026-06-10', '2026-06-24', '2026-06-22');  -- 鈴木はノルウェイの森を返却済み
