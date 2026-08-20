# 模範: 課題04 品質ノート（記入例）＋ 模範テスト

> これは**記入の見本**です。大事なのは「モックが守れる範囲はどこまでか」を、自分の言葉で線引きできていること。

---

## 品質ノート（記入例）

【ステップ1で何が起きたか（条件を消したのにテストが緑だった理由）】
 `markReturned` の SQL から `AND returned_at IS NULL` を消した。これは「まだ返していないものだけを返却済みにする」＝**二重返却を防ぐ条件**。消せば、すでに返却済みの記録でも返却日が上書きされてしまう。なのに `./gradlew test` は全部緑だった。**既存のテストは、この SQL を1回も走らせていない**から。

【なぜ LendingServiceTest では、この壊れを検出できないのか】
 `LendingServiceTest` は `when(lendingMapper.markReturned(...)).thenReturn(0)` とモックしている。つまり「返却済みなら0件更新」という**SQLの仕事を、テストを書いた人が手で書いている**。SQLがその仕事をやめても、モックは変わらず0を返す。テストが確かめているのは「0が返ってきたとき Service が例外を投げるか」だけで、**0が返ってくること自体は確かめていない**。

【書いたテストは何を守っているか（1本ずつ）】
 - 二重返却のテスト: `markReturned` の `WHERE` に「未返却のものだけ」という条件が入っていること。更新件数0と、最初の返却日が上書きされないことの2つで守っている。この条件が消えたら赤くなる。
 - 正常系のテスト  : 逆に、貸出中の記録は**ちゃんと1件更新され、返却日が入る**こと。`WHERE` を厳しくしすぎて何も通らなくなる壊れ方を、こちらで守っている。

【モックのテストと Mapper層のテスト、それぞれ「守れる範囲」はどこまでか】
 - モックのテスト（Service層）: **Serviceの判断**を守る。「Mapperが0を返したら例外を投げる」「在庫を戻さない」といった分岐。速いし、DBが要らない。ただし**モックの向こう側（SQLの中身）は一切守れない**。
 - Mapper層のテスト: **SQLそのもの**を守る。`WHERE` の条件、`ORDER BY` の並び順、列名の綴り。DBが必要で少し遅いが、SQLの壊れはここでしか捕まえられない。

 境界は「モックを置いた線」。**モックを置いた瞬間、その先は自分のテストの守備範囲から外れる**。外れた分をどこで拾うかを決めるのが、テストを書く人の仕事。

【この題材以外に、いま自分のコードで "モックでは守れていない" ものはあるか】
 `findByMemberId` の `ORDER BY lent_at DESC, id DESC`。Service のテストは Mapper が返したリストをそのまま検証しているだけなので、`ORDER BY` が消えても気づけない。並び順が仕様なら、Mapper層のテストで守るべき。

---

## 模範テスト

`training/project-repo/src/test/java/com/example/training/lending/LendingMapperTest.java`

```java
package com.example.training.lending;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

/**
 * {@link LendingMapper} の「SQLそのもの」を確かめるテスト。
 *
 * <p>モックを使わず、実際の PostgreSQL に繋いで SQL を走らせる。
 * Service のテスト（モック）では守れない「WHERE の条件」を、ここで守る。
 *
 * <p>テストの中で入れたデータは、テスト終了時に自動で取り消される（ロールバック）。
 */
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LendingMapperTest {

    @Autowired
    private LendingMapper lendingMapper;

    private static final LocalDate LENT_AT = LocalDate.of(2026, 7, 1);
    private static final LocalDate DUE_DATE = LocalDate.of(2026, 7, 15);

    /**
     * テスト用の貸出記録を1件作る。
     * book_id / member_id はサンプルデータに実在する 1 を使う（外部キー制約があるため）。
     *
     * @param returnedAt null なら「貸出中」、日付を入れれば「返却済み」の記録になる
     */
    private Lending insertLending(LocalDate returnedAt) {
        Lending lending = new Lending();
        lending.setBookId(1L);
        lending.setMemberId(1L);
        lending.setLentAt(LENT_AT);
        lending.setDueDate(DUE_DATE);
        lending.setReturnedAt(returnedAt);
        lendingMapper.insert(lending);   // 採番された id が lending にセットされる
        return lending;
    }

    @Test
    void 返却_貸出中の記録は1件更新され返却日が入る() {
        Lending lending = insertLending(null);   // 貸出中
        LocalDate returnDate = LocalDate.of(2026, 7, 20);

        int updated = lendingMapper.markReturned(lending.getId(), returnDate);

        assertThat(updated).isEqualTo(1);
        assertThat(lendingMapper.findById(lending.getId()).getReturnedAt()).isEqualTo(returnDate);
    }

    @Test
    void 二重返却_すでに返却済みの記録は更新されず返却日も変わらない() {
        LocalDate firstReturn = LocalDate.of(2026, 7, 10);
        Lending lending = insertLending(firstReturn);   // すでに返却済み

        int updated = lendingMapper.markReturned(lending.getId(), LocalDate.of(2026, 7, 20));

        // WHERE の returned_at IS NULL が効いていれば、1行も更新されない。
        assertThat(updated).isZero();
        // 最初の返却日が上書きされていないこと。
        assertThat(lendingMapper.findById(lending.getId()).getReturnedAt()).isEqualTo(firstReturn);
    }
}
```

---

## この記入例で見てほしいポイント

- 「テストが緑だった理由」を **「SQLを1回も走らせていないから」**と、原因の在り処で言えている（「テストが弱い」で止まっていない）。
- モックの正体を **「SQLの仕事を人間が手で書いている」**と言語化できている。ここが今回の核心。
- 守備範囲を **「モックを置いた線」** という一言で線引きできている。この線引きは、これから書くすべてのテストに効く。
- 二重返却（通してはいけない）だけでなく、**正常系（通すべき）**も守っている。片方だけだと「何も更新しないSQL」でも緑になってしまう。
- 最後の欄で、`ORDER BY` という**別の"モックでは守れないもの"**に自分で気づけている。今日の視点が、他のコードにも効き始めた証拠。
