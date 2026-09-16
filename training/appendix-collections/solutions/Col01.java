/**
 * 付録: List / Set / Map —— 同じデータから3つ作って、違いを見る（模範解答）
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Col01 {

    /**
     * ① List —— 最後に借りた3冊を「新しい順」で返す。
     * 借りた履歴は古い順に入っている。3冊に満たなければ、ある分だけ返す。
     */
    public List<String> latest3(List<String> borrowed) {
        List<String> result = new ArrayList<>();
        for (int i = borrowed.size() - 1; i >= 0 && result.size() < 3; i--) {
            result.add(borrowed.get(i));
        }
        return result;
    }

    /**
     * ② Set —— これまでに借りたことのある本は、何種類か。
     */
    public int kinds(List<String> borrowed) {
        Set<String> titles = new HashSet<>(borrowed);
        return titles.size();
    }

    /**
     * ③ Map —— 本ごとの貸出回数を返す。
     */
    public Map<String, Integer> countByTitle(List<String> borrowed) {
        Map<String, Integer> counts = new HashMap<>();
        for (String title : borrowed) {
            counts.put(title, counts.getOrDefault(title, 0) + 1);
        }
        return counts;
    }
}
