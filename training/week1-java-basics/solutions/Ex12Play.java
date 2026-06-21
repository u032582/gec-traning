/**
 * 課題12 動かしてみる用ランナー
 * 実行: javac Ex12.java Ex12Play.java && java Ex12Play
 */
import java.util.Arrays;
import java.util.List;

public class Ex12Play {

    public static void main(String[] args) {
        Ex12 ex = new Ex12();

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6);
        System.out.println("元のリスト → " + nums);
        System.out.println("  偶数だけ → " + ex.evens(nums));
        System.out.println("  2倍に → " + ex.doubled(nums));
        System.out.println("  合計 → " + ex.sum(nums));
        System.out.println();

        List<Item> items = Arrays.asList(
                new Item("りんご", 120),
                new Item("みかん", 80),
                new Item("高級メロン", 3000)
        );
        System.out.println("商品リストから名前だけ → " + ex.names(items));
        System.out.println("1000円以上の合計 → " + ex.totalPriceOver(items, 1000) + " 円");
        System.out.println();

        System.out.println("✨ リストが変換されて見えたら、Stream API は動いています！");
        System.out.println("次に Ex12Test で正解判定してください。");
    }
}
