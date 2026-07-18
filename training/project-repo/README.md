# 図書貸出管理API（題材リポジトリ）

これは、研修・第2フェーズ後半（週6〜11）で **「既存システム」として読み・直し・機能追加していく題材アプリ** です。
あなた（新人）がゼロから作るのではなく、**すでに動いているコードに参画する**ための土台です。現場で最初に任される動き——他人の書いたコードを読み、壊さず直し、機能を足す——を、この1本のリポジトリで通して練習します。

> 作り手向けの設計意図は [DESIGN.md](DESIGN.md) にあります（新人は読まなくてOK）。

---

## 1. このアプリは何をするか

図書館（社内文庫でもよい）の **本の貸し借り** を管理するAPIです。登場するのは3つ。

- **書籍（book）**: 蔵書。同じ本を複数冊持つことがある（`total_count`）。いま貸せる残り冊数が `available_count`。
- **利用者（member）**: 本を借りる人。
- **貸出（lending）**: 「誰が・どの本を・いつ借りて・いつ返したか」の記録。返却日が未記入なら「貸出中」。

**このアプリの正しさの核**は、状態の整合です。

> 本を1冊貸すと、貸出記録が1件増え、その本の `available_count` が1減る。
> 返すと、返却日が入り、`available_count` が1戻る。**この2つが噛み合っていること。**

---

## 2. 全体像（3層構造）

週3で学んだ **3層構造**（Controller → Service → Mapper）でできています。エンティティごとにパッケージが分かれています。

```
src/main/java/com/example/training/
  LibraryApplication.java     ← 起動クラス（main はここ）
  book/     書籍まわり     … Controller / Service / Mapper / エンティティ(Book) / DTO(BookResponse)
  member/   利用者まわり   … Service / Mapper / エンティティ(Member)
  lending/  貸出・返却     … Controller / Service / Mapper / エンティティ(Lending) / DTO(Request・Response)【このアプリの核】
  common/   共通           … 例外ハンドラ / NotFoundException(404) / BusinessRuleException(409)
src/main/resources/
  application.yml           ← DB接続などの設定
  mapper/*.xml              ← 実際のSQL（MapperインタフェースとXMLのidが対応）
db/
  schema.sql                ← テーブル定義 + サンプルデータ
src/test/java/...
  lending/LendingServiceTest.java  ← 貸出/返却の主要分岐のテスト（※わざと手薄。週8で埋める）
```

> **現在の整備状況**: book（書籍取得）・member（利用者）・lending（貸出・返却）・common（例外処理）まで**ひととおり動きます**。
> テストは lending の主要分岐だけの**最低限**（週8で AI と協働して埋める前提で、わざと手薄にしてあります）。

---

## 3. 動かし方

### 前提
- 週0で作った PostgreSQL の `training` データベースが使えること。
- Java 21 が使えること（週0で用意済み）。

> WSL2は再起動するとPostgreSQLが止まります。使う前に `sudo service postgresql start` を実行してください。

### 手順1: テーブルとサンプルデータを入れる
```bash
psql -h localhost -U postgres -d training -f db/schema.sql
```
> パスワードを聞かれたら、週0で自分が設定したものを入力します。
> 何度流しても同じ状態になります（先にDROPしてから作り直す作り）。

### 手順2: DBパスワードを自分のものに直す
`src/main/resources/application.yml` の `password:` を、週0で設定した自分のパスワードに書き換えます
（`postgres` にした人はそのままでOK）。

### 手順3: アプリを起動する
```bash
./gradlew bootRun
```
次の行が出れば起動成功です（ポート8080で待ち受け）。
```
Tomcat started on port 8080 (http) with context path '/'
Started LibraryApplication in 1.x seconds ...
```
止めるときは **Ctrl + C**。

### 手順4: 動作確認（別ターミナルで）
```bash
# 書籍一覧
curl http://localhost:8080/api/books

# 分類で絞り込み
curl "http://localhost:8080/api/books?category=技術書"

# 1件取得
curl http://localhost:8080/api/books/1

# 存在しないID → 404 が返る
curl -i http://localhost:8080/api/books/999
```

一覧なら、次のようなJSONが返ります（`available_count` に注目——貸出中のぶん減っています）。
```json
[
  {"id":1,"title":"リーダブルコード","author":"Dustin Boswell","category":"技術書","totalCount":3,"availableCount":2},
  ...
]
```

貸出・返却も試せます（**在庫の増減に注目**——このアプリの核です）。
```bash
# 本を借りる（book 5 を member 1 が）。成功すると201で貸出記録が返る。
curl -i -X POST http://localhost:8080/api/lendings \
     -H 'Content-Type: application/json' \
     -d '{"bookId":5,"memberId":1}'
# → この直後に curl http://localhost:8080/api/books/5 を見ると availableCount が1減っている

# 在庫0の本（id=3 は貸出中で在庫0）を借りようとする → 409 が返る
curl -i -X POST http://localhost:8080/api/lendings \
     -H 'Content-Type: application/json' -d '{"bookId":3,"memberId":2}'

# 返す（上で作られた貸出のidを {id} に入れる） → 在庫が1戻る
curl -i -X POST http://localhost:8080/api/lendings/{id}/return

# 同じものをもう一度返そうとする → 409（二重返却）
curl -i -X POST http://localhost:8080/api/lendings/{id}/return

# ある利用者の貸出履歴
curl http://localhost:8080/api/members/1/lendings
```

---

## 4. 読み方のヒント（週6でやること）

初見のリポは「入口から処理を追う」のが基本です。書籍一覧 `GET /api/books` を例に:

1. `BookController#list` … URLを受けて Service を呼ぶ（窓口）
2. `BookService#findAll` … Mapper を呼ぶ（判断・処理）
3. `BookMapper#findAll` ↔ `mapper/BookMapper.xml` の `<select id="findAll">` … SQLを実行（DB出し入れ）
4. 結果の `Book` を `BookResponse` に詰め替えて返す

この「Controller → Service → Mapper → XML」の流れを、ファイル名と行で追えるようになるのが週6の目標です。

---

## 5. ビルド・テスト

```bash
./gradlew build    # テスト込みでビルド
./gradlew test     # 単体テストだけ実行（DB不要。Mockitoで完結）
```

> 週3/週4と同じGradleとMyBatis構成なので、コマンドの使い勝手はこれまでと同じです。
