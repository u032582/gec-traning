# 課題10 模範解答・解説: 正規化の基礎

※ 先に自分で考えてから読むこと。

---

## `orders_flat`（非正規化テーブル）の問題点（3つ）

1. **重複が多い**: 同じ顧客（佐藤 太郎 / sato@example.com）の名前・メールが、その人の明細の数だけ繰り返し書かれる。容量のムダで、読みづらい。
2. **更新の矛盾が起きる**: 顧客がメールを変えると、その顧客の**全行**を直す必要がある。直し漏れると「同じ人なのにメールが違う行」ができ、どれが正しいか分からなくなる（=データの信頼性が崩れる）。
3. **表現できないデータがある**: 注文が1件も無い顧客（伊藤 さくら）は、明細行が無いのでこの表に存在できない。「注文はないが会員ではある」という事実を記録できない。

> 核心: **「1つの事実は1か所だけに持つ」**。佐藤さんのメールは `customers` の1行だけに持てば、変更も1か所で済み、矛盾しない。

---

## 列の対応づけ（`orders_flat` の列 → 正規化後の表）

| `orders_flat` の列 | 行き先の表 | 行き先の列 |
|---|---|---|
| `customer_name` | customers | name |
| `customer_email` | customers | email |
| `category_name` | categories | name |
| `product_name` | products | name |
| （商品の単価そのもの） | products | price |
| `order_id` | orders（本体） / order_items（参照） | orders.id / order_items.order_id |
| `quantity` | order_items | quantity |
| `unit_price`（注文時の単価） | order_items | unit_price |

ポイント:
- 顧客は `customers` に1行だけ持ち、注文側は `customer_id`（番号）で指す。
- カテゴリは `categories` に1行だけ持ち、商品側は `category_id`（番号）で指す。
- `unit_price` は「注文した時点の単価」なので、商品の `price` とは別に `order_items` に残す（値上げ後も注文時の値段が分かる）。

正解の `CREATE TABLE` は `schema.sql` の各テーブル定義そのもの。

---

## 正規化後でも、JOINで元の一覧に戻せる（確認SQL）

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

- 結果は **17行**（明細の総数）。
- 表は5つに分かれているが、JOINでつなげば `orders_flat` と同じ「見た目」を復元できる。
- つまり正規化は「情報を失わずに、重複と矛盾を取り除く」作業だと分かる。

---

## まとめ（自分の言葉で言えるように）

- 正規化 = 「重複を持たない／1つの事実は1か所」。
- 番号（外部キー）で他の表を指すことで、実体は1か所に保てる。
- 分けても、JOINでいつでも元の一覧に戻せる。
- やりすぎ（過度な分割）は実務では逆効果なこともあるが、新人のうちは上の基本を守れれば十分。
