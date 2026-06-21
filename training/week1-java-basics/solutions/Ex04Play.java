/**
 * 課題04 動かしてみる用ランナー
 * 実行: javac Ex04.java Ex04Play.java && java Ex04Play
 */
public class Ex04Play {

    public static void main(String[] args) {
        Ex04 ex = new Ex04();

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        System.out.println("1 から 5 までの合計 → " + ex.sumTo(5));
        System.out.println("5!（5の階乗）→ " + ex.factorial(5));
        System.out.println("12345 の桁数 → " + ex.countDigits(12345));
        System.out.println();

        System.out.println("FizzBuzz（1〜15）:");
        System.out.println(ex.fizzbuzz(15));
        System.out.println();

        System.out.println("✨ 繰り返し処理の結果が画面に並んだら、ループは動いています！");
        System.out.println("次に Ex04Test で正解判定してください。");
    }
}
