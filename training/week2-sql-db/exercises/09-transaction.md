# 課題09: トランザクション — BEGIN / COMMIT / ROLLBACK

- **所要時間の目安**: 40分
- **AI利用区分**: 🚫 AIにSQLを書かせるのは禁止。家庭教師用途のみ（エラーの意味/レビュー/概念説明）。

> 用語メモ: **トランザクション** … 「一連の操作をひとまとめにして、**全部成功（COMMIT）か、全部なかったことに（ROLLBACK）**」を保証する仕組み。途中で失敗しても“中途半端な状態”を残さないための安全装置です。

---

## 背景・狙い（現場でどう使うか）

例えば「銀行口座Aから1万円減らして、口座Bに1万円増やす」。Aを減らした直後にエラーで止まったら、お金が消えてしまいます。トランザクションを使えば「両方成功したときだけ確定、片方でも失敗したら両方とも取り消し」にできます。在庫を減らして注文を作る、といったECの処理も同じ。複数の書き換えを**まとめて安全に**行うための、現場必須の概念です。

この課題では、トランザクションを**手で**体験します。`BEGIN`（開始）→ いくつかUPDATE → `ROLLBACK`（取り消し）すると **変更がなかったことになる** ——これを自分の目で確かめるのが狙いです。

> 用語メモ:
> **BEGIN** … トランザクションを始める合図。「ここから先はまとめて扱う」。
> **COMMIT（コミット）** … ここまでの変更を**確定**する。
> **ROLLBACK（ロールバック）** … ここまでの変更を**全部取り消す**（BEGINの時点に戻す）。

> ⚠️ **初期状態（`psql -h localhost -U postgres -d training -f schema.sql` を流した直後）から始めてください。**

---

## 仕様（やること）

`psql -h localhost -U postgres -d training` で接続した1つの画面（セッション）の中で、次の2つの実験を順に行います。**コマンドを上から1行ずつ実行**してください。

### 実験A: ROLLBACK で変更が消えることを確かめる

1. まず現在の在庫を見る:
   ```sql
   SELECT id, name, stock FROM products WHERE id = 1;   -- りんごの在庫は 100 のはず
   ```
2. トランザクションを開始する:
   ```sql
   BEGIN;
   ```
3. りんご（id=1）の在庫を 100 → 0 に書き換える:
   ```sql
   UPDATE products SET stock = 0 WHERE id = 1;
   ```
4. **同じ画面で**もう一度 在庫を見る（この時点では 0 に見える）:
   ```sql
   SELECT id, name, stock FROM products WHERE id = 1;   -- 0 になっている
   ```
5. 取り消す:
   ```sql
   ROLLBACK;
   ```
6. もう一度 在庫を見る（**100 に戻っている** = 変更がなかったことになった）:
   ```sql
   SELECT id, name, stock FROM products WHERE id = 1;   -- 100 に戻る
   ```

### 実験B: COMMIT で変更が確定することを確かめる

1. トランザクションを開始:
   ```sql
   BEGIN;
   ```
2. バナナ（id=2）の在庫を 50 → 999 に書き換える:
   ```sql
   UPDATE products SET stock = 999 WHERE id = 2;
   ```
3. 確定する:
   ```sql
   COMMIT;
   ```
4. 在庫を見る（**999 のまま** = 確定した）:
   ```sql
   SELECT id, name, stock FROM products WHERE id = 2;   -- 999 のまま
   ```

---

## 完成条件（自分で正解か判定する基準）

- 実験A: ROLLBACK の **後**、りんご（id=1）の在庫が **100 に戻っている**（途中で0に見えたのが、取り消しで元通り）。
- 実験B: COMMIT の **後**、バナナ（id=2）の在庫が **999 のまま**（確定したので残る）。

「ROLLBACKすると変更が消える／COMMITすると変更が残る」を、自分の画面で確認できたらクリアです。

---

## 検証方法

実験Aの最後（手順6）と実験Bの最後（手順4）の SELECT 結果が、次になることを確認します。

```
-- 実験A 手順6（ROLLBACK後）
 id |  name  | stock
----+--------+-------
  1 | りんご |   100      ← 0 ではなく 100 に戻っていれば成功

-- 実験B 手順4（COMMIT後）
 id |  name  | stock
----+--------+-------
  2 | バナナ |   999      ← 999 のまま残っていれば成功
```

### まとめ確認（実験のあと）

実験が終わったら、次でりんごとバナナの状態を一度に確認できます。

```sql
SELECT id, name, stock FROM products WHERE id IN (1, 2) ORDER BY id;
```

**期待される結果**:

```
 id |  name  | stock
----+--------+-------
  1 | りんご |   100      ← ROLLBACKで元に戻った
  2 | バナナ |   999      ← COMMITで確定した
```

> 実験Bでバナナを999にしたままなので、次の課題に進む前に `psql -h localhost -U postgres -d training -f schema.sql` で初期状態に戻しておきましょう。

---

## つまずきポイントとヒント

<details>
<summary>BEGIN を打ったのに、別の画面（別セッション）から見ると変更が見えない</summary>

それが正しい挙動です。COMMITする前の変更は、**そのトランザクションの中だけ**で見えます。確定（COMMIT）して初めて他からも見えるようになります。これを「分離（アイソレーション）」と呼びます。今は「コミット前は自分にしか見えない」とだけ覚えればOK。
</details>

<details>
<summary>psql で「autocommit（オートコミット）」って何？</summary>

psqlは普段、1つのSQLを実行するたびに自動で確定（コミット）しています（これがautocommit）。だから普段はUPDATEがすぐ反映されます。`BEGIN` を打つと「ここからは手動でCOMMIT/ROLLBACKするまで確定しない」モードに切り替わります。だから実験では `BEGIN` が起点になります。
</details>

<details>
<summary>ROLLBACK したのに在庫が戻らない</summary>

`BEGIN` を打たずにUPDATEしてしまった可能性があります。BEGINの前のUPDATEは autocommit で即確定されているので、後からROLLBACKしても戻りません。`schema.sql` を流し直し、**必ず BEGIN → UPDATE → ROLLBACK の順** でやり直してください。
</details>

<details>
<summary>現場ではいつトランザクションを使うの？</summary>

「複数の表・複数行を、まとめて変えたい」ときです（在庫を減らす＋注文を作る、など）。途中でエラーが出たら全部ROLLBACKすれば、中途半端なデータが残りません。週3のSpring Bootでは、これをアノテーション（`@Transactional`）で自動化する書き方を学びます。今はその土台として「まとめて成功/まとめて取消」の感覚をつかめれば十分です。
</details>

---

## 模範解答への導線

実験A・Bの結果が確認できたら、[../solutions/09-transaction.sql](../solutions/09-transaction.sql) と見比べてください（操作手順そのものが模範解答です）。
