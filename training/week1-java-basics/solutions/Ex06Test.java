/**
 * 課題06 自己採点用テストランナー
 * 実行: javac Ex06.java Ex06Test.java && java Ex06Test
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ex06Test {

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
        Ex06 ex = new Ex06();

        List<String> base = new ArrayList<>();
        ex.addAll(base, "a", "bb", "ccc");
        check("addAll", Arrays.asList("a", "bb", "ccc"), base);

        List<String> src = new ArrayList<>(Arrays.asList("a", "bb", "ccc", "dddd"));
        check("removeShort", Arrays.asList("ccc", "dddd"), ex.removeShort(src, 3));
        // 元のListは壊さない
        check("removeShortKeepsOriginal", Arrays.asList("a", "bb", "ccc", "dddd"), src);

        check("join", "a,bb,ccc", ex.join(Arrays.asList("a", "bb", "ccc"), ","));
        check("joinEmpty", "", ex.join(new ArrayList<>(), ","));

        check("indexOf", 1, ex.indexOf(Arrays.asList("a", "bb", "ccc"), "bb"));
        check("indexOfNone", -1, ex.indexOf(Arrays.asList("a", "bb"), "x"));

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
