import java.util.ArrayList;
import java.util.List;

public class Ex11 {
    // ファイル名(Ex11.java)に合わせるための入れ物。中身は空でよい。
}

class Product {
    private String name;
    private int price;
    private int quantity;

    public Product(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name;}
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int subtotal() { return price * quantity; }
}

class Inventory {
    private List<Product> products = new ArrayList<>();

    public void add(Product p) {
        products.add(p);
    }

    public int totalAmount() {
        int total = 0;
        for(Product p : products){
            total = total + p.subtotal();
        }
        return total;
    }

    public Product findByName(String name) {
        for(Product p : products){
            if(name.equals(p.getName())){
                return p;
            }
        }
        return null;
    }

    public int countLowStock(int threshold) {
        int count = 0;
        for(Product p : products){
            if(p.getQuantity() < threshold){
                count++;
            }
        }
        return count;
    }
}