/**
 * 課題11: クラス間の関係（コレクション内のオブジェクト操作）（模範解答）
 *
 * このファイルには Product（商品）と Inventory（在庫）を定義してある。
 *
 * Product 仕様:
 *  - フィールド(private): name(String 商品名), price(int 単価), quantity(int 在庫数)
 *  - コンストラクタ: Product(String name, int price, int quantity)
 *  - getName() / getPrice() / getQuantity() : それぞれの値を返す
 *  - subtotal() : price * quantity を返す（その商品の在庫金額）
 *
 * Inventory 仕様（内部に List<Product> を持つ）:
 *  - add(Product p)        : 商品を1つ追加する
 *  - totalAmount()         : 全商品の subtotal の合計を返す
 *  - findByName(String name): 名前が一致する最初の Product を返す。なければ null
 *  - countLowStock(int threshold) : 在庫数が threshold 未満の商品の個数を返す
 */
import java.util.ArrayList;
import java.util.List;

public class Ex11 {
    // ファイル名(Ex11.java)に合わせるための public クラス。中身は使わない。
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

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int subtotal() {
        return price * quantity;
    }
}

class Inventory {
    private List<Product> products = new ArrayList<>();

    public void add(Product p) {
        products.add(p);
    }

    public int totalAmount() {
        int total = 0;
        for (Product p : products) {
            total += p.subtotal();
        }
        return total;
    }

    public Product findByName(String name) {
        for (Product p : products) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public int countLowStock(int threshold) {
        int count = 0;
        for (Product p : products) {
            if (p.getQuantity() < threshold) {
                count++;
            }
        }
        return count;
    }
}
