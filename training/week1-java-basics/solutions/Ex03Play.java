/**
 * 課題03 動かしてみる用ランナー
 * 実行: javac Ex03.java Ex03Play.java && java Ex03Play
 */
public class Ex03Play {

    public static void main(String[] args) {
        Ex03 ex = new Ex03();

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        System.out.println("sign(7)   → " + ex.sign(7));
        System.out.println("sign(0)   → " + ex.sign(0));
        System.out.println("sign(-3)  → " + ex.sign(-3));
        System.out.println();

        System.out.println("85点の成績 → " + ex.grade(85));
        System.out.println("55点の成績 → " + ex.grade(55));
        System.out.println();

        System.out.println("2024年はうるう年？ → " + ex.isLeapYear(2024));
        System.out.println("1900年はうるう年？ → " + ex.isLeapYear(1900));
        System.out.println();

        System.out.println("3つの最大値 max3(10, 25, 18) → " + ex.max3(10, 25, 18));
        System.out.println();

        System.out.println("✨ 条件によって結果が変わるのが見えたら、分岐は動いています！");
        System.out.println("次に Ex03Test で正解判定してください。");
    }
}
