/**
 * 課題06: List（ArrayList）（模範解答）
 *
 * 仕様:
 *  - addAll(List<String> list, String... items) : items をすべて list の末尾に追加して、その list を返す
 *  - removeShort(List<String> list, int minLen)  : 長さが minLen 未満の文字列を取り除いた「新しいList」を返す
 *  - join(List<String> list, String sep)        : list の要素を sep でつないだ文字列を返す（空なら ""）
 *  - indexOf(List<String> list, String target)  : target が最初に現れる位置(0始まり)。なければ -1
 */
import java.util.ArrayList;
import java.util.List;

public class Ex06 {

    public List<String> addAll(List<String> list, String... items) {
        for (String item : items) {
            list.add(item);
        }
        return list;
    }

    public List<String> removeShort(List<String> list, int minLen) {
        List<String> result = new ArrayList<>();
        for (String s : list) {
            if (s.length() >= minLen) {
                result.add(s);
            }
        }
        return result;
    }

    public String join(List<String> list, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(sep);
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    public int indexOf(List<String> list, String target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) {
                return i;
            }
        }
        return -1;
    }
}
