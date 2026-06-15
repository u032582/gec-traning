# 課題03: 複数テーブルの結合 — JOIN

- **所要時間の目安**: 60分
- **AI利用区分**: 🚫 AIにSQLを書かせるのは禁止。家庭教師用途のみ（エラーの意味/レビュー/概念説明）。

> 用語メモ: **JOIN（ジョイン）** … 別々の表を、共通する列でつなげて1つの結果にすること。例えば「商品（products）」には `category_id`（カテゴリ番号）しかありませんが、「カテゴリ（categories）」とJOINすれば、番号ではなく**カテゴリ名**を一緒に表示できます。

---

## 背景・狙い（現場でどう使うか）

実務のデータは「番号でつながった複数の表」に分かれています（理由は課題10の正規化で学びます）。だから「注文一覧を、顧客名つきで見たい」「明細を、商品名つきで見たい」のように、**表をつなげて初めて意味のある情報になる**場面がほとんどです。JOINは現場のSELECTで最頻出の技です。ここが週2の山場の1つ。

表どうしの関係は [../README.md](../README.md) の「表どうしの関係」図を見ながら進めてください。

---

## 仕様（やること）

次の4問を解いてください。

- **問1（2表のJOIN）**: 全商品について、**商品名（products.name）と、その商品が属するカテゴリ名（categories.name）** を一覧する。
  - 使う表: `products` と `categories`。つなぐ条件: `products.category_id = categories.id`。
- **問2（2表のJOIN）**: 全注文について、**注文番号（orders.id）・顧客名（customers.name）・注文日（orders.ordered_at）・状態（orders.status）** を一覧する。
  - 使う表: `orders` と `customers`。つなぐ条件: `orders.customer_id = customers.id`。
- **問3（3表のJOIN・条件つき）**: **注文番号1006** の明細について、**商品名（products.name）・個数（order_items.quantity）・注文時単価（order_items.unit_price）** を一覧する。
  - 使う表: `order_items` と `products`。つなぐ条件: `order_items.product_id = products.id`。さらに `WHERE order_items.order_id = 1006` で絞る。
- **問4（LEFT JOIN）**: **全顧客** について、**顧客名（customers.name）と、その人の注文番号（orders.id）** を一覧する。**ただし、1度も注文していない顧客も必ず表示する**（その人の注文番号は空＝NULLになる）。
  - 使う表: `customers` と `orders`。`customers` を主にした **LEFT JOIN** を使う。

> 用語メモ:
> **内部結合（INNER JOIN、ただ JOIN と書いてもこれ）** … 両方の表にマッチする行だけ残す。
> **左外部結合（LEFT JOIN）** … 左の表（FROMに書いた表）の行は、右にマッチがなくても必ず残す。マッチしない部分はNULL（空）になる。問4で「注文ゼロの顧客も残したい」ときに使う。

---

## 完成条件（自分で正解か判定する基準）

- 問1: **12行**（商品の数だけ）。各商品に正しいカテゴリ名が並ぶ。
- 問2: **10行**（注文の数だけ）。
- 問3: **3行**（注文1006の明細3つ）。
- 問4: **11行**。うち1行は「伊藤 さくら」で注文番号が空（NULL）になる。

> 問4の行数が「6人だから6行」ではなく **11行** になる理由: 1人で複数注文している人がいるため、顧客×注文の組み合わせの数になります（佐藤3 + 鈴木2 + 高橋2 + 田中2 + 渡辺1 + 伊藤0件だが1行）= 11行。

---

## 検証方法

`psql -h localhost -U postgres -d training` で実行し、下と見比べてください。

### 問1の期待結果（12行）

```
        name        |  name
--------------------+--------
 りんご             | 食品
 バナナ             | 食品
 緑茶               | 飲料
 コーヒー           | 飲料
 ミネラルウォーター | 飲料
 ティッシュ         | 日用品
 洗剤               | 日用品
 ドライヤー         | 家電
 電気ケトル         | 家電
 SQL入門書          | 書籍
 Java入門書         | 書籍
 ノートPC           | 家電
```

> 見出しが両方 `name` で紛らわしいので、`SELECT products.name AS 商品名, categories.name AS カテゴリ名` のように別名を付けると読みやすくなります（付けなくても正解）。並び順は指定なしなので多少違ってもOK。

