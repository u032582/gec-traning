/**
 * 課題03 自己採点用テストランナー
 * 実行: javac Ex03.java Ex03Test.java && java Ex03Test
 */
public class Ex03Test {

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
        Ex03 ex = new Ex03();

        check("sign+", "正", ex.sign(5));
        check("sign0", "ゼロ", ex.sign(0));
        check("sign-", "負", ex.sign(-3));

        check("gradeA", "A", ex.grade(95));
        check("gradeB", "B", ex.grade(80));
        check("gradeC", "C", ex.grade(75));
        check("gradeD", "D", ex.grade(60));
        check("gradeF", "F", ex.grade(40));

        check("leap2000", true, ex.isLeapYear(2000));
        check("leap1900", false, ex.isLeapYear(1900));
        check("leap2024", true, ex.isLeapYear(2024));
        check("leap2023", false, ex.isLeapYear(2023));

        check("max3a", 9, ex.max3(9, 4, 2));
        check("max3b", 8, ex.max3(3, 8, 5));
        check("max3c", 7, ex.max3(1, 6, 7));

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
