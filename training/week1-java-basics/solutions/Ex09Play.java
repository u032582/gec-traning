/**
 * 課題09 動かしてみる用ランナー
 * 実行: javac Ex09.java Ex09Play.java && java Ex09Play
 */
public class Ex09Play {

    public static void main(String[] args) {
        Ex09 ex = new Ex09();

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        System.out.println("10 ÷ 2 = " + ex.safeDivide(10, 2));
        System.out.println("10 ÷ 0 = " + ex.safeDivide(10, 0) + " （0除算を安全に処理）");
        System.out.println("\"42\" を整数に = " + ex.parseOrDefault("42", -1));
        System.out.println("\"abc\" を整数に = " + ex.parseOrDefault("abc", -1) + " （変換失敗時は -1）");
        System.out.println();

        try {
            int after = ex.withdraw(1000, 300);
            System.out.println("残高1000円から300円引き出し → 残高 " + after + " 円");
            ex.withdraw(1000, 1500);
            System.out.println("（ここには来ないはず）");
        } catch (InsufficientBalanceException e) {
            System.out.println("残高不足を検知 → " + e.getMessage());
        }
        System.out.println();

        System.out.println("✨ エラーをうまく処理した結果が見えたら、例外処理は動いています！");
        System.out.println("次に Ex09Test で正解判定してください。");
    }
}
