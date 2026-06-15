# 課題06: データを入れる — INSERT

- **所要時間の目安**: 30分
- **AI利用区分**: 🚫 AIにSQLを書かせるのは禁止。家庭教師用途のみ（エラーの意味/レビュー/概念説明）。

> 用語メモ: **INSERT（インサート）** … 表に新しい行（データ）を追加する命令。

---

## 背景・狙い（現場でどう使うか）

新しい商品が増えた、新しい会員が登録した——データを**入れる**操作は、SELECTの次に基本です。INSERT以降（06〜08）は**データを書き換える操作**なので、間違えても `schema.sql` を流し直せば元に戻せます。安心して試してください。

> ⚠️ ここからは**データを変える**課題です。やり直したくなったら、ターミナルで `psql -h localhost -U postgres -d training -f schema.sql` を実行すれば、全テーブルが初期状態に戻ります。

---

## 仕様（やること）

次の3つのINSERTを、順番に実行してください。

- **問1**: `categories` に、新しいカテゴリを1件追加する。
  - `id` = 6、`name` = `文房具`
- **問2**: `products` に、新しい商品を1件追加する。
  - `id` = 13、`name` = `消しゴム`、`category_id` = 6、`price` = 80、`stock` = 200
  - ※ `category_id` = 6 は問1で追加したカテゴリ。**問1を先にやらないと**「そんなカテゴリ番号は無い」と外部キーのエラーになります（順番が大事）。
- **問3**: `customers` に、新しい顧客を1件追加する。
  - `id` = 7、`name` = `山本 涼`、`email` = `yamamoto@example.com`、`prefecture` = `北海道`、`registered_at` = `2025-01-20`

> 用語メモ: **外部キー制約** … `products.category_id` は `categories.id` に存在する番号しか入れられない、という決まり。存在しないカテゴリ番号を入れようとすると、DBがエラーで止めてくれます（おかしなデータを防ぐ仕組み）。

---

## 完成条件（自分で正解か判定する基準）

3件の追加がすべて成功し、追加後の件数が次になること。

- `categories` … **6行**（元5 + 1）
- `products` … **13行**（元12 + 1）
- `customers` … **7行**（元6 + 1）

そして、追加した3行の内容が仕様どおりに入っていること。

---

## 検証方法

`psql -h localhost -U postgres -d training` で接続し、自分のINSERTを実行したあと、次で確認します。

### 件数の確認

```sql
SELECT
  (SELECT COUNT(*) FROM categories) AS categories,
  (SELECT COUNT(*) FROM products)   AS products,
  (SELECT COUNT(*) FROM customers)  AS customers;
```

**期待される結果**:

```
 categories | products | customers
------------+----------+-----------
          6 |       13 |         7
```

### 追加した中身の確認

```sql
SELECT * FROM categories WHERE id = 6;
SELECT * FROM products   WHERE id = 13;
SELECT * FROM customers  WHERE id = 7;
```

**期待される結果**:

```
 id |  name
----+--------
  6 | 文房具

 id |  name  | category_id | price | stock
----+--------+-------------+-------+-------
 13 | 消しゴム |           6 |    80 |   200

 id |  name   |        email         | prefecture | registered_at
----+---------+----------------------+------------+---------------
  7 | 山本 涼 | yamamoto@example.com | 北海道     | 2025-01-20
```

> 確認できたら、次の課題に備えて `psql -h localhost -U postgres -d training -f schema.sql` で初期状態に戻しておくと安全です（戻さなくても次の課題は動きますが、件数が増えたままになります）。

---

## つまずきポイントとヒント

<details>
<summary>INSERT の基本の形</summary>

`INSERT INTO 表名 (列1, 列2, ...) VALUES (値1, 値2, ...);` です。列の並びと値の並びを**対応させる**のがコツ。`schema.sql` の中に同じ形のINSERTが並んでいるので、それをお手本にしてください（コピーして書き換えるのではなく、形を見て自分で書く練習）。
</details>

<details>
<summary>文字列・日付の書き方</summary>

文字列も日付もシングルクォートで囲みます: `'文房具'`、`'2025-01-20'`。数値（id や price）はクォート不要です。
</details>

<details>
<summary>「duplicate key value violates unique constraint（主キー重複）」エラー</summary>

すでに同じ `id` の行があるときに出ます。例えば問1を2回実行すると、2回目は「id=6 はもう在る」と怒られます。`schema.sql` を流し直して初期状態に戻してからやり直してください。
</details>

<details>
<summary>「violates foreign key constraint（外部キー違反）」エラー</summary>

問2で `category_id = 6` を入れようとしたとき、カテゴリ6がまだ無いと出ます。問1（カテゴリ追加）を先に実行してください。これは「存在しないカテゴリの商品を作れない」というDBの安全装置です。
</details>

---

## 模範解答への導線

3問とも成功し件数も合ったら、[../solutions/06-insert.sql](../solutions/06-insert.sql) と見比べてください。**先に答えを見ないこと。**
