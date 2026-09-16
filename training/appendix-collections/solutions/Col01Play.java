/**
 * 付録01 動かしてみる用ランナー
 * 実行: javac Col01.java Col01Play.java && java Col01Play
 */
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Col01Play {

    public static void main(String[] args) {
        Col01 col = new Col01();

        // 貸出履歴（古い順）。同じ本を何度も借りている。
        List<String> borrowed = Arrays.asList(
                "リーダブルコード", "ノルウェイの森", "リーダブルコード",
                "人月の神話", "ノルウェイの森", "リーダブルコード");

        System.out.println("=== 同じ1つのデータから、3つ作ってみます ===");
        System.out.println();
        System.out.println("元の貸出履歴（古い順・" + borrowed.size() + "件）");
        System.out.println("  " + borrowed);
        System.out.println();

        System.out.println("① List  最近借りた3冊（新しい順） → " + col.latest3(borrowed));
        System.out.println("     ねらい: 順番が意味を持つ。同じ本が何度出てきてもよい。");
        System.out.println();

        System.out.println("② Set   借りたことのある本の種類  → " + col.kinds(borrowed) + " 種類");
        System.out.println("     中身はこう: " + new HashSet<>(borrowed));
        System.out.println("     ねらい: 重複が消える。並び順は保証されない。");
        System.out.println();

        System.out.println("③ Map   本ごとの貸出回数          → " + col.countByTitle(borrowed));
        System.out.println("     ねらい: 名前（キー）から値をすぐ引ける。");
        System.out.println();

        System.out.println("--------");
        System.out.println("6件の履歴が、3冊・3種類・{本=回数} と別の形になりました。");
        System.out.println("同じデータでも、聞かれていることが違えば入れ物も変わります。");
        System.out.println();
        System.out.println("※ ここで null や空っぽが出ても、実装が途中なだけです。あわてなくて大丈夫。");
        System.out.println("✨ 3つとも中身が出たら動いています！ 次に Col01Test で正解判定してください。");
    }
}
