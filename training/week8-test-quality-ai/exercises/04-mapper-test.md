# 課題04: モックで守れないものを守る — Mapper層のテスト

**所要時間の目安**: 90分
**AI利用区分**: ✅ AIにテストを書かせてよい。ただし「そのテストが何を守っているか」の判断は自分がすること。

---

## 1. 背景・狙い（現場でどう使うか）

課題01〜03で使ったテストは、すべて **Mockito でモックを置いたテスト**でした。モックは速くて便利です。でも、ここまでで薄々気づいているはずです——**モックを置いた瞬間、そのテストが守れるのは「モックの内側」だけ**になります。

たとえば「すでに返却済みの本は、二重に返却できない」。このルールは、実は **SQL の `WHERE` 句**が守っています。ところがモックを使ったテストでは、その SQL の代わりに `thenReturn(0)` と**人間が答えを書いてしまう**。すると、SQL からその条件が消えても、テストは緑のままです。

> **誰も気づかないまま壊れる。** これが今回踏む落とし穴です。

現場では「テストが全部緑だから安心」と言った次の日に本番が壊れます。**モックが肩代わりしている範囲はどこまでか**を知り、そこから漏れるものを **Mapper層のテスト**で拾う——それが今回の狙いです。

> 用語メモ: **Mapper層のテスト** … Mockitoを使わず、**実際のデータベースに繋いで SQL を本当に走らせる**テスト。SQL の書き間違い（`WHERE` の条件漏れ、`ORDER BY` の消滅など）は、SQL が走らないと分からないので、このテストでしか守れない。現場では「結合テスト」「インテグレーションテスト」と呼ばれることも多い。
> 用語メモ: **`@MybatisTest`** … 「Mapper とDB接続だけを起動してテストする」ためのSpringの印。アプリ全部（Controller や Service）は起動しないので速い。**テストの中で入れたデータは、テストが終わると自動で取り消される**（＝ロールバック）ので、あなたの `training` データベースは汚れない。

---

## 2. 対象

題材リポ [project-repo](../../project-repo/) を使います（課題01〜03の `test-quality-lab` ではありません）。週6で読み、週9〜11で実際に手を入れていく、あのリポです。

見るのは次の2つ。**先に両方を開いて読んでください。**

| ファイル | 見るところ |
|---|---|
| [mapper/LendingMapper.xml](../../project-repo/src/main/resources/mapper/LendingMapper.xml) | `markReturned` の SQL。`WHERE` に `AND returned_at IS NULL` がある |
| [LendingServiceTest.java](../../project-repo/src/test/java/com/example/training/lending/LendingServiceTest.java) | `返却失敗_すでに返却済みなら例外で在庫は戻さない()`。`markReturned` を `thenReturn(0)` でモックしている |

**この2つを見比べてください。** SQL が守っているルール（返却済みなら1行も更新しない）を、テストの側では人間が `0` と手で書いている。ここが今日の急所です。

---

## 3. 準備（先にやる）

1. PostgreSQL が動いていること。止まっていたら起動します。
   ```bash
   sudo service postgresql start
   ```
2. `training` データベースにテーブルとサンプルデータがあること（週6でやったはず。不安なら流し直す）。
   ```bash
   cd training/project-repo
   psql -h localhost -U postgres -d training -f db/schema.sql
   ```
3. いまの状態でテストが緑になること。
   ```bash
   ./gradlew test
   ```

> ⚠️ 今回のテストは、**あなたの `training` データベースに本当に繋ぎます**。ただしテストが入れたデータは終了時に自動で取り消される（ロールバック）ので、データが増えたり消えたりする心配はありません。安心して進めてください。

---

## 4. やること

### ステップ1: 壊してみる（ここが体験の本体）

[LendingMapper.xml](../../project-repo/src/main/resources/mapper/LendingMapper.xml) の `markReturned` から、**`AND returned_at IS NULL` の1行を消します**（あとで戻すので、コメントアウトでもOK）。

```xml
    <update id="markReturned">
        UPDATE lendings
        SET returned_at = #{returnedAt}
        WHERE id = #{id}
        <!-- AND returned_at IS NULL  ← これを消した -->
    </update>
```

この状態でテストを流します。

```bash
./gradlew test
```

**全部緑のままです。** 二重返却を防ぐ条件を消したのに、既存のテストは1つも気づきません。

いま何が起きたか、品質ノートに一言で書いてください。**この「緑のまま」が、今日いちばん大事な体験です。**

### ステップ2: Mapper層のテストを書く

条件を**消したまま**、次の場所に新しいテストを作ります。

```
training/project-repo/src/test/java/com/example/training/lending/LendingMapperTest.java
```

骨組みは次のとおりです。**印（アノテーション）の部分はそのまま写して構いません**——ここは初めて見る書き方なので、真似るところから始めます。中身（テストの本体）を自分で書いてください。

```java
package com.example.training.lending;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

/**
 * LendingMapper の「SQLそのもの」を確かめるテスト。
 * モックを使わず、実際の PostgreSQL に繋いで SQL を走らせる。
 */
@MybatisTest                                                        // Mapper とDB接続だけ起動する
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // 本物のDBを使う
class LendingMapperTest {

    @Autowired
    private LendingMapper lendingMapper;   // モックではない。本物が入る

    @Test
    void 二重返却_すでに返却済みの記録は更新されず返却日も変わらない() {
        // TODO: ここを書く
        //  1. 「すでに返却済み」の貸出記録を1件 insert する
        //     （book_id / member_id は、サンプルデータにある 1L を使う）
        //  2. その記録に markReturned を呼ぶ
        //  3. 更新件数が 0 であること、返却日が書き換わっていないことを assert する
    }
}
```

