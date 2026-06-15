/**
 * 課題08 自己採点用テストランナー
 * 実行: javac Ex08.java Ex08Test.java && java Ex08Test
 */
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Ex08Test {

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

    static Set<Integer> setOf(Integer... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    public static void main(String[] args) {
        Ex08 ex = new Ex08();

        check("unique", 3, ex.unique(Arrays.asList(1, 2, 2, 3, 3, 3)));
        check("uniqueAll", 4, ex.unique(Arrays.asList(1, 2, 3, 4)));

        check("hasDupTrue", true, ex.hasDuplicate(Arrays.asList(1, 2, 2)));
        check("hasDupFalse", false, ex.hasDuplicate(Arrays.asList(1, 2, 3)));

        check("intersection", setOf(2, 3), ex.intersection(setOf(1, 2, 3), setOf(2, 3, 4)));
        check("intersectionEmpty", setOf(), ex.intersection(setOf(1, 2), setOf(3, 4)));

        check("difference", setOf(1), ex.difference(setOf(1, 2, 3), setOf(2, 3, 4)));
        check("differenceAll", setOf(1, 2), ex.difference(setOf(1, 2), setOf(3, 4)));

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
