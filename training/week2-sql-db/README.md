# 週2: SQLとDBアクセス — データを出し入れできるようになる

> 用語メモ: **SQL（エスキューエル）** … データベースに「こういうデータが欲しい」「こう書き換えて」と命令するための言葉。**DB（データベース）** … データを保存・検索する仕組み。この研修では PostgreSQL を使います。

進め方・AI利用ルール・質問の仕方・日報の書き方は、まず [training/README.md](../README.md) を読んでください。このファイルは**週2だけの案内**です。

---

## この週の到達目標

**「テーブルと取得したいデータを示されたら、SQLを書けて、Javaから実行できる」** 状態になること。

具体的には、週末に次を「自分の言葉で説明でき、実際に書ける」ようになっていればクリアです。

- 1つの表からデータを取り出す（SELECT / WHERE / ORDER BY / LIMIT）
- 複数の表をつなげて取り出す（JOIN）
- データをまとめて数える・合計する（GROUP BY / COUNT / SUM / AVG / HAVING）
- データを入れる・書き換える・消す（INSERT / UPDATE / DELETE）
- 「まとめて成功か、まとめて取り消すか」を扱う（トランザクション）
- なぜ表を分けるのか、を説明できる（正規化の基礎）
- JavaのプログラムからSQLを実行できる（MyBatis）

---

## AI利用区分

🚫 **週2もAIにコードを書かせるのは禁止。** 家庭教師としての利用だけ許可します（エラーの意味を聞く／自分のSQLのレビューを頼む／概念を説明させる）。「このSQLを書いて」はダメです。理由は [training/README.md](../README.md) の「2. AI利用ルール」を読んでください。

---

## 最初にやること: 演習用データベースを準備する

このフォルダの `schema.sql` を流し込むと、演習で使う5つの表とサンプルデータが一気に作られます。

> 用語メモ: **スキーマ（schema）** … 「どんな表が、どんな列を持つか」という設計図。`schema.sql` には、表を作る命令（CREATE TABLE）と、最初に入れておくデータ（INSERT）が書いてあります。

### 流し込み手順

1. WSL2のターミナル（Ubuntu）を開く。
2. このフォルダ（`week2-sql-db`）に移動する。
3. 次のコマンドを実行する（`postgres` は週0で使った管理ユーザー）。

> WSL2は再起動するとPostgreSQLが止まります。DBを使う前に `sudo service postgresql start` を実行してください（詳しくは週0参照）。これをしないと `Connection refused` で接続できません。

```bash
psql -h localhost -U postgres -d training -f schema.sql
```

- パスワードを聞かれたら、週0で設定したパスワードを入力します（入力中は画面に出ませんが打ててはいます）。
- `-d training` は「training という名前のDBに対して実行する」という意味です（週0で作成済み）。
- `-f schema.sql` は「schema.sql というファイルの中身を実行する」という意味です。

**期待される結果**: エラーが出ず、`DROP TABLE` / `CREATE TABLE` / `INSERT 0 5` のようなメッセージが並べば成功です。

> このコマンドは**何度流してもOK**です。`schema.sql` の冒頭で古い表を消してから作り直すので、演習でデータを書き換えてしまっても、もう一度流せば元の状態に戻せます。INSERT / UPDATE / DELETE の課題で「やり直したい」ときは、これを流し直してください。

### 入ったか確認する

`psql -h localhost -U postgres -d training` でDBに入り、次を実行します。

```sql
SELECT 'categories'  AS t, COUNT(*) FROM categories
UNION ALL SELECT 'products',    COUNT(*) FROM products
UNION ALL SELECT 'customers',   COUNT(*) FROM customers
UNION ALL SELECT 'orders',      COUNT(*) FROM orders
UNION ALL SELECT 'order_items', COUNT(*) FROM order_items;
```

**期待される結果**:

```
      t       | count
--------------+-------
 categories   |     5
 products     |    12
 customers    |     6
 orders       |    10
 order_items  |    17
```

この件数になっていれば、データは正しく入っています。`\q` で抜けられます。

> 用語メモ: **psql（ピーエスキューエル）** … PostgreSQL にコマンドで命令するための道具。`psql -U ユーザー名 -d DB名` で対話モードに入り、SQLを打って Enter で実行します。終了は `\q`。

---

## 取り扱う表（ドメイン: ECサイト = ネット通販）

`schema.sql` で作られる5つの表です。詳しい列の意味は `schema.sql` のコメントを読んでください。

| 表 | 日本語 | 何を表すか |
|---|---|---|
| `categories` | カテゴリ | 商品の分類（食品・飲料・日用品・家電・書籍） |
| `products` | 商品 | 商品名・所属カテゴリ・単価・在庫 |
| `customers` | 顧客 | 会員の氏名・メール・都道府県・登録日 |
| `orders` | 注文 | 誰が・いつ・どんな状態で注文したか |
| `order_items` | 注文明細 | 1注文の中で、どの商品を何個・いくらで買ったか |

