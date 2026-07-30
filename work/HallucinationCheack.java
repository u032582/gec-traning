import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

public class HallucinationCheack {
    public static void main(String[] args) {
        List<Integer> list = List.of(5, 3, 3, 1, 5);
        List<Integer> top3 = list.stream().distinct().sorted(Comparator.reverseOrder()).limit(3).collect(Collectors.toList());
        System.out.println(top3);
    }
}