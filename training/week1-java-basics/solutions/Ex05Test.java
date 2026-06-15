/**
 * 課題05 自己採点用テストランナー
 * 実行: javac Ex05.java Ex05Test.java && java Ex05Test
 */
import java.util.Arrays;

public class Ex05Test {

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

    static void checkArray(String caseName, int[] expected, int[] actual) {
        total++;
        if (Arrays.equals(expected, actual)) {
            System.out.println("[PASS] " + caseName);
        } else {
            fail++;
            System.out.println("[FAIL] " + caseName + ": 期待=" + Arrays.toString(expected)
                    + ", 実際=" + Arrays.toString(actual));
        }
    }

    public static void main(String[] args) {
        Ex05 ex = new Ex05();

        check("sum", 15, ex.sum(new int[]{1, 2, 3, 4, 5}));
        check("sumEmpty", 0, ex.sum(new int[]{}));

        check("max", 9, ex.max(new int[]{3, 9, 1, 7}));
        check("maxNeg", -1, ex.max(new int[]{-5, -1, -3}));

        check("count", 3, ex.count(new int[]{1, 2, 2, 3, 2}, 2));
        check("count0", 0, ex.count(new int[]{1, 2, 3}, 9));

        checkArray("reverse", new int[]{5, 4, 3, 2, 1}, ex.reverse(new int[]{1, 2, 3, 4, 5}));

        // reverse は元配列を壊さないこと
        int[] original = {1, 2, 3};
        ex.reverse(original);
        checkArray("reverseKeepsOriginal", new int[]{1, 2, 3}, original);

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
