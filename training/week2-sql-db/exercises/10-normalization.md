# 課題10: 正規化の基礎 — なぜ表を分けるのか

- **所要時間の目安**: 50分
- **AI利用区分**: 🚫 AIにSQLを書かせるのは禁止。家庭教師用途のみ（エラーの意味/レビュー/概念説明）。

> 用語メモ: **正規化（せいきか）** … データの重複や矛盾を防ぐために、1つの大きな表を「意味のまとまりごと」に複数の表へ分ける作業のこと。これまで使ってきた `customers` / `orders` / `order_items` / `products` が分かれているのも、正規化の結果です。

これは座学（考える）と手を動かす作業を組み合わせた課題です。SQLを大量に書くというより、「**なぜ表を分けるのか**」を腑に落とすのが目的です。

---

## 背景・狙い（現場でどう使うか）

設計済みのDBを触るだけなら正規化を知らなくても動きます。しかし「なぜこの表はこう分かれているのか」が分からないと、JOINの意味が腑に落ちず、改修で誤ったデータの持たせ方をしてしまいます。正規化の**最低限の感覚**（重複を持たせない／1つの事実は1か所に）を身につけることが、これまでの課題すべての土台を固めます。

---

## まず、悪い例を見る: 何でも1つの表に詰め込んだ「非正規化テーブル」

ある人が、注文情報を**全部1つの表**で持とうとして、次のような表を作りました。これを `orders_flat`（フラットな注文表）と呼びます。

| order_id | customer_name | customer_email | product_name | category_name | quantity | unit_price |
|---|---|---|---|---|---|---|
| 1001 | 佐藤 太郎 | sato@example.com | りんご | 食品 | 3 | 150 |
| 1001 | 佐藤 太郎 | sato@example.com | 緑茶 | 飲料 | 2 | 130 |
| 1002 | 佐藤 太郎 | sato@example.com | コーヒー | 飲料 | 1 | 250 |
| 1003 | 鈴木 花子 | suzuki@example.com | ティッシュ | 日用品 | 5 | 300 |

### この表の何が問題か（自分で3つ考えてから下を開く）

<details>
<summary>問題点の答え合わせ（先に自分で考えること）</summary>

1. **重複（じゅうふく）が多い**: 「佐藤 太郎 / sato@example.com」が注文の明細ごとに何度も出てくる。佐藤さんが10商品買えば、同じ名前・メールが10回書かれる。容量のムダであり、読みにくい。
2. **更新の矛盾（こうしんいじょう）が起きる**: 佐藤さんがメールアドレスを変えたら、**この表の佐藤さんの行を全部**書き換えないといけない。1か所でも直し漏れると「同じ人なのにメールが違う行」が生まれ、データが信用できなくなる。
3. **存在できないデータがある**: まだ1度も注文していない顧客（伊藤さくら）は、この表に**一切登場できない**（注文行が無いと書けないため）。「注文はないが会員ではある」という事実を表現できない。
</details>

> これらが「正規化されていない表」の典型的な弊害です。**「1つの事実は1か所だけに持つ」** のが正規化の核心です。佐藤さんのメールは「顧客」という1か所にだけ持てば、変更も1か所で済み、矛盾しません。

---

## 仕様（やること）: この非正規化テーブルを正規化する

`orders_flat` を、**意味のまとまりごとに4つの表に分けて**ください。完成形は、まさにこの研修で使ってきた `schema.sql` の構造です。

### 完成条件（=分けるべき4つの表とその列）

次の形になればOKです。

1. **customers**（顧客）: `id`, `name`, `email`
   - 「人」に関する事実だけを持つ。同じ人は1行だけ。
2. **categories**（カテゴリ）: `id`, `name`
   - 「分類」に関する事実だけ。同じカテゴリは1行だけ。
3. **products**（商品）: `id`, `name`, `category_id`, `price`
   - 「商品」の事実。カテゴリは番号（`category_id`）で参照する（カテゴリ名は持たない）。
4. **orders**（注文）と **order_items**（明細）:
   - `orders`: `id`, `customer_id`（誰の注文か。顧客は番号で参照）
   - `order_items`: `id`, `order_id`, `product_id`, `quantity`, `unit_price`（どの注文の・どの商品を・何個・いくらで）

ポイント:
- 顧客名・メールは `customers` だけに持つ（注文側は `customer_id` という**番号**で指す）。
- カテゴリ名は `categories` だけに持つ（商品側は `category_id` で指す）。
- こうすると、佐藤さんのメール変更は `customers` の1行を直すだけで済む。注文ゼロの顧客も `customers` に行を持てる。

