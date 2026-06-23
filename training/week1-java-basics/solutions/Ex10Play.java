/**
 * 課題10 動かしてみる用ランナー
 * 実行: javac Ex10.java Ex10Play.java && java Ex10Play
 */
public class Ex10Play {

    public static void main(String[] args) {
        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        BankAccount account = new BankAccount("田中", 1000);
        System.out.println("口座を開設 → 名義: " + account.getOwner() + " / 残高: " + account.getBalance() + " 円");

        account.deposit(500);
        System.out.println("500円 入金後 → 残高: " + account.getBalance() + " 円");

        boolean ok = account.withdraw(300);
        System.out.println("300円 引き出し " + (ok ? "成功" : "失敗") + " → 残高: " + account.getBalance() + " 円");

        ok = account.withdraw(2000);
        System.out.println("2000円 引き出し " + (ok ? "成功" : "失敗") + " → 残高: " + account.getBalance() + " 円");
        System.out.println();

        System.out.println("✨ 口座の残高が変わって見えたら、クラス設計は動いています！");
        System.out.println("次に Ex10Test で正解判定してください。");
    }
}
