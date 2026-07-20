import java.util.List;
import java.util.stream.Collectors;

public class Ex12 {

    public List<Integer> evens(List<Integer> nums) {
        return nums.stream()
        .filter(n -> n % 2 ==0)
        .collect(Collectors.toList());
    }

    public List<Integer> doubled(List<Integer> nums) {
        return nums.stream()
        .map(n -> n * 2)
        .collect(Collectors.toList());
    }

    public int sum(List<Integer> nums) {
        return nums.stream()
        .mapToInt(n -> n)
        .sum(); 
    }

    public List<String> names(List<Item> items) {
        return items.stream()
        .map(item -> item.getName())
        .collect(Collectors.toList());
    }

    public int totalPriceOver(List<Item> items, int min) {
        return items.stream()
        .filter(item -> item.getPrice() >= min)
        .mapToInt(item -> item.getPrice())
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

    public String getName() { return name; }
    public int getPrice() { return price; }
}