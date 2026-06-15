/**
 * 課題10 自己採点用テストランナー
 * 実行: javac Ex10.java Ex10Test.java && java Ex10Test
 */
public class Ex10Test {

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
        BankAccount acc = new BankAccount("田中", 1000);

        check("owner", "田中", acc.getOwner());
        check("initialBalance", 1000, acc.getBalance());

        acc.deposit(500);
        check("afterDeposit", 1500, acc.getBalance());

        acc.deposit(-100); // 0以下は無視
        check("depositIgnoreNegative", 1500, acc.getBalance());

        check("withdrawOK", true, acc.withdraw(300));
        check("afterWithdraw", 1200, acc.getBalance());

        check("withdrawTooMuch", false, acc.withdraw(99999));
        check("balanceUnchanged", 1200, acc.getBalance());

        check("withdrawNegative", false, acc.withdraw(-50));
        check("balanceUnchanged2", 1200, acc.getBalance());

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
