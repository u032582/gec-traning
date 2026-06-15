/**
 * 課題12 自己採点用テストランナー
 * 実行: javac Ex12.java Ex12Test.java && java Ex12Test
 */
import java.util.Arrays;
import java.util.List;

public class Ex12Test {

    static int fail = 0;
    static int total = 0;

    static void check(String caseName, Object expected, Object actual) {
        total++;
        if (expected == null ? actual == null : expected.equals(actual)) {
            System.out.println("[PASS] " + caseName);
        } else {
            fail++;
            System.out.println("[FAIL] " + caseName + ": 期待=" + expected + ", 実際=" + actual);
        }
    }

    public static void main(String[] args) {
        Ex12 ex = new Ex12();

        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6);
        check("evens", Arrays.asList(2, 4, 6), ex.evens(nums));
        check("doubled", Arrays.asList(2, 4, 6, 8, 10, 12), ex.doubled(nums));
        check("sum", 21, ex.sum(nums));

        List<Item> items = Arrays.asList(
                new Item("りんご", 100),
                new Item("みかん", 80),
                new Item("メロン", 500)
        );
        check("names", Arrays.asList("りんご", "みかん", "メロン"), ex.names(items));
        // 100以上: りんご(100) + メロン(500) = 600
        check("totalPriceOver", 600, ex.totalPriceOver(items, 100));

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
