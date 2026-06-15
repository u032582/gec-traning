/**
 * 課題11 自己採点用テストランナー
 * 実行: javac Ex11.java Ex11Test.java && java Ex11Test
 */
public class Ex11Test {

    static int fail = 0;
    static int total = 0;

    static void check(String caseName, Object expected, Object actual) {
        total++;
        if (expected == null ? actual == null : expected.equals(actual)) {
            System.out.println("[PASS] " + caseName);
        } else {
            fail++;
            System.out.println("[FAIL] " + caseName + ": 期待=" + expected + ", 実際=" + actual);
        }
    }

    public static void main(String[] args) {
        Product apple = new Product("りんご", 100, 5);
        check("subtotal", 500, apple.subtotal());

        Inventory inv = new Inventory();
        inv.add(new Product("りんご", 100, 5));   // 500
        inv.add(new Product("みかん", 80, 10));   // 800
        inv.add(new Product("バナナ", 120, 2));   // 240

        check("totalAmount", 1540, inv.totalAmount());

        Product found = inv.findByName("みかん");
        check("findByNameNotNull", true, found != null);
        check("findByNamePrice", 80, found.getPrice());
        check("findByNameMiss", null, inv.findByName("ぶどう"));

        // 在庫数 3未満は バナナ(2) のみ -> 1件
        check("countLowStock", 1, inv.countLowStock(3));
        // 在庫数 6未満は りんご(5), バナナ(2) -> 2件
        check("countLowStock2", 2, inv.countLowStock(6));

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
