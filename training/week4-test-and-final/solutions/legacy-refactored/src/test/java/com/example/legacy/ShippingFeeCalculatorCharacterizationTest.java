package com.example.legacy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ShippingFeeCalculator の「振る舞い固定テスト」（characterization test）。
 *
 * <p>このテストは「正しい仕様」を表したものではなく、
 * <b>“いまのコードが実際にどう動くか”をそのまま写し取って固定</b>したものである。
 *
 * <p>課題3（コードリーディング+小改修）のルール:
 * <ul>
 *   <li>あなたはコードを「読みやすく・重複なく」直してよい。</li>
 *   <li>ただし、<b>このテストが改修後も全部緑のまま</b>でなければならない。</li>
 *   <li>= 見た目を直しても、外から見た振る舞いは1mmも変えてはいけない、という意味。</li>
 * </ul>
 *
 * <p>※ このテストには「現状の挙動」がそのまま固定されている。中には
 *    「本当は望ましくない挙動」も含まれているかもしれない。だが今回の改修では
 *    “振る舞いを変えない”のが鉄則なので、まずはこの網を通すことを最優先にする。
 */
class ShippingFeeCalculatorCharacterizationTest {

    private final ShippingFeeCalculator calculator = new ShippingFeeCalculator();

    @Test
    @DisplayName("calc: east/west/その他 を1件ずつ合算する")
    void calc_mixedRegions() {
        // east 1500g → 500, west 3000g → 900, north(その他) 6000g → 1500
        List<String> lines = List.of(
                "east,1500",
                "west,3000",
                "north,6000");
        assertEquals(500 + 900 + 1500, calculator.calc(lines));
    }

    @Test
    @DisplayName("calc: 重量の境界値（2000ちょうど/5000ちょうど）")
    void calc_boundary() {
        // east 2000g → 500（2000以下）, east 5000g → 800（5000以下）, east 5001g → 1200
        List<String> lines = List.of(
                "east,2000",
                "east,5000",
                "east,5001");
        assertEquals(500 + 800 + 1200, calculator.calc(lines));
    }

    @Test
    @DisplayName("calc: 壊れた行は無視されて合算に含まれない（現状の挙動）")
    void calc_brokenLineIsSkipped() {
        // "east" だけ（重量がない）→ 例外が握りつぶされてスキップ。
        // "east,abc" → 数値変換に失敗してスキップ。
        // 有効なのは west,1000(→600) のみ。
        List<String> lines = List.of(
                "east",
                "east,abc",
                "west,1000");
        assertEquals(600, calculator.calc(lines));
    }

    @Test
    @DisplayName("calc: 空リストは0")
    void calc_empty() {
        assertEquals(0, calculator.calc(List.of()));
    }

    @Test
    @DisplayName("calcWithDiscount: ランク3は20%引き")
    void calcWithDiscount_rank3() {
        // east 1500 → 500。 500 - (500*20/100) = 500 - 100 = 400
        assertEquals(400, calculator.calcWithDiscount(List.of("east,1500"), 3));
    }

    @Test
    @DisplayName("calcWithDiscount: ランク2は10%引き")
    void calcWithDiscount_rank2() {
        // west 3000 → 900。 900 - (900*10/100) = 900 - 90 = 810
        assertEquals(810, calculator.calcWithDiscount(List.of("west,3000"), 2));
    }

    @Test
    @DisplayName("calcWithDiscount: ランク1は5%引き")
    void calcWithDiscount_rank1() {
        // east 1500 → 500。 500 - (500*5/100) = 500 - 25 = 475
        assertEquals(475, calculator.calcWithDiscount(List.of("east,1500"), 1));
    }

    @Test
    @DisplayName("calcWithDiscount: ランク0（対象外）は割引なし")
    void calcWithDiscount_rank0() {
        assertEquals(500, calculator.calcWithDiscount(List.of("east,1500"), 0));
    }

    @Test
    @DisplayName("calcWithDiscount: 複数行の合算にランク3割引")
    void calcWithDiscount_multi() {
        // east 1500(500) + west 3000(900) = 1400。 1400 - 280 = 1120
        List<String> lines = List.of("east,1500", "west,3000");
        assertEquals(1120, calculator.calcWithDiscount(lines, 3));
    }
}
