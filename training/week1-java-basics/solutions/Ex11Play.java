/**
 * 課題11 動かしてみる用ランナー
 * 実行: javac Ex11.java Ex11Play.java && java Ex11Play
 */
public class Ex11Play {

    public static void main(String[] args) {
        Inventory inv = new Inventory();

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        inv.add(new Product("りんご", 120, 10));
        inv.add(new Product("みかん", 80, 5));
        inv.add(new Product("ぶどう", 300, 2));

        System.out.println("在庫を3品目登録しました");
        System.out.println("  在庫総額 → " + inv.totalAmount() + " 円");

        Product p = inv.findByName("みかん");
        if (p != null) {
            System.out.println("  \"みかん\" を検索 → 単価 " + p.getPrice() + " 円 × 在庫 " + p.getQuantity());
        }

        System.out.println("  在庫5未満の品目数 → " + inv.countLowStock(5));
        System.out.println();

        System.out.println("✨ 商品と在庫の情報が見えたら、クラス間の連携は動いています！");
        System.out.println("次に Ex11Test で正解判定してください。");
    }
}
