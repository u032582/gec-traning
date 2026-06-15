# 課題11: クラス間の関係（コレクション内のオブジェクト操作）

**所要時間の目安**: 60分
**AI利用区分**: 🚫 コードを書かせるのは禁止（家庭教師用途のみ）

---

## 1. 背景・狙い（なぜ学ぶか）

現場のデータは「クラスのインスタンスを、Listにたくさん入れて扱う」形がほとんどです。「商品（`Product`）のリストを持つ在庫（`Inventory`）」「注文の一覧」などです。
今回は、課題10で作ったクラスを **コレクションに入れて、まとめて集計・検索する** 練習をします。「全商品の在庫金額の合計」「名前で商品を探す」「在庫が少ない商品を数える」——これはまさに業務システムの中身そのものです。クラスとコレクション（List）を組み合わせられれば、週1の山を越えたと言えます。

> 用語メモ: **インスタンス** … クラスから作られた実体（モノ）。**フィールドにListを持つ** … クラスの中に `List<Product>` を持たせ、そこへ要素を足していく設計。

---

## 2. 詳細設計（この通りに実装する）

`Product`（商品）と `Inventory`（在庫）の2クラスを、`Ex11.java` の中に実装してください（ファイル名に合わせて空の `public class Ex11 {}` も置く）。

### `Product` の仕様

- **フィールド**（すべて `private`）: `String name`, `int price`（単価）, `int quantity`（在庫数）
- **コンストラクタ**: `Product(String name, int price, int quantity)`
- **メソッド**: `getName()` / `getPrice()` / `getQuantity()`、および `subtotal()`（= `price * quantity`、その商品の在庫金額）

### `Inventory` の仕様（内部に `List<Product>` を持つ）

| メソッド | 引数 | 戻り値 | 仕様 |
|---|---|---|---|
| `add` | `Product p` | `void` | 商品を1つ追加 |
| `totalAmount` | なし | `int` | 全商品の`subtotal`の合計 |
| `findByName` | `String name` | `Product` | 名前が一致する最初の商品。なければ`null` |
| `countLowStock` | `int threshold` | `int` | 在庫数がthreshold未満の商品の個数 |

### ひな型（`Ex11.java`）

```java
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
        // TODO
    }

    public String getName() { return null; /* TODO */ }
    public int getPrice() { return 0; /* TODO */ }
    public int getQuantity() { return 0; /* TODO */ }
    public int subtotal() { return 0; /* TODO */ }
}

class Inventory {
    private List<Product> products = new ArrayList<>();

    public void add(Product p) {
        // TODO
    }

    public int totalAmount() {
        return 0; // TODO
    }

    public Product findByName(String name) {
        return null; // TODO
    }

    public int countLowStock(int threshold) {
        return 0; // TODO
    }
}
```

---

## 3. 完成条件

- `Ex11Test.java` を実行して **`ALL PASS ✅`** が出ること。

---

## 4. 検証方法

```bash
javac Ex11.java Ex11Test.java && java Ex11Test
```

### 自己採点用テストランナー（`Ex11Test.java`）

```java
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
```

---

## 5. つまずきポイントとヒント

<details>
<summary>Inventory の中で Product をどう回る？</summary>

`for (Product p : products) { ... }` で、Listに入った各 `Product` を1つずつ取り出せます。その `p.subtotal()` や `p.getQuantity()` を使って集計します。
</details>

<details>
<summary>findByName で名前の比較</summary>

文字列の比較は `p.getName().equals(name)` を使います（`==` は不可）。一致した最初の `p` を `return` し、最後まで見つからなければ `null` を返します。
</details>

<details>
<summary>countLowStock の「未満」に注意</summary>

「threshold **未満**」なので `p.getQuantity() < threshold`（イコールを含まない）です。`<=` にすると境界がずれてテストに落ちます。
</details>

---

## 6. 発展（余裕があれば）

この課題が終わったら、`totalAmount` や `countLowStock` を、次の課題12で学ぶ **Stream API** で書き直してみると、forとの違いが体感できます（提出不要）。

---

## 7. 模範解答への導線

先に自分で解いてから、[solutions/Ex11.java](../solutions/Ex11.java) と見比べてください。