### 手を動かすパート

`schema.sql` の `customers` / `categories` / `products` / `orders` / `order_items` の `CREATE TABLE` 文が、まさにこの正規化の**正解の形**です。

1. `schema.sql` を開き、5つの `CREATE TABLE` を読んで、「`orders_flat` のどの列が、どの表に行ったか」を**自分の言葉でノートに対応づけて**ください。例:
   - `customer_name`, `customer_email` → `customers` 表へ
   - `category_name` → `categories` 表へ
   - `product_name`, `unit_price`(の元になる商品単価) → `products` 表へ
   - `order_id` → `orders` と `order_items` をつなぐ番号へ
   - `quantity`, `unit_price` → `order_items` 表へ
2. 次のSELECTを実行すると、**正規化された4表をJOINで再びつなげて**、`orders_flat` と同じ「見た目」を復元できます。正規化しても、JOINすれば元の一覧は作れる——これを確認してください。

```sql
SELECT
    o.id              AS order_id,
    c.name            AS customer_name,
    c.email           AS customer_email,
    p.name            AS product_name,
    cat.name          AS category_name,
    oi.quantity,
    oi.unit_price
FROM orders o
JOIN customers  c   ON o.customer_id = c.id
JOIN order_items oi ON oi.order_id    = o.id
JOIN products   p   ON oi.product_id  = p.id
JOIN categories cat ON p.category_id  = cat.id
ORDER BY o.id, oi.id;
```

---

## 検証方法

上のJOINを実行すると、`order_items` の数だけ（**17行**）の一覧が出ます。先頭4行が、最初に見た悪い例 `orders_flat` の表と**同じ内容**になっていることを確認してください。

**期待される結果（先頭4行・以降1010まで続く）**:

```
 order_id | customer_name | customer_email  | product_name | category_name | quantity | unit_price
----------+---------------+-----------------+--------------+---------------+----------+------------
     1001 | 佐藤 太郎     | sato@example.com| りんご       | 食品          |        3 |        150
     1001 | 佐藤 太郎     | sato@example.com| 緑茶         | 飲料          |        2 |        130
     1002 | 佐藤 太郎     | sato@example.com| コーヒー     | 飲料          |        1 |        250
     1003 | 鈴木 花子     | suzuki@example.com | ティッシュ | 日用品        |        5 |        300
```

### 件数チェック

```sql
SELECT COUNT(*) FROM (
  SELECT o.id
  FROM orders o
  JOIN customers  c   ON o.customer_id = c.id
  JOIN order_items oi ON oi.order_id    = o.id
  JOIN products   p   ON oi.product_id  = p.id
  JOIN categories cat ON p.category_id  = cat.id
) AS t;
```

**期待される結果**: `17`（明細の総数。正規化前の `orders_flat` も明細ごとに1行だったので、行数の意味は同じ）。

---

## この課題の合否（自己判定）

次の3つを**自分の言葉で説明できたら**クリアです（口頭でも、ノートに書いてもよい）。

- [ ] `orders_flat` の問題点を3つ挙げられる（重複 / 更新の矛盾 / 表現できないデータ）。
- [ ] `orders_flat` の各列が、正規化後のどの表に対応するかを説明できる。
- [ ] 「正規化すると表は増えるが、JOINで元の一覧に戻せる」ことを、上のSQLを実行して確認した。

> 対面研修で講師に「なぜ表を分けるのか」を説明できれば完璧です。説明できないなら、まだ腑に落ちていないサイン。`orders_flat` の問題点をもう一度考えてみてください。

---

## つまずきポイントとヒント

<details>
<summary>「番号で参照する」がピンとこない</summary>

`orders` は顧客名そのものではなく `customer_id`（顧客番号）を持ちます。名前が知りたければ、その番号で `customers` を引けばよい（=JOIN）。こうすれば名前は `customers` の1か所だけにあり、変更も1か所で済みます。「実体は1か所、他は番号で指す」が正規化の基本動作です。
</details>

<details>
<summary>どこまで分ければいいの？（正規化のやりすぎ）</summary>

実務では「やりすぎると逆にJOINだらけで遅く・複雑になる」こともあり、あえて崩す（非正規化する）判断もあります。ただし新人のうちは **「重複を持たない／1つの事実は1か所」** という基本を守れれば十分です。崩す判断は経験を積んでから。
</details>

---

## 模範解答への導線

3つの自己判定が説明できたら、[../solutions/10-normalization.md](../solutions/10-normalization.md) で解説とJOINの答えを確認してください。
