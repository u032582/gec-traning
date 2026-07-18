package com.example.training.lending;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * ⚠️【週8 練習用・出来の悪いテストの見本】
 *
 * <p>ここにあるテストは、<b>すべて緑（PASSED）になります</b>。でも、どれも
 * 「AIが出しがちだが、実は役に立たない/危ないテスト」の見本です。
 * 緑だからといって、良いテストとは限りません。
 *
 * <p>週8の課題で、この1本1本を「採用/却下」で判定し、なぜダメかを言語化します。
 * <b>このファイルは書き換えず、批評の対象として読んでください</b>
 * （良いテストは別ファイルに自分で書きます）。
 */
class BadTestExamplesTest {

    // -----------------------------------------------------------------
    // 見本A: 何も確かめていない（トートロジー＝同義反復）アサーション
    // -----------------------------------------------------------------
    @Test
    void 見本A_延滞判定を呼ぶ() {
        OverdueChecker checker = new OverdueChecker();
        Lending lending = new Lending();
        lending.setDueDate(LocalDate.of(2026, 1, 1));

        boolean result = checker.isOverdue(lending, LocalDate.of(2026, 7, 18));

        // result == result は必ず true。何も確かめていない。
        assertThat(result).isEqualTo(result);
    }

    // -----------------------------------------------------------------
    // 見本B: メソッドを呼ぶだけ（カバレッジ稼ぎ）。結果を検証していない
    // -----------------------------------------------------------------
    @Test
    void 見本B_例外なく動くことだけ確認() {
        OverdueChecker checker = new OverdueChecker();
        Lending lending = new Lending();
        lending.setDueDate(LocalDate.of(2026, 1, 1));

        // 呼ぶだけ。戻り値も副作用も何も見ていない。
        // カバレッジ（実行された行の割合）の数字は上がるが、正しさは1ミリも保証しない。
        checker.isOverdue(lending, LocalDate.of(2026, 7, 18));
    }

    // -----------------------------------------------------------------
    // 見本C: モックの戻り値をそのまま確認している（実装ではなくモックをテスト）
    // -----------------------------------------------------------------
    @Test
    void 見本C_モックが返した値をそのまま確認() {
        // Lending をモックにして「dueDateはこれ」と仕込み、その仕込んだ値を確認しているだけ。
        // OverdueChecker の判定ロジックは1ミリも通っていない。テスト対象を取り違えている。
        Lending lending = mock(Lending.class);
        org.mockito.Mockito.when(lending.getDueDate()).thenReturn(LocalDate.of(2026, 1, 1));

        assertThat(lending.getDueDate()).isEqualTo(LocalDate.of(2026, 1, 1));
    }

    // -----------------------------------------------------------------
    // 見本D: silent failure を「仕様」として固定してしまっている危ないテスト
    // -----------------------------------------------------------------
    @Test
    void 見本D_dueDateがnullでも延滞ではない() {
        OverdueChecker checker = new OverdueChecker();
        Lending lending = new Lending();
        lending.setDueDate(null);   // 返却期限が入っていない（データ不整合）

        // OverdueChecker は内部で例外を握りつぶして false を返す。
        // このテストはその「握りつぶし」を "正しい仕様" として緑で固定してしまう。
        // 本当は「dueDate が null なんて異常事態を、静かに false で流していいのか？」を
        // 問うべき場面。緑だが、バグを追認しているだけの危ないテスト。
        assertThat(checker.isOverdue(lending, LocalDate.of(2026, 7, 18))).isFalse();
    }
}
