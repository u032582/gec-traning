/**
 * 課題09: 例外処理（try-catch・自作例外）（模範解答）
 *
 * 仕様:
 *  - safeDivide(int a, int b) : a / b を返す。b が 0 のときは例外を投げず 0 を返す（try-catchで握る）
 *  - parseOrDefault(String s, int defaultValue)
 *        : s を整数に変換して返す。変換できない（数字でない・null）ときは defaultValue を返す
 *  - withdraw(int balance, int amount) : 残高 balance から amount を引いた残高を返す
 *        amount が balance より大きいときは InsufficientBalanceException を投げる
 *        （自作の検査例外＝メソッド宣言に throws を書くタイプ）
 *
 * 自作例外 InsufficientBalanceException は同じファイル内に定義してある。
 */
public class Ex09 {

    public int safeDivide(int a, int b) {
        try {
            return a / b;
        } catch (ArithmeticException e) {
            // 0で割ると ArithmeticException が出る。ここで握って 0 を返す
            return 0;
        }
    }

    public int parseOrDefault(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            // 数字に変換できない場合（null も含む）に飛んでくる
            return defaultValue;
        }
    }

    public int withdraw(int balance, int amount) throws InsufficientBalanceException {
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "残高不足です。残高=" + balance + ", 引き出し=" + amount);
        }
        return balance - amount;
    }
}

/**
 * 自作の検査例外（けんされいがい）。
 * Exception を継承すると「呼び出し側が必ず try-catch か throws で扱う」タイプになる。
 */
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