### 表どうしの関係（これを頭に入れると JOIN が分かる）

```
categories (1) ──< (多) products
customers  (1) ──< (多) orders (1) ──< (多) order_items >── (1) products
```

読み方:
- 1つのカテゴリには 複数の商品 がぶら下がる（`products.category_id` が `categories.id` を指す）。
- 1人の顧客は 複数の注文 を持てる（`orders.customer_id` が `customers.id` を指す）。
- 1つの注文は 複数の明細 を持つ（`order_items.order_id` が `orders.id` を指す）。
- 1つの明細は 1つの商品 を指す（`order_items.product_id` が `products.id` を指す）。

> 用語メモ: **外部キー（がいぶキー）** … ある表の列が、別の表の主キーを「指している」関係のこと。上の `category_id`・`customer_id`・`order_id`・`product_id` がそれです。これがあるから JOIN で表をつなげられます。

---

## 課題一覧（上から順に解く）

難易度順です。**必ず上から順に**進めてください。前の課題で覚えたことを次で使います。

| # | ファイル | テーマ | 目安 |
|---|---|---|---|
| 01 | [exercises/01-select-basics.md](exercises/01-select-basics.md) | SELECT の基礎（列を選ぶ・全件取る） | 30分 |
| 02 | [exercises/02-where-order-limit.md](exercises/02-where-order-limit.md) | 絞り込み・並べ替え・件数制限（WHERE / ORDER BY / LIMIT） | 40分 |
| 03 | [exercises/03-join.md](exercises/03-join.md) | 複数テーブルの結合（JOIN） | 60分 |
| 04 | [exercises/04-aggregate.md](exercises/04-aggregate.md) | 集計（GROUP BY / COUNT / SUM / AVG / HAVING） | 60分 |
| 05 | [exercises/05-subquery.md](exercises/05-subquery.md) | サブクエリ（クエリの中のクエリ） | 50分 |
| 06 | [exercises/06-insert.md](exercises/06-insert.md) | データを入れる（INSERT） | 30分 |
| 07 | [exercises/07-update.md](exercises/07-update.md) | データを書き換える（UPDATE） | 30分 |
| 08 | [exercises/08-delete.md](exercises/08-delete.md) | データを消す（DELETE） | 30分 |
| 09 | [exercises/09-transaction.md](exercises/09-transaction.md) | トランザクション（BEGIN / COMMIT / ROLLBACK） | 40分 |
| 10 | [exercises/10-normalization.md](exercises/10-normalization.md) | 正規化の基礎（なぜ表を分けるのか・座学+手を動かす） | 50分 |
| 11 | [exercises/11-mybatis.md](exercises/11-mybatis.md) | MyBatisでJavaからDBを叩く（最終課題） | 90分 |

各課題ファイルには、必ず次が書いてあります。

1. 課題タイトルと所要時間の目安
2. AI利用区分
3. 背景・狙い（現場でどう使うか）
4. 仕様（対象テーブル・取りたいデータ・前提条件）
5. 完成条件（一人で正解判定できる基準）
6. 検証方法（自分で叩くコマンドと、期待される結果）
7. つまずきポイントとヒント（折りたたみ。答えそのものではない）
8. 模範解答への導線

---

## 進め方（週2版）

1. 上の表の上から課題を1つ開く。
2. **まず自分でSQLを書く**。模範解答（`solutions/`）は先に見ない。
3. 課題の「検証方法」のコマンドを `psql` で実行し、**期待される結果と一致するか自分で照合する**。
4. 多くの課題には「答え合わせ用の確認SQL」が付いています。自分が書いたSQLが正しいか、件数や合計で機械的にチェックできます。
5. 詰まったら「つまずきポイントとヒント」を開く。
6. それでもダメなら、AIに**家庭教師として**質問する（コードを書かせない）か、質問テンプレートで講師に相談する。
7. 1日の終わりに日報を書く。

> SQLは「書いて、流して、結果を見る」を何度も繰り返すのが上達の近道です。間違えても `schema.sql` を流し直せば元に戻るので、どんどん試してください。

---

## もっとやりたい人へ（早く終わったら）

- `04-aggregate` の集計に、`ROUND()`（四捨五入）や `ORDER BY` を足して、結果を見やすく整えてみる。
- 自分で「こんなデータが欲しい」というお題を作り、それを満たすSQLを書いてみる（例: 「都道府県ごとの注文金額合計」）。
- `11-mybatis` の最終課題を、SELECT 1件だけでなく「全件取得」に拡張してみる（ヒント: 戻り値を `List` にする）。

---

準備ができたら [exercises/01-select-basics.md](exercises/01-select-basics.md) から始めましょう。
