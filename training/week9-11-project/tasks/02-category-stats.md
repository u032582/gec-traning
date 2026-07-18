# タスク2: カテゴリ別集計API（GET /api/books/stats/by-category）

**難易度**: ★★☆（集計SQL + 新しいレスポンス形。既存は壊しにくい）
**AI利用区分**: ✅ 全面解禁。ただし説明責任を伴う。

---

## 1. 背景（なぜこの機能が要るか）

蔵書を「分類（category）ごとに、何冊あって、いま何冊貸出可能か」を集計して見たい、という要望です。運用者が在庫の偏りを把握するのに使います。既存の書籍一覧（`GET /api/books`）は1冊ずつ返すだけで、集計はしていません。

---

## 2. 仕様（受け入れ基準）

### エンドポイント
```
GET /api/books/stats/by-category
```

### 返すもの
分類ごとに集計した配列。各要素は次の形（新しいDTOを作る）:
```json
[
  {"category":"技術書","titleCount":3,"totalCount":6,"availableCount":4},
  {"category":"小説","titleCount":2,"totalCount":3,"availableCount":3},
  ...
]
```
- `titleCount` … その分類の**書名の数**（books テーブルの行数）
- `totalCount` … その分類の蔵書数の合計（`total_count` の合計）
- `availableCount` … その分類の貸出可能数の合計（`available_count` の合計）
- 並び順は分類名の昇順が望ましい。

### 受け入れ基準
- [ ] `GET /api/books/stats/by-category` が 200 で、分類ごとの集計を返す
- [ ] 集計の数字が、`books` テーブルの実データと合っている
- [ ] 新しいレスポンスDTO（例: `CategoryStatsResponse`）を作り、エンティティを直接返していない
- [ ] 単体テスト（Service層）を足し、`./gradlew test` が**全緑**
- [ ] 既存のテストを壊していない

---

## 3. 着地点のヒント（週6の読解を使う）

`book` パッケージに足します。集計は**SQL側でやる**のが素直です（Javaで全件取って集計するより、DBに `GROUP BY` させる）。

- **Mapper**: `BookMapper` に集計メソッドを1つ足す。XMLに次のようなSQL:
  ```sql
  SELECT category,
         COUNT(*)              AS title_count,
         SUM(total_count)      AS total_count,
         SUM(available_count)  AS available_count
  FROM books
  GROUP BY category
  ORDER BY category
  ```
  受け取る型は、集計用の入れ物（新しいクラス）にマッピングする。
- **Service**: `BookService` に集計を呼ぶメソッドを足す。
- **Controller**: `BookController` に `GET /api/books/stats/by-category` を足す。

> パス設計に注意: `GET /api/books/{id}` が既にあります。`/api/books/stats/by-category` が `{id}` と衝突しないか（`stats` が id として解釈されないか）を確認してください。これは**既存ルーティングとの影響範囲**の一種です。

---

## 4. 動作確認（自分でやってから、PRを出す）

```bash
curl http://localhost:8080/api/books/stats/by-category
```
`db/schema.sql` の書籍データ（技術書3件・小説2件・教養1件…）と、返ってきた集計が合うか、**自分で手計算して突き合わせて**ください。例えば技術書の `total_count` の合計は、schema のデータから自分で足せます。

---

## 5. レビュー観点（出す前に自分でも確認）

- **集計をどこでやっているか**: SQLの `GROUP BY` でやっているか（Javaで全件ループして集計していないか＝データが増えると重くなる）。
- **数字が正しいか**: `titleCount`（行数）と `totalCount`（合計）を取り違えていないか。
- **DTOを分けているか**（週3の作法）: エンティティ Book をそのまま返さず、集計専用のレスポンスにしているか。
- **パスの衝突**: `/api/books/{id}` と `/api/books/stats/by-category` が競合していないか。
- **テストの意味**（週8）: 集計結果の**具体的な数字**を検証しているか（呼ぶだけになっていないか）。

---

## 6. このタスクの提出物

- 実装（ブランチ + コミット） / PR（やったこと・なぜこの実装か・動作確認・影響範囲） / レビュー往復の記録

> 進め方・レビューの往復のコツは [週9〜11 README](../README.md) を参照。