### 問2の期待結果（10行）

```
  id  |   name    | ordered_at |  status
------+-----------+------------+-----------
 1001 | 佐藤 太郎 | 2024-01-10 | completed
 1002 | 佐藤 太郎 | 2024-03-05 | completed
 1003 | 鈴木 花子 | 2024-03-20 | completed
 1004 | 高橋 一郎 | 2024-04-01 | shipped
 1005 | 鈴木 花子 | 2024-05-12 | cancelled
 1006 | 田中 美咲 | 2024-06-08 | completed
 1007 | 佐藤 太郎 | 2024-07-19 | shipped
 1008 | 渡辺 健   | 2024-08-25 | pending
 1009 | 高橋 一郎 | 2024-09-30 | completed
 1010 | 田中 美咲 | 2024-10-14 | completed
```

### 問3の期待結果（3行・注文1006）

```
    name    | quantity | unit_price
------------+----------+------------
 Java入門書 |        1 |       3200
 SQL入門書  |        1 |       2800
 コーヒー   |        2 |        250
```

### 問4の期待結果（11行・注文ゼロの顧客も含む）

```
    name    |  id
------------+------
 佐藤 太郎  | 1001
 佐藤 太郎  | 1002
 佐藤 太郎  | 1007
 鈴木 花子  | 1003
 鈴木 花子  | 1005
 高橋 一郎  | 1004
 高橋 一郎  | 1009
 田中 美咲  | 1006
 田中 美咲  | 1010
 渡辺 健    | 1008
 伊藤 さくら |      ← 注文番号が空（NULL）
```

（並び順は指定なし。**伊藤 さくらの行があり、その注文番号が空** であることが問4の合否ポイントです。）

### 答え合わせ用の確認SQL（件数チェック）

```sql
-- 問1は 12
SELECT COUNT(*) FROM ( （ここに問1のSELECT文） ) AS t;
-- 問2は 10
SELECT COUNT(*) FROM ( （ここに問2のSELECT文） ) AS t;
-- 問3は 3
SELECT COUNT(*) FROM ( （ここに問3のSELECT文） ) AS t;
-- 問4は 11（もしここが 10 になったら、LEFT ではなく内部結合になっている＝注文ゼロの顧客が落ちている）
SELECT COUNT(*) FROM ( （ここに問4のSELECT文） ) AS t;
```

> 問4で件数が **10** になったら、それは普通のJOIN（内部結合）になっていて、注文ゼロの伊藤さんが消えてしまったサインです。`LEFT JOIN` になっているか見直してください。

---

## つまずきポイントとヒント

<details>
<summary>JOINの基本の形が思い出せない</summary>

`SELECT 列 FROM 表A JOIN 表B ON 表A.列 = 表B.列;` が基本形です。`ON` のところに「どの列どうしでつなぐか」を書きます。問1なら `ON products.category_id = categories.id`。
</details>

<details>
<summary>3つの表をつなぐには？（問3）</summary>

JOINは続けて書けます: `FROM 表A JOIN 表B ON ... JOIN 表C ON ...`。問3は `order_items` と `products` の2表で足ります（商品名は products にあるため）。注文番号での絞り込みは `WHERE order_items.order_id = 1006`。
</details>

<details>
<summary>「column reference is ambiguous（列の指定があいまい）」というエラー</summary>

`products` と `categories` の両方に `name` という列があるとき、ただ `name` と書くとDBは「どっちの name?」と困ります。`products.name` のように **表名.列名** で指定すれば解決します。
</details>

<details>
<summary>問4で注文ゼロの顧客が出てこない</summary>

普通の `JOIN`（内部結合）だと、両方にマッチする行しか残りません。注文ゼロの顧客は orders 側にマッチがないので消えます。「左（customers）は必ず残す」= `LEFT JOIN` を使います。`FROM customers LEFT JOIN orders ON ...` の順番（customersが左）が大切です。
</details>

---

## 模範解答への導線

4問とも書けて件数チェックも通ったら、[../solutions/03-join.sql](../solutions/03-join.sql) と見比べてください。**先に答えを見ないこと。**
