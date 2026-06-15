package com.example.training;

/**
 * 課題1-1（純粋ロジック）のテスト対象クラス。
 *
 * <p>「純粋（じゅんすい）ロジック」とは、DBや外部サービスに頼らず、
 * 入力だけで答えが決まる計算のこと。テストが一番書きやすい部類なので、
 * 単体テスト入門の最初の題材にしている。
 *
 * <p>このクラス自体は完成済み（=正しく動く）。あなたが書くのは
 * このクラスに対する「テストコード」のほうである。
 */
public class PriceCalculator {

    /** 標準の消費税率（10%）。マジックナンバーを避けるため定数にしている。 */
    private static final double TAX_RATE = 0.10;

    /**
     * 税抜き価格に消費税を足した「税込み価格」を返す。
     *
     * <p>仕様:
     * <ul>
     *   <li>税込み = 税抜き × 1.10 を計算し、小数点以下は切り捨て（floor）て整数で返す。</li>
     *   <li>例: 100 → 110、150 → 165、199 → 218（199×1.1=218.9 を切り捨て）。</li>
     *   <li>税抜き価格が負の数（0未満）のときは IllegalArgumentException を投げる。</li>
     * </ul>
     *
     * @param priceWithoutTax 税抜き価格（0以上）
     * @return 税込み価格（小数点以下切り捨て）
     * @throws IllegalArgumentException 税抜き価格が負のとき
     */
    public int withTax(int priceWithoutTax) {
        if (priceWithoutTax < 0) {
            throw new IllegalArgumentException("価格は0以上で指定してください: " + priceWithoutTax);
        }
        return (int) Math.floor(priceWithoutTax * (1 + TAX_RATE));
    }

    /**
     * 割引後の価格を返す。
     *
     * <p>仕様:
     * <ul>
     *   <li>割引後 = 価格 × (100 - 割引率) ÷ 100 を計算し、小数点以下は切り捨てて整数で返す。</li>
     *   <li>例: (1000, 20) → 800、(150, 10) → 135、(199, 5) → 189。</li>
     *   <li>割引率は 0〜100 の範囲のみ許可。範囲外なら IllegalArgumentException を投げる。</li>
     *   <li>価格が負のときも IllegalArgumentException を投げる。</li>
     * </ul>
     *
     * @param price        元の価格（0以上）
     * @param discountRate 割引率（0〜100）
     * @return 割引後の価格（小数点以下切り捨て）
     * @throws IllegalArgumentException 価格が負、または割引率が0〜100の範囲外のとき
     */
    public int applyDiscount(int price, int discountRate) {
        if (price < 0) {
            throw new IllegalArgumentException("価格は0以上で指定してください: " + price);
        }
        if (discountRate < 0 || discountRate > 100) {
            throw new IllegalArgumentException("割引率は0〜100で指定してください: " + discountRate);
        }
        return price * (100 - discountRate) / 100;
    }
}
