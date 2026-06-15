/**
 * 課題07: Map（HashMap）（模範解答）
 *
 * 仕様:
 *  - countWords(List<String> words) : 単語ごとの出現回数を Map<String,Integer> で返す
 *  - getOrZero(Map<String,Integer> map, String key) : key があればその値、なければ 0 を返す
 *  - merge(Map<String,Integer> a, Map<String,Integer> b)
 *        : 2つのMapを合算した「新しいMap」を返す（同じキーは値を足す）
 *  - maxKey(Map<String,Integer> map) : 値が最大のキーを返す（空Mapなら null。最大が複数なら任意の1つでよい）
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ex07 {

    public Map<String, Integer> countWords(List<String> words) {
        Map<String, Integer> result = new HashMap<>();
        for (String w : words) {
            result.put(w, result.getOrDefault(w, 0) + 1);
        }
        return result;
    }

    public int getOrZero(Map<String, Integer> map, String key) {
        return map.getOrDefault(key, 0);
    }

    public Map<String, Integer> merge(Map<String, Integer> a, Map<String, Integer> b) {
        Map<String, Integer> result = new HashMap<>(a);
        for (Map.Entry<String, Integer> e : b.entrySet()) {
            result.put(e.getKey(), result.getOrDefault(e.getKey(), 0) + e.getValue());
        }
        return result;
    }

    public String maxKey(Map<String, Integer> map) {
        String maxKey = null;
        int maxValue = Integer.MIN_VALUE;
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (e.getValue() > maxValue) {
                maxValue = e.getValue();
                maxKey = e.getKey();
            }
        }
        return maxKey;
    }
}
