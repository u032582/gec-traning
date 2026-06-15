/**
 * 課題07 自己採点用テストランナー
 * 実行: javac Ex07.java Ex07Test.java && java Ex07Test
 */
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Ex07Test {

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
        Ex07 ex = new Ex07();

        Map<String, Integer> counted = ex.countWords(Arrays.asList("a", "b", "a", "c", "a", "b"));
        Map<String, Integer> expectedCount = new HashMap<>();
        expectedCount.put("a", 3);
        expectedCount.put("b", 2);
        expectedCount.put("c", 1);
        check("countWords", expectedCount, counted);

        Map<String, Integer> m = new HashMap<>();
        m.put("x", 5);
        check("getOrZeroHit", 5, ex.getOrZero(m, "x"));
        check("getOrZeroMiss", 0, ex.getOrZero(m, "y"));

        Map<String, Integer> a = new HashMap<>();
        a.put("x", 1);
        a.put("y", 2);
        Map<String, Integer> b = new HashMap<>();
        b.put("y", 3);
        b.put("z", 4);
        Map<String, Integer> expectedMerge = new HashMap<>();
        expectedMerge.put("x", 1);
        expectedMerge.put("y", 5);
        expectedMerge.put("z", 4);
        check("merge", expectedMerge, ex.merge(a, b));

        Map<String, Integer> maxMap = new HashMap<>();
        maxMap.put("p", 1);
        maxMap.put("q", 9);
        maxMap.put("r", 3);
        check("maxKey", "q", ex.maxKey(maxMap));
        check("maxKeyEmpty", null, ex.maxKey(new HashMap<>()));

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
