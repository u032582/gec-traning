/**
 * 課題08: Set（模範解答）
 *
 * 仕様:
 *  - unique(List<Integer> list)  : 重複を除いた要素数を返す
 *  - hasDuplicate(List<Integer> list) : 重複が1つでもあれば true
 *  - intersection(Set<Integer> a, Set<Integer> b) : a と b の両方に含まれる要素の Set を返す
 *  - difference(Set<Integer> a, Set<Integer> b)   : a にあって b にない要素の Set を返す
 */
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ex08 {

    public int unique(List<Integer> list) {
        Set<Integer> set = new HashSet<>(list);
        return set.size();
    }

    public boolean hasDuplicate(List<Integer> list) {
        Set<Integer> seen = new HashSet<>();
        for (Integer v : list) {
            if (!seen.add(v)) {
                // add は「すでにあった」場合 false を返す
                return true;
            }
        }
        return false;
    }

    public Set<Integer> intersection(Set<Integer> a, Set<Integer> b) {
        Set<Integer> result = new HashSet<>();
        for (Integer v : a) {
            if (b.contains(v)) {
                result.add(v);
            }
        }
        return result;
    }

    public Set<Integer> difference(Set<Integer> a, Set<Integer> b) {
        Set<Integer> result = new HashSet<>();
        for (Integer v : a) {
            if (!b.contains(v)) {
                result.add(v);
            }
        }
        return result;
    }
}
