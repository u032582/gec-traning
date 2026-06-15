-- =====================================================================
-- 総合課題「タスク管理API」のテーブル定義
-- ---------------------------------------------------------------------
-- 対象DB : training（週0で作成済み）
-- 流し方 : psql -h localhost -U postgres -d training -f db/schema.sql
--          パスワードを聞かれたら、週0で設定したものを入力する。
--
-- 何度流しても同じ状態になるよう、先に DROP してから作り直す。
-- 動作確認しやすいよう、サンプルを3件入れている（不要なら消してよい）。
-- =====================================================================

DROP TABLE IF EXISTS tasks;

CREATE TABLE tasks (
    -- id : 主キー。BIGSERIAL で自動採番（1, 2, 3 …）。
    id          BIGSERIAL PRIMARY KEY,
    -- title : タスク名。必須・100文字以内。
    title       VARCHAR(100) NOT NULL,
    -- description : 説明。任意・500文字以内。
    description VARCHAR(500),
    -- status : 状態。'todo'（未着手）/ 'doing'（作業中）/ 'done'（完了）のみ許可。
    --          CHECK制約でDB側でも値を縛っておく（アプリのバリデーションと二重の守り）。
    status      VARCHAR(10)  NOT NULL CHECK (status IN ('todo', 'doing', 'done')),
    -- due_date : 締め切り日。任意。
    due_date    DATE,
    -- created_at / updated_at : 作成・更新日時。登録時は現在時刻が自動で入る。
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 動作確認用のサンプルデータ（任意）
INSERT INTO tasks (title, description, status, due_date) VALUES
    ('研修の単体テストを書く', 'PriceCalculatorのテストを完成させる', 'doing', '2026-06-20'),
    ('総合課題のAPIを実装する', 'タスク管理APIをCRUDで作る',          'todo',  '2026-06-25'),
    ('環境構築を完了する',     'JDK21とPostgreSQLのセットアップ',     'done',  NULL);
