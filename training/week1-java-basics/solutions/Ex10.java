/**
 * 課題10: クラス設計（フィールド・コンストラクタ・メソッド・カプセル化）（模範解答）
 *
 * このファイルには、実装すべき BankAccount クラスを定義してある。
 * （public class Ex10 は、ファイル名と合わせるための入れ物。中身は空でよい）
 *
 * BankAccount 仕様:
 *  - フィールド: owner(String, 口座名義), balance(int, 残高) … どちらも private（外から直接いじれない）
 *  - コンストラクタ: BankAccount(String owner, int initialBalance)
 *  - getOwner()           : 名義を返す
 *  - getBalance()         : 残高を返す
 *  - deposit(int amount)  : 残高を amount 増やす。amount が0以下なら何もしない（残高変更なし）
 *  - withdraw(int amount) : 残高を amount 減らす。amount が0以下、または残高超過なら何もせず false を返す。
 *                           成功したら true を返す。
 */
public class Ex10 {
    // ファイル名(Ex10.java)に合わせるための public クラス。中身は使わない。
}

class BankAccount {
    private String owner;
    private int balance;

    public BankAccount(String owner, int initialBalance) {
        this.owner = owner;
        this.balance = initialBalance;
    }

    public String getOwner() {
        return owner;
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int amount) {
        if (amount <= 0) {
            return;
        }
        balance += amount;
    }

    public boolean withdraw(int amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }
}
