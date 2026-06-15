# 総合課題 詳細設計書: タスク管理API

**所要時間の目安**: 2〜3日（実装 + テスト + PR提出）

**AI利用区分**: ✅ AIにコードを書かせてよい。ただし**生成したコードを自分の言葉で説明でき、なぜその実装かを言えること**が必須（記入欄は本書末尾）。

---

## 0. この課題のゴール

週3で学んだ「3層構造のREST API」と、週4前半で学んだ「単体テスト」を合わせ、**設計書を読んで、APIを実装し、テストを書き、Gitでプルリク（PR）提出するまでを一人で一気通貫**でやります。これが「現場の実装工程を一通り自走できる」状態です。

週3の雛形（starter-project）の作り方をベースにして構いません。題材（タスク管理）は週2/3のECサイトとは独立しています。

> 用語メモ: **CRUD（クラッド）** … Create（作成）/ Read（読む）/ Update（更新）/ Delete（削除）の4操作。データを扱うアプリの基本動作。

---

## 1. 機能一覧

| # | 機能 | メソッド | パス |
|---|---|---|---|
| 1 | タスク作成 | POST | `/api/tasks` |
| 2 | タスク一覧取得 | GET | `/api/tasks` |
| 3 | タスク単体取得 | GET | `/api/tasks/{id}` |
| 4 | タスク更新 | PUT | `/api/tasks/{id}` |
| 5 | タスク削除 | DELETE | `/api/tasks/{id}` |

---

## 2. テーブル定義（SQL）

DB は週0で作った `training`。下のSQLを流してテーブルを作ります（参考実装の `solutions/final-project-task-api/db/schema.sql` と同じ）。

> WSL2は再起動するとPostgreSQLが止まります。DBを使う前に `sudo service postgresql start` を実行してください（詳しくは週0参照）。これをしないと `Connection refused` で接続できません。

