/**
 * 課題01 動かしてみる用ランナー
 * 実行: javac Ex01.java Ex01Play.java && java Ex01Play
 */
public class Ex01Play {

    public static void main(String[] args) {
        Ex01 ex = new Ex01();

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        String g1 = ex.greeting("田中");
        System.out.println("greeting(\"田中\") の結果:");
        System.out.println("  → " + g1);
        System.out.println();

        String i1 = ex.introduce("田中", 25);
        System.out.println("introduce(\"田中\", 25) の結果:");
        System.out.println("  → " + i1);
        System.out.println();

        System.out.println("✨ 自分で作った文章が画面に出たら、うまく動いています！");
        System.out.println("次に Ex01Test で正解判定してください。");
    }
}
