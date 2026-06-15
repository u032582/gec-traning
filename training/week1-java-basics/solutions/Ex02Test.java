/**
 * 課題02 自己採点用テストランナー
 * 実行: javac Ex02.java Ex02Test.java && java Ex02Test
 */
public class Ex02Test {

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

    // double は誤差が出ることがあるので、ごく小さな差は同じとみなす
    static void checkDouble(String caseName, double expected, double actual) {
        total++;
        if (Math.abs(expected - actual) < 1e-9) {
            System.out.println("[PASS] " + caseName);
        } else {
            fail++;
            System.out.println("[FAIL] " + caseName + ": 期待=" + expected + ", 実際=" + actual);
        }
    }

    public static void main(String[] args) {
        Ex02 ex = new Ex02();

        check("add", 8, ex.add(5, 3));
        check("subtract", 2, ex.subtract(5, 3));
        check("multiply", 15, ex.multiply(5, 3));
        checkDouble("divideAsDouble", 3.5, ex.divideAsDouble(7, 2));
        checkDouble("divideAsDouble2", 2.5, ex.divideAsDouble(5, 2));
        checkDouble("average", 20.0, ex.average(10, 20, 30));
        checkDouble("average2", 2.0, ex.average(1, 2, 3));
        check("toInt", 3, ex.toInt(3.9));
        check("toInt2", 7, ex.toInt(7.1));

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
