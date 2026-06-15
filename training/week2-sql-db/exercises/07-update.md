# 課題07: データを書き換える — UPDATE

- **所要時間の目安**: 30分
- **AI利用区分**: 🚫 AIにSQLを書かせるのは禁止。家庭教師用途のみ（エラーの意味/レビュー/概念説明）。

> 用語メモ: **UPDATE（アップデート）** … すでにある行の値を書き換える命令。

---

## 背景・狙い（現場でどう使うか）

値上げ・在庫の補充・注文状態の変更——既存データの**書き換え**は日常的な作業です。UPDATEで一番怖いのは **WHERE の付け忘れ**。WHEREを忘れると「全行」が書き換わってしまいます。現場の事故の典型なので、ここで「必ずWHEREで対象を絞る」習慣を体に入れます。

> ⚠️ データを変える課題です。やり直したくなったら `psql -h localhost -U postgres -d training -f schema.sql` で初期状態に戻せます。**この課題は初期状態（schema.sqlを流した直後）から始めてください。**

---

## 仕様（やること）

`schema.sql` を流した直後の状態から、次の3つのUPDATEを実行してください。

- **問1（1行だけ更新）**: 商品「コーヒー」（`id` = 4）の単価（`price`）を **250 → 280** に変更する。
- **問2（複数行をまとめて更新）**: 飲料カテゴリ（`category_id` = 2）の商品すべての在庫（`stock`）を、**現在の値に +50** する。
  - ヒント: `SET stock = stock + 50`（今の値を使って増やす書き方）。
- **問3（状態の更新）**: 注文番号1008（`id` = 1008、現在 `status` = `pending`）の状態を **`cancelled`** に変更する。

---

## 完成条件（自分で正解か判定する基準）

- 問1: コーヒー（id=4）の price が **280** になっている。
- 問2: 飲料3商品の在庫が、緑茶 **250**、コーヒー **130**、ミネラルウォーター **350** になっている（それぞれ元の +50）。
- 問3: 注文1008の status が **cancelled** になっている。

---

## 検証方法

`psql -h localhost -U postgres -d training` で接続し、自分のUPDATEを実行したあと確認します。

### 問1の確認

```sql
SELECT id, name, price FROM products WHERE id = 4;
```

**期待される結果**:

```
 id |   name   | price
----+----------+-------
  4 | コーヒー |   280
```

### 問2の確認

```sql
SELECT id, name, stock FROM products WHERE category_id = 2 ORDER BY id;
```

**期待される結果**:

```
 id |        name        | stock
----+--------------------+-------
  3 | 緑茶               |   250
  4 | コーヒー           |   130
  5 | ミネラルウォーター |   350
```

> 検算: 緑茶 200+50=250、コーヒー 80+50=130、ミネラルウォーター 300+50=350。

### 問3の確認

```sql
SELECT id, status FROM orders WHERE id = 1008;
```

**期待される結果**:

```
  id  |  status
------+-----------
 1008 | cancelled
```

### 答え合わせ用の確認SQL（更新件数の感覚をつかむ）

UPDATEを実行すると、psqlは `UPDATE 1` や `UPDATE 3` のように **何行更新したか** を返します。

- 問1の実行直後 → `UPDATE 1`（1行だけ更新できていれば成功）
- 問2の実行直後 → `UPDATE 3`（飲料は3商品なので3行）
- 問3の実行直後 → `UPDATE 1`

もし問1で `UPDATE 12` と出たら、それは **WHEREを付け忘れて全商品を書き換えた** という事故のサインです。すぐ `schema.sql` を流し直してやり直してください。

---

## つまずきポイントとヒント

<details>
<summary>UPDATE の基本の形</summary>

`UPDATE 表名 SET 列 = 新しい値 WHERE 対象を絞る条件;` です。**WHEREを必ず付ける**こと。WHEREが無いと全行が更新されます。
</details>

<details>
<summary>「今の値に +50」ってどう書く？（問2）</summary>

`SET stock = stock + 50` と書けます。右辺の `stock` は「今の値」を指すので、「今の値に50足したものを入れ直す」という意味になります。
</details>

<details>
<summary>更新する前に対象を確認したい（安全な癖）</summary>

UPDATE を打つ前に、同じ WHERE で **SELECT して対象行を見ておく** のが現場の安全策です。例: `SELECT * FROM products WHERE id = 4;` で1行だけ出ることを確認してから UPDATE する。この癖が事故を防ぎます。
</details>

<details>
<summary>間違えて全部更新してしまった</summary>

落ち着いて `psql -h localhost -U postgres -d training -f schema.sql` を流し直せば初期状態に戻ります。研修段階なので失敗は財産です。「WHEREを忘れると怖い」を体で覚えられたなら、その失敗には価値があります。
</details>

---

## 模範解答への導線

3問とも成功し確認も合ったら、[../solutions/07-update.sql](../solutions/07-update.sql) と見比べてください。**先に答えを見ないこと。**
