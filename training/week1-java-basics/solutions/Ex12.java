/**
 * 課題12: Stream API（filter / map / collect / reduce）（模範解答）
 *
 * このファイルには、データ用の Item クラスも定義してある。
 *
 * Item 仕様:
 *  - フィールド(private): name(String), price(int)
 *  - コンストラクタ: Item(String name, int price)
 *  - getName() / getPrice()
 *
 * Ex12 仕様（すべて Stream API を使って実装すること。for文で書かない）:
 *  - evens(List<Integer> nums)            : 偶数だけを取り出した List<Integer> を返す
 *  - doubled(List<Integer> nums)          : 各要素を2倍にした List<Integer> を返す
 *  - sum(List<Integer> nums)              : 合計を返す（reduce か mapToInt().sum() を使う）
 *  - names(List<Item> items)              : 商品名だけを集めた List<String> を返す
 *  - totalPriceOver(List<Item> items, int min)
 *        : 価格が min 以上の商品だけに絞り、その price の合計を返す
 */
import java.util.List;
import java.util.stream.Collectors;

public class Ex12 {

    public List<Integer> evens(List<Integer> nums) {
        return nums.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
    }

    public List<Integer> doubled(List<Integer> nums) {
        return nums.stream()
                .map(n -> n * 2)
                .collect(Collectors.toList());
    }

    public int sum(List<Integer> nums) {
        return nums.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public List<String> names(List<Item> items) {
        return items.stream()
                .map(Item::getName)
                .collect(Collectors.toList());
    }

    public int totalPriceOver(List<Item> items, int min) {
        return items.stream()
                .filter(i -> i.getPrice() >= min)
                .mapToInt(Item::getPrice)
                .sum();
    }
}

class Item {
    private String name;
    private int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