> ヒント: `lendingMapper.insert(lending)` を呼ぶと、採番された id が `lending` にセットされます（XMLの `useGeneratedKeys` のおかげ）。`lendingMapper.findById(id)` で入れた記録を読み直せます。

### ステップ3: 赤を見る

書けたら流します。

```bash
./gradlew test
```

**あなたの書いた `LendingMapperTest` だけが赤（FAILED）になるはずです。** 既存の `LendingServiceTest` は緑のまま。

この赤が「SQL から条件が消えている」という指差しです。**モックのテストには見えなかったものが、Mapper層のテストには見えた。** これが今日の答えです。

赤が出なかったら、テストが SQL を本当に確かめられていません。品質ノートに書く前に、何を assert しているか見直してください。

### ステップ4: 直して緑にする

消した `AND returned_at IS NULL` を**元に戻します**。もう一度流して、**全部緑**になることを確認してください。

```bash
./gradlew test
```

### ステップ5: もう1本、正常系も守る（推奨）

「返却済みなら更新しない」だけだと、`WHERE` が厳しすぎて**何も更新しない**SQLでも緑になってしまいます。**貸出中の記録はちゃんと1件更新される**ことを確かめるテストも足してください。「守る」は、通すべきものを通すことまで含みます。

---

## 品質ノート（この課題の提出物・必ず埋める）

```
【ステップ1で何が起きたか（条件を消したのにテストが緑だった理由）】


【なぜ LendingServiceTest では、この壊れを検出できないのか】


【書いたテストは何を守っているか（1本ずつ）】
 - 二重返却のテスト:
 - 正常系のテスト  :

【モックのテストと Mapper層のテスト、それぞれ「守れる範囲」はどこまでか】


【この題材以外に、いま自分のコードで "モックでは守れていない" ものはあるか】

```

---

## 5. 完成条件（自分で判定できる）

- [ ] ステップ1で、SQLの条件を消しても既存テストが**全部緑のまま**であることを、自分の目で見た
- [ ] `LendingMapperTest` を書き、条件を消した状態で**赤（FAILED）になる**ことを確認した
- [ ] 条件を戻したら**全部緑**になることを確認した
- [ ] 正常系（貸出中の記録は1件更新される）のテストも書いた
- [ ] 品質ノートに「モックが守れる範囲／守れない範囲」を自分の言葉で書いた

---

## 6. つまずきポイントとヒント

<details>
<summary>「Failed to replace DataSource with an embedded database」というエラーが出る</summary>

`@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)` が付いていないと、Spring は「テスト用の使い捨てDBに差し替えよう」として、それが見つからず失敗します。骨組みのとおり、この1行を付けてください。**「本物のDBをそのまま使う」という宣言**です。
</details>

<details>
<summary>「Connection refused」「could not connect to server」で落ちる</summary>

PostgreSQL が起動していません。WSL2は再起動すると止まります。

```bash
sudo service postgresql start
```

自分のコードが悪いのか環境が悪いのかを切り分けるのは、現場で毎日やることです。**エラーメッセージが「繋がらない」と言っているなら、まず繋ぎ先を疑う。**
</details>

<details>
<summary>テストで insert したデータが、DBに残っていない</summary>

それで正しいです。`@MybatisTest` は、テストが終わると**入れたデータを自動で取り消します**（ロールバック）。テストのたびにデータが増えていったら、次のテストが前のテストの影響を受けてしまうからです。「テストは、前のテストの後片付けを当てにしない」——これは現場でも通じる原則です。
</details>

<details>
<summary>外部キー制約（foreign key constraint）でエラーになる</summary>

`lendings` の `book_id` / `member_id` は、`books` / `members` に**実在する行しか指せません**。存在しない `999L` などを入れると弾かれます。サンプルデータにある `1L` を使ってください。不安なら `psql` で確認できます。
</details>

<details>
<summary>AIに書かせたら @SpringBootTest を出してきた</summary>

動きはしますが、**アプリ全部（Controller / Service 含む）を起動する**ので遅く、しかも「Mapperだけを確かめたい」という意図がテストから読み取れなくなります。`@MybatisTest` は「Mapper とDB接続だけ」を起動する印です。**AIの出したものが動く＝それが適切、ではありません。** 採用/却下の理由を品質ノートに書けるかどうかが、今週ずっと問われていることです。
</details>

<details>
<summary>そもそも、なんで Service のテストじゃダメなの？</summary>

`LendingServiceTest` は `markReturned` を `thenReturn(0)` とモックしています。つまり「返却済みなら0件」という**SQLの仕事を、テストを書いた人が手で肩代わりしている**。SQLがその仕事をやめても、モックは変わらず0を返し続けます。だから気づけません。

守りたいものが「Serviceの判断」なら Service のテストで足ります。守りたいものが「SQLの正しさ」なら、**SQLを実際に走らせるしかない**。テストの層は、守りたい対象で選びます。
</details>

---

## 7. 模範への導線

自分で書いてから、[solutions/04-quality-note.md](../solutions/04-quality-note.md) と見比べてください。**先に自分で。** 模範のテストコードも載せていますが、大事なのはコードより「モックが守れる範囲はどこまでか」を言葉にできているかです。
