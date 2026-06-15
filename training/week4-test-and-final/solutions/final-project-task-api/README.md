# 総合課題 参考実装: タスク管理API

このフォルダは、総合課題（[final-project-spec.md](../../final-project-spec.md)）の**参考実装**です。
**まず自分で実装してから**見比べてください。先に答えを写すと身につきません。

> この参考実装は「唯一の正解」ではありません。設計どおりに動き、テストが緑になっていれば、書き方の細部が違っても正解です。

---

## 構成（3層構造 + 付随）

```
src/main/java/com/example/training/task/
  TaskApiApplication.java     起動クラス（@MapperScan でMapperを認識）
  Task.java                   tasksテーブルの入れ物（エンティティ）
  TaskRequest.java            作成/更新リクエストのDTO（@Valid の入力チェック付き）
  TaskMapper.java             DBアクセス役（インタフェース）。SQLはXMLに
  TaskService.java            判断・処理役。存在チェックや業務ルール
  TaskController.java         窓口役。URL→Serviceの振り分け
  TaskNotFoundException.java  見つからないとき投げる例外（→404）
  GlobalExceptionHandler.java 例外をHTTPレスポンスに変換（404 / 400）
src/main/resources/
  application.yml             DB接続とMyBatisの設定
  mapper/TaskMapper.xml       実際のSQL
db/
  schema.sql                  tasksテーブル定義 + サンプルデータ
src/test/java/com/example/training/task/
  TaskServiceTest.java        TaskServiceの単体テスト（Mockito、DB不要）
```

---

## 実行方法

### 1. 単体テスト（DB不要）
```bash
./gradlew test
```
`TaskServiceTest` が Mockito で完結するので、PostgreSQL が起動していなくても通ります。

### 2. アプリを起動して動かす（要 PostgreSQL）
```bash
# 事前にテーブルを作る（週0で作った training DB に対して）
psql -h localhost -U postgres -d training -f db/schema.sql

# application.yml の password を自分の設定に書き換えてから起動
./gradlew bootRun
```
起動後、`http://localhost:8080/api/tasks` にアクセスできます。検証用のcurl例は [final-project-spec.md](../../final-project-spec.md) の「検証方法」を参照。

---

## 設計の意図（説明できるようにしておくと良いポイント）

- **DTOとエンティティを分けた理由**: `TaskRequest`（JSON受け取り用・入力チェック付き）と `Task`（DB行）を分けることで、「APIの形」と「テーブルの形」を独立して変えられる。
- **存在チェックをServiceに集約した理由**: 「無ければ404」という業務ルールを Controller ではなく Service に置くことで、どこから呼ばれても同じルールが効く。
- **例外を GlobalExceptionHandler に集約した理由**: 各 Controller に try-catch を書かずに済み、エラーレスポンスの形を1か所で揃えられる。
- **テストがDB不要な理由**: `TaskMapper` をモックにしているので、Serviceの判断ロジックだけを高速に・安定して確認できる。
