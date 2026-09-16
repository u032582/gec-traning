/**
 * 付録01 自己採点用テストランナー
 * 実行: javac Col01.java Col01Test.java && java Col01Test
 */
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Col01Test {

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
        Col01 col = new Col01();

        List<String> borrowed = Arrays.asList(
                "リーダブルコード", "ノルウェイの森", "リーダブルコード",
                "人月の神話", "ノルウェイの森", "リーダブルコード");

        // ① List: 新しい順に3冊。重複はそのまま残る。
        check("latest3",
                Arrays.asList("リーダブルコード", "ノルウェイの森", "人月の神話"),
                col.latest3(borrowed));
        check("latest3Short",
                Arrays.asList("B", "A"),
                col.latest3(Arrays.asList("A", "B")));
        check("latest3Empty",
                Arrays.asList(),
                col.latest3(Arrays.asList()));

        // ② Set: 重複が消える。
        check("kinds", 3, col.kinds(borrowed));
        check("kindsAllSame", 1, col.kinds(Arrays.asList("A", "A", "A")));
        check("kindsEmpty", 0, col.kinds(Arrays.asList()));

        // ③ Map: キーごとの件数。
        Map<String, Integer> expected = new HashMap<>();
        expected.put("リーダブルコード", 3);
        expected.put("ノルウェイの森", 2);
        expected.put("人月の神話", 1);
        check("countByTitle", expected, col.countByTitle(borrowed));
        check("countByTitleEmpty", new HashMap<String, Integer>(), col.countByTitle(Arrays.asList()));

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
