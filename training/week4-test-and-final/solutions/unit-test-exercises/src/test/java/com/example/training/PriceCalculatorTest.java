package com.example.training;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 課題1-1 模範解答: PriceCalculator の単体テスト。
 *
 * <p>JUnit5 の基本だけで書いている。
 * <ul>
 *   <li>{@code @Test} … このメソッドが1つのテストであることの目印。</li>
 *   <li>{@code assertEquals(期待, 実際)} … 2つが等しいか確認。違えば失敗。</li>
 *   <li>{@code assertThrows(例外クラス, 処理)} … その処理が指定の例外を投げるか確認。</li>
 *   <li>{@code @DisplayName} … テスト結果に出る日本語の説明（任意）。</li>
 * </ul>
 */
class PriceCalculatorTest {

    // テスト対象。各テストの前に new しておく。
    private final PriceCalculator calculator = new PriceCalculator();

    // ---- withTax: 正常系 -------------------------------------------------

    @Test
    @DisplayName("税込み: 100円 → 110円")
    void withTax_100() {
        assertEquals(110, calculator.withTax(100));
    }

    @Test
    @DisplayName("税込み: 150円 → 165円")
    void withTax_150() {
        assertEquals(165, calculator.withTax(150));
    }

    @Test
    @DisplayName("税込み: 199円 → 218円（小数点以下は切り捨て）")
    void withTax_roundDown() {
        // 199 × 1.1 = 218.9 → 切り捨てて 218
        assertEquals(218, calculator.withTax(199));
    }

    @Test
    @DisplayName("税込み: 0円 → 0円（境界値）")
    void withTax_zero() {
        assertEquals(0, calculator.withTax(0));
    }

    // ---- withTax: 異常系 -------------------------------------------------

    @Test
    @DisplayName("税込み: 負の価格は例外")
    void withTax_negative() {
        assertThrows(IllegalArgumentException.class, () -> calculator.withTax(-1));
    }

    // ---- applyDiscount: 正常系 ------------------------------------------

    @Test
    @DisplayName("割引: 1000円の20%引き → 800円")
    void applyDiscount_20percent() {
        assertEquals(800, calculator.applyDiscount(1000, 20));
    }

    @Test
    @DisplayName("割引: 150円の10%引き → 135円")
    void applyDiscount_10percent() {
        assertEquals(135, calculator.applyDiscount(150, 10));
    }

    @Test
    @DisplayName("割引: 割引率0% → 価格そのまま（境界値）")
    void applyDiscount_zeroRate() {
        assertEquals(500, calculator.applyDiscount(500, 0));
    }

    @Test
    @DisplayName("割引: 割引率100% → 0円（境界値）")
    void applyDiscount_fullRate() {
        assertEquals(0, calculator.applyDiscount(500, 100));
    }

    // ---- applyDiscount: 異常系 ------------------------------------------

    @Test
    @DisplayName("割引: 割引率がマイナスは例外")
    void applyDiscount_negativeRate() {
        assertThrows(IllegalArgumentException.class, () -> calculator.applyDiscount(1000, -1));
    }

    @Test
    @DisplayName("割引: 割引率が100超は例外")
    void applyDiscount_over100Rate() {
        assertThrows(IllegalArgumentException.class, () -> calculator.applyDiscount(1000, 101));
    }

    @Test
    @DisplayName("割引: 価格がマイナスは例外")
    void applyDiscount_negativePrice() {
        assertThrows(IllegalArgumentException.class, () -> calculator.applyDiscount(-1, 10));
    }
}