```sql
DROP TABLE IF EXISTS tasks;

CREATE TABLE tasks (
    id          BIGSERIAL PRIMARY KEY,                 -- 自動採番の主キー
    title       VARCHAR(100) NOT NULL,                 -- タイトル（必須・100文字以内）
    description VARCHAR(500),                           -- 説明（任意・500文字以内）
    status      VARCHAR(10)  NOT NULL
                CHECK (status IN ('todo', 'doing', 'done')), -- 状態（3種のみ）
    due_date    DATE,                                   -- 締め切り日（任意）
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

流し方:
```bash
psql -h localhost -U postgres -d training -f db/schema.sql
```

---

## 3. 各エンドポイントの詳細設計

JSONの日付は `"yyyy-MM-dd"` 形式の文字列。`status` は `todo` / `doing` / `done` のいずれか。

### 3-1. タスク作成: `POST /api/tasks`

**リクエストボディ（JSON）**
```json
{
  "title": "設計書を読む",
  "description": "総合課題の仕様を読む",
  "status": "todo",
  "dueDate": "2026-06-30"
}
```
| 項目 | 必須 | 制約 |
|---|---|---|
| title | ○ | 空白だけは不可・100文字以内 |
| description | × | 500文字以内 |
| status | ○ | `todo` / `doing` / `done` のいずれか |
| dueDate | × | `yyyy-MM-dd` 形式 |

**成功レスポンス**: `201 Created`、ヘッダ `Location: /api/tasks/{採番されたid}`、ボディは作成されたタスク全体。
```json
{
  "id": 4,
  "title": "設計書を読む",
  "description": "総合課題の仕様を読む",
  "status": "todo",
  "dueDate": "2026-06-30",
  "createdAt": "2026-06-15T10:00:00",
  "updatedAt": "2026-06-15T10:00:00"
}
```
**異常時**: 入力チェック違反 → `400 Bad Request`（後述のエラー形式）。

### 3-2. タスク一覧取得: `GET /api/tasks`

**成功レスポンス**: `200 OK`、タスクの配列（id昇順）。0件なら `[]`。
```json
[
  { "id": 1, "title": "...", "description": "...", "status": "doing", "dueDate": "2026-06-20", "createdAt": "...", "updatedAt": "..." },
  { "id": 2, "title": "...", "description": null, "status": "todo", "dueDate": null, "createdAt": "...", "updatedAt": "..." }
]
```

### 3-3. タスク単体取得: `GET /api/tasks/{id}`

**成功レスポンス**: `200 OK`、該当タスク1件。
**異常時**: 該当idが無い → `404 Not Found`。

### 3-4. タスク更新: `PUT /api/tasks/{id}`

**リクエストボディ**: 作成と同じ形（title/status は必須、description/dueDate は任意）。**全項目を上書き**する（部分更新ではない）。
**成功レスポンス**: `200 OK`、更新後のタスク全体（`updatedAt` が更新される）。
**異常時**: 該当idが無い → `404`。入力チェック違反 → `400`。

### 3-5. タスク削除: `DELETE /api/tasks/{id}`

**成功レスポンス**: `204 No Content`（ボディなし）。
**異常時**: 該当idが無い → `404`。

---

## 4. バリデーション（入力チェック）要件

`TaskRequest`（DTO）に Bean Validation の注釈を付けて実現します。

| 項目 | ルール | 違反時メッセージ例 |
|---|---|---|
| title | `@NotBlank` + `@Size(max=100)` | `titleは必須です` / `titleは100文字以内で入力してください` |
| description | `@Size(max=500)` | `descriptionは500文字以内で入力してください` |
| status | `@NotBlank` + `@Pattern(regexp="todo|doing|done")` | `statusは必須です` / `statusは todo / doing / done のいずれかです` |
| dueDate | 形式チェックのみ（型がLocalDateなので不正形式は自動で弾かれる） | — |

Controller の引数に `@Valid` を付けると、これらが自動で効きます。違反は `400` にする（`GlobalExceptionHandler` で変換）。

### エラーレスポンスの形（400 / 404 共通の枠）
```json
{
  "timestamp": "2026-06-15T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "入力内容に誤りがあります",
  "errors": { "title": "titleは必須です" }
}
```
（404のときは `errors` を省いてよい。`message` に「タスクが見つかりません: id=99」等。）

---

## 5. 必須の単体テスト観点（TaskServiceを対象に、Mockitoで）

`TaskMapper` をモック化し、`TaskService` の業務ルールを確認します（DB不要）。最低限、次を書くこと。

| # | 観点 | 確認すること |
|---|---|---|
| 1 | `findById` 正常 | 存在すればそのタスクを返す |
| 2 | `findById` 異常 | 存在しなければ `TaskNotFoundException` |
| 3 | `create` 正常 | insertが呼ばれ、採番後のタスクを返す（`verify`） |
| 4 | `update` 異常 | 対象が無ければ例外。`update` が呼ばれない（`verify(..., never())`） |
| 5 | `delete` 異常 | 削除件数0なら例外 |

> 余裕があれば `findAll` や `update` 正常系も追加するとなお良い。

---

## 6. 完成条件（自分で判定できる基準）

すべて満たせば完成です。

- [ ] テーブルを作成した（`psql ... -f db/schema.sql`）
- [ ] 5つのエンドポイントを実装した（3層構造: Controller / Service / Mapper）
- [ ] バリデーションが効く（不正入力で400が返る）
- [ ] 存在しないidで404が返る（GET単体 / PUT / DELETE）
- [ ] 「8. 検証方法」のcurlを順に叩くと、**設計どおりのステータス・JSONが返る**
- [ ] 単体テスト（必須観点5つ以上）を書き、`./gradlew test` が**緑**
- [ ] Gitでブランチを切り、コミットし、**PR（プルリク）が出ている**（手順は week4 README 参照）
- [ ] 本書末尾の「AI生成コードの説明記入欄」を埋めた

---

## 7. 推奨の進め方（つまずきにくい順番）

1. テーブルを作る → アプリを起動できる状態にする（週3雛形の設定をコピー）。
2. **GET一覧** から作る（一番簡単。動けば3層のつながりが確認できる）。
3. **GET単体**（404の練習）。
4. **POST作成**（バリデーションの練習）。
5. **PUT更新** → **DELETE削除**。
6. 各機能ができるたびに curl で確認し、こまめにコミット。
7. 最後に TaskService の単体テストを書く。

> 1機能できるたびにコミットするのがコツ。「全部できてから1回コミット」より、後から見て分かりやすく、戻しやすい。

---

## 8. 検証方法（curl例と期待レスポンス）

アプリ起動後（`./gradlew bootRun`）、別ターミナルで順に実行します。

```bash
# (1) 作成 → 201。Location ヘッダと作成結果が返る
curl -i -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"設計書を読む","description":"総合課題","status":"todo","dueDate":"2026-06-30"}'
```
期待: `HTTP/1.1 201`、ヘッダに `Location: /api/tasks/4` のような行、ボディに作成タスク（id付き）。

```bash
# (2) 一覧 → 200。配列で返る
curl -i http://localhost:8080/api/tasks
```
期待: `HTTP/1.1 200`、JSON配列。

```bash
# (3) 単体取得（存在するid）→ 200
curl -i http://localhost:8080/api/tasks/1
```
期待: `HTTP/1.1 200`、該当タスク1件。

```bash
# (4) 単体取得（存在しないid）→ 404
curl -i http://localhost:8080/api/tasks/9999
```
期待: `HTTP/1.1 404`、`"message":"タスクが見つかりません: id=9999"` を含むJSON。

```bash
# (5) 作成のバリデーション違反（titleが空）→ 400
curl -i -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"","status":"todo"}'
```
期待: `HTTP/1.1 400`、`"errors":{"title":"titleは必須です"}` を含むJSON。

```bash
# (6) 作成のバリデーション違反（statusが不正）→ 400
curl -i -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"x","status":"finished"}'
```
期待: `HTTP/1.1 400`、`"errors"` に status のメッセージ。

```bash
# (7) 更新 → 200。updatedAtが変わる
curl -i -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"更新後タイトル","status":"doing"}'
```
期待: `HTTP/1.1 200`、更新後タスク（statusがdoing）。

```bash
# (8) 削除 → 204（ボディなし）
curl -i -X DELETE http://localhost:8080/api/tasks/1
```
期待: `HTTP/1.1 204`。

```bash
# (9) 削除した後にもう一度取得 → 404
curl -i http://localhost:8080/api/tasks/1
```
期待: `HTTP/1.1 404`。

> これらの `curl` はWSL2のターミナル（Ubuntu / bash）で実行してください。行末の `\` での改行や `'...'` でのJSON指定は、bashではそのまま動きます。

---

## 9. AI生成コードの説明記入欄（提出時に必ず埋める）

```
【3層（Controller / Service / Mapper）それぞれの役割を自分の言葉で】


【なぜ存在チェックをServiceに書いたのか】


【@Valid と GlobalExceptionHandler の関係（不正入力がどう400になるか）】


【AIに書かせた部分があれば、その箇所と「なぜその実装か」】

```

---

## 10. 模範解答への導線

自分で完成させたら、`solutions/final-project-task-api/` を見比べてください。設計どおり動いていれば、書き方の細部が違っても正解です。模範解答の `README.md` には設計の意図も書いてあります。
