package com.example.training.lending;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class MyGoodTest {
    
    @Test
    void 期限切れで未返却なら延滞() {
        OverdueChecker checker = new OverdueChecker();
        Lending lending = new Lending();
        lending.setDueDate(LocalDate.of(2026, 1, 1)); // 過去の期限
        // returnedAt は null のまま（未返却）
        boolean result = checker.isOverdue(lending, LocalDate.of(2026, 7, 18));
        assertThat(result).isTrue();  // ← 期待は「true」。result同士ではない
    }
}
