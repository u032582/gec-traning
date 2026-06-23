/**
 * 課題02 動かしてみる用ランナー
 * 実行: javac Ex02.java Ex02Play.java && java Ex02Play
 */
public class Ex02Play {

    public static void main(String[] args) {
        Ex02 ex = new Ex02();

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        System.out.println("5 + 3 = " + ex.add(5, 3));
        System.out.println("5 - 3 = " + ex.subtract(5, 3));
        System.out.println("5 × 3 = " + ex.multiply(5, 3));
        System.out.println("7 ÷ 2 = " + ex.divideAsDouble(7, 2) + " （小数）");
        System.out.println("(10 + 20 + 30) ÷ 3 = " + ex.average(10, 20, 30));
        System.out.println("3.9 を整数に = " + ex.toInt(3.9));
        System.out.println();

        System.out.println("✨ 数字が画面に出たら、計算処理は動いています！");
        System.out.println("次に Ex02Test で正解判定してください。");
    }
}
