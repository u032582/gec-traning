# 課題08: データを消す — DELETE

- **所要時間の目安**: 30分
- **AI利用区分**: 🚫 AIにSQLを書かせるのは禁止。家庭教師用途のみ（エラーの意味/レビュー/概念説明）。

> 用語メモ: **DELETE（デリート）** … 表から行を削除する命令。

---

## 背景・狙い（現場でどう使うか）

不要になったデータを**消す**操作です。UPDATEと同じく **WHEREの付け忘れ＝全削除** という事故が一番怖い操作。さらにDELETEには「外部キーでつながっている行は、参照されている側を先に消せない」という重要なルールがあります（子→親の順で消す）。この2点を、実際に手を動かして体験します。

> ⚠️ データを変える課題です。**初期状態（`psql -h localhost -U postgres -d training -f schema.sql` を流した直後）から始めてください。** やり直したくなったらいつでも流し直せます。

---

## 仕様（やること）

`schema.sql` を流した直後の状態から、次を順番に実行してください。

- **問1（子テーブルを先に消す）**: キャンセルされた注文1005の **明細** を削除する。
  - 対象: `order_items` のうち `order_id` = 1005 の行（明細id=8の1行だけ）。
- **問2（親テーブルを消す）**: 注文1005 **本体** を削除する。
  - 対象: `orders` のうち `id` = 1005 の行。
  - ※ **問1を先にやらないと**、この削除は外部キーのエラーで失敗します。「注文1005を指している明細が残っているのに、注文1005を消そうとした」状態になるためです。**子（明細）→ 親（注文）の順** で消すのがルール。
- **問3（参照されていない行は単独で消せる）**: 商品「洗剤」（`id` = 7）を削除する。
  - 洗剤は一度も注文されていない（`order_items` から参照されていない）ので、単独で削除できます。

---

## 完成条件（自分で正解か判定する基準）

- 問1: `order_items` が **16行**（元17 − 1）。注文1005の明細が消えている。
- 問2: `orders` が **9行**（元10 − 1）。注文1005が消えている。
- 問3: `products` が **11行**（元12 − 1）。洗剤（id=7）が消えている。

---

## 検証方法

`psql -h localhost -U postgres -d training` で接続し、自分のDELETEを実行したあと確認します。

### 件数の確認

```sql
SELECT
  (SELECT COUNT(*) FROM order_items) AS order_items,
  (SELECT COUNT(*) FROM orders)      AS orders,
  (SELECT COUNT(*) FROM products)    AS products;
```

**期待される結果**:

```
 order_items | orders | products
-------------+--------+----------
          16 |      9 |       11
```

### 消えたことの確認（0行になればOK）

```sql
SELECT * FROM order_items WHERE order_id = 1005;   -- 0行
SELECT * FROM orders      WHERE id = 1005;          -- 0行
SELECT * FROM products    WHERE id = 7;             -- 0行
```

3つとも `(0 rows)` と表示されれば、正しく削除できています。

### 削除件数で機械チェック

DELETEを実行すると psql は `DELETE 1` のように **消した行数** を返します。

- 問1 → `DELETE 1`
- 問2 → `DELETE 1`
- 問3 → `DELETE 1`

もし問1で `DELETE 17` のような大きな数が出たら、WHEREの付け忘れで全明細を消した事故です。すぐ `schema.sql` を流し直してください。

---

## つまずきポイントとヒント

<details>
<summary>DELETE の基本の形</summary>

`DELETE FROM 表名 WHERE 対象を絞る条件;` です。SELECTやUPDATEと違って「列の指定」はありません（行ごと消すため）。**WHEREを必ず付ける**こと。
</details>

<details>
<summary>問2が「violates foreign key constraint」で失敗する</summary>

注文1005を指している明細（order_items）がまだ残っています。**先に問1（明細の削除）** をやってください。これは「親（注文）を消す前に、それを指す子（明細）を片付けないとデータの辻褄が合わなくなる」のをDBが防いでいるためです。順番は **子 → 親**。
</details>

<details>
<summary>なぜ洗剤（問3）は単独で消せるの？</summary>

洗剤は `order_items` のどの行からも参照されていない（一度も注文されていない）ためです。誰からも指されていない行は、そのまま消せます。逆に、注文された商品（例: りんご）を消そうとすると、その商品を指す明細が残っているのでエラーになります。試しに `DELETE FROM products WHERE id = 1;`（りんご）を打ってみて、エラーになることを確認すると理解が深まります（確認したら schema.sql を流し直す）。
</details>

<details>
<summary>消す前に対象を確認したい（安全な癖）</summary>

UPDATE同様、DELETEの前に同じWHEREで **SELECTして対象行を見ておく** のが鉄則です。「消す予定の行が、思った通りの行か」を目で確かめてからDELETEする習慣をつけてください。
</details>

---

## 模範解答への導線

3問とも成功し件数も合ったら、[../solutions/08-delete.sql](../solutions/08-delete.sql) と見比べてください。**先に答えを見ないこと。**
