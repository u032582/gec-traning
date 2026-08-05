package com.example.training.lending;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * 課題03: OverdueChecker の silent failure を暴く／正常系を守る。
 *
 * (a) dueDate が null のとき、false で握りつぶさず例外になってほしい
 *     → 今の実装では RED（それが「問題がある」証拠）
 * (b) 期限切れ未返却→true、返却済み→false → GREEN
 */
class OverdueCheckerTest {

    private final OverdueChecker checker = new OverdueChecker();
    private final LocalDate asOf = LocalDate.of(2026, 8, 5);

    /**
     * (a) 危うさを暴くテスト。
     * あるべき姿: dueDate が null のような異常データは、false ではなく例外で気づかせる。
     * 今の実装は catch で false を返すので、このテストは赤になる想定。
     */
    @Test
    void dueDateがnullなら例外になるべき_いまは握りつぶしで赤になる想定() {
        Lending lending = new Lending();
        lending.setDueDate(null);      // 異常データ
        lending.setReturnedAt(null);   // 未返却

        assertThatThrownBy(() -> checker.isOverdue(lending, asOf))
                .isInstanceOf(RuntimeException.class);
    }

    /** (b) 正常系: 期限切れ・未返却なら延滞 */
    @Test
    void 期限切れで未返却なら延滞() {
        Lending lending = new Lending();
        lending.setDueDate(LocalDate.of(2026, 7, 1)); // 過去
        lending.setReturnedAt(null);

        assertThat(checker.isOverdue(lending, asOf)).isTrue();
    }

    /** (b) 正常系: 返却済みなら延滞ではない */
    @Test
    void 返却済みなら延滞ではない() {
        Lending lending = new Lending();
        lending.setDueDate(LocalDate.of(2026, 7, 1));
        lending.setReturnedAt(LocalDate.of(2026, 7, 10));

        assertThat(checker.isOverdue(lending, asOf)).isFalse();
    }
}
