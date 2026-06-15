/**
 * 課題04 自己採点用テストランナー
 * 実行: javac Ex04.java Ex04Test.java && java Ex04Test
 */
public class Ex04Test {

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
        Ex04 ex = new Ex04();

        check("sumTo10", 55, ex.sumTo(10));
        check("sumTo1", 1, ex.sumTo(1));
        check("sumTo0", 0, ex.sumTo(0));

        check("factorial5", 120L, ex.factorial(5));
        check("factorial0", 1L, ex.factorial(0));
        check("factorial1", 1L, ex.factorial(1));

        check("countDigits0", 1, ex.countDigits(0));
        check("countDigits7", 1, ex.countDigits(7));
        check("countDigits100", 3, ex.countDigits(100));
        check("countDigits12345", 5, ex.countDigits(12345));

        check("fizzbuzz5", "1\n2\nFizz\n4\nBuzz", ex.fizzbuzz(5));
        check("fizzbuzz15tail", "FizzBuzz", ex.fizzbuzz(15).substring(ex.fizzbuzz(15).lastIndexOf("\n") + 1));

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
