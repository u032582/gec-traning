/**
 * 課題07 動かしてみる用ランナー
 * 実行: javac Ex07.java Ex07Play.java && java Ex07Play
 */
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ex07Play {

    public static void main(String[] args) {
        Ex07 ex = new Ex07();

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        List<String> words = Arrays.asList("apple", "banana", "apple", "cherry", "banana", "apple");
        Map<String, Integer> counts = ex.countWords(words);
        System.out.println("単語の出現回数 → " + counts);
        System.out.println("\"apple\" の回数 → " + ex.getOrZero(counts, "apple"));
        System.out.println("最も多い単語 → " + ex.maxKey(counts));
        System.out.println();

        Map<String, Integer> a = new HashMap<>();
        a.put("A", 1);
        a.put("B", 2);
        Map<String, Integer> b = new HashMap<>();
        b.put("B", 3);
        b.put("C", 4);
        System.out.println("Mapの合算 → " + ex.merge(a, b));
        System.out.println();

        System.out.println("✨ 単語の集計結果が見えたら、Map 操作は動いています！");
        System.out.println("次に Ex07Test で正解判定してください。");
    }
}
