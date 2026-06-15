/**
 * 課題09 自己採点用テストランナー
 * 実行: javac Ex09.java Ex09Test.java && java Ex09Test
 */
public class Ex09Test {

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
        Ex09 ex = new Ex09();

        check("safeDivide", 3, ex.safeDivide(6, 2));
        check("safeDivideZero", 0, ex.safeDivide(6, 0));

        check("parseOK", 123, ex.parseOrDefault("123", -1));
        check("parseNG", -1, ex.parseOrDefault("abc", -1));
        check("parseNull", -1, ex.parseOrDefault(null, -1));

        // 正常系: 引き出せる
        try {
            check("withdrawOK", 700, ex.withdraw(1000, 300));
        } catch (InsufficientBalanceException e) {
            fail++;
            total++;
            System.out.println("[FAIL] withdrawOK: 例外が出てはいけないのに出た -> " + e.getMessage());
        }

        // 異常系: 残高不足なら例外が投げられること
        total++;
        try {
            ex.withdraw(1000, 1500);
            fail++;
            System.out.println("[FAIL] withdrawNG: 例外が投げられるべきなのに投げられなかった");
        } catch (InsufficientBalanceException e) {
            System.out.println("[PASS] withdrawNG");
        }

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
