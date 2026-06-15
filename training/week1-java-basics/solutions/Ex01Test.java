/**
 * 課題01 自己採点用テストランナー
 * 実行: javac Ex01.java Ex01Test.java && java Ex01Test
 */
public class Ex01Test {

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
        Ex01 ex = new Ex01();

        check("greeting1", "こんにちは、田中さん！", ex.greeting("田中"));
        check("greeting2", "こんにちは、佐藤さん！", ex.greeting("佐藤"));
        check("introduce1", "田中（25歳）です。", ex.introduce("田中", 25));
        check("introduce2", "鈴木（30歳）です。", ex.introduce("鈴木", 30));

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
