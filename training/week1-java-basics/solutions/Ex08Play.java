/**
 * 課題08 動かしてみる用ランナー
 * 実行: javac Ex08.java Ex08Play.java && java Ex08Play
 */
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ex08Play {

    public static void main(String[] args) {
        Ex08 ex = new Ex08();

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        List<Integer> nums = Arrays.asList(1, 2, 2, 3, 3, 3);
        System.out.println("リスト " + nums);
        System.out.println("  重複を除いた種類数 → " + ex.unique(nums));
        System.out.println("  重複あり？ → " + ex.hasDuplicate(nums));
        System.out.println();

        Set<Integer> a = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        Set<Integer> b = new HashSet<>(Arrays.asList(3, 4, 5, 6));
        System.out.println("集合A " + a + " と 集合B " + b);
        System.out.println("  共通部分 → " + ex.intersection(a, b));
        System.out.println("  AにあってBにない → " + ex.difference(a, b));
        System.out.println();

        System.out.println("✨ 重複や集合の結果が見えたら、Set 操作は動いています！");
        System.out.println("次に Ex08Test で正解判定してください。");
    }
}
