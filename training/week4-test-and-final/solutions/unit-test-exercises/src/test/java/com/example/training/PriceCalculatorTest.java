package com.example.training;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
class PriceCalculatorTest {
    @Test
    void withTax_100円なら110円() {
    PriceCalculator calc = new PriceCalculator();
    assertEquals(110, calc.withTax(100));
}
@Test
void withTax_150円なら165円() {
    PriceCalculator calc = new PriceCalculator();
    assertEquals(165, calc.withTax(150));
}
@Test
void withTax_199円なら218円() {
    PriceCalculator calc = new PriceCalculator();
    assertEquals(218, calc.withTax(199));
}
@Test
void withTax_0円なら0円() {
    PriceCalculator calc = new PriceCalculator();
    assertEquals(0, calc.withTax(0));
}
@Test
void withTax_負の価格は例外() {
    PriceCalculator calc = new PriceCalculator();
    assertThrows(IllegalArgumentException.class, () -> calc.withTax(-1));
}
@Test
void applyDiscount_1000円の20パーセント引きなら800円() {
    PriceCalculator calc = new PriceCalculator();
    assertEquals(800, calc.applyDiscount(1000, 20));
}
@Test
void applyDiscount_150円の10パーセント引きなら135円() {
    PriceCalculator calc = new PriceCalculator();
    assertEquals(135, calc.applyDiscount(150, 10));
}
@Test
void applyDiscount_0パーセント引きなら価格そのまま() {
    PriceCalculator calc = new PriceCalculator();
    assertEquals(500, calc.applyDiscount(500, 0));
}
@Test
void applyDiscount_100パーセント引きなら0円() {
    PriceCalculator calc = new PriceCalculator();
    assertEquals(0, calc.applyDiscount(500, 100));
}
@Test
void applyDiscount_割引率がマイナスは例外(){
    PriceCalculator calc = new PriceCalculator();
    assertThrows(IllegalArgumentException.class, () -> calc.applyDiscount(1000, -1));
}
@Test
void applyDiscount_割引率が100超は例外() {
    PriceCalculator calc = new PriceCalculator();
    assertThrows(IllegalArgumentException.class, () -> calc.applyDiscount(1000, 101));
}
@Test
void applyDiscount_価格がマイナスは例外() {
    PriceCalculator calc = new PriceCalculator();
    assertThrows(IllegalArgumentException.class, () -> calc.applyDiscount(-1, 10));
}
}