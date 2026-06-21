/**
 * 課題06 動かしてみる用ランナー
 * 実行: javac Ex06.java Ex06Play.java && java Ex06Play
 */
import java.util.ArrayList;
import java.util.List;

public class Ex06Play {

    public static void main(String[] args) {
        Ex06 ex = new Ex06();

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        List<String> fruits = new ArrayList<>();
        ex.addAll(fruits, "りんご", "みかん", "ぶどう", "桃");
        System.out.println("果物リストに追加 → " + fruits);
        System.out.println("join(\" / \") → " + ex.join(fruits, " / "));
        System.out.println();

        List<String> filtered = ex.removeShort(fruits, 3);
        System.out.println("3文字未満を除く → " + filtered + " （「桃」だけ消える）");
        System.out.println("\"みかん\" の位置 → " + ex.indexOf(fruits, "みかん"));
        System.out.println();

        System.out.println("✨ リストの中身が変わって見えたら、List 操作は動いています！");
        System.out.println("次に Ex06Test で正解判定してください。");
    }
}
