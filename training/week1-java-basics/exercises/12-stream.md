# 課題12: Stream API（filter / map / collect / reduce）

**所要時間の目安**: 60分
**AI利用区分**: 🚫 コードを書かせるのは禁止（家庭教師用途のみ）

---

## 1. 背景・狙い（なぜ学ぶか）

ここまでは `for` でコレクションを回して、絞り込みや集計をしてきました。**Stream API（ストリームエーピーアイ）** は、その処理を「データの流れ作業」として短く・読みやすく書くための仕組みです。
「条件で絞る（`filter`）→ 形を変える（`map`）→ 集める（`collect`）／合計する（`reduce`）」という流れを、`.` でつないで書きます。現場のJavaコードでは Stream が大量に出てきます。**ただし、まず `for` で書ける状態が前提**です（for で書けないものを Stream で書くと、中身を理解しないまま使うことになるため）。だからこの課題は週1の最後に置いています。

> 用語メモ: **Stream（ストリーム）** … コレクションを「流れ」として処理する仕組み。**filter** … 条件で絞る。**map** … 各要素を別の形に変換する。**collect** … 結果をList等に集める。**reduce / sum** … 1つの値（合計など）にまとめる。**ラムダ式** … `n -> n % 2 == 0` のような短い関数の書き方。

---

## 2. 詳細設計（この通りに実装する）

`Ex12` クラスに次のメソッドを実装してください。**すべて Stream API で書くこと**（`for` 文は使わない）。データ用の `Item` クラスも同じファイルに定義します。

### `Item` の仕様

- **フィールド**（`private`）: `String name`, `int price`
- **コンストラクタ**: `Item(String name, int price)`
- **メソッド**: `getName()` / `getPrice()`

### `Ex12` のメソッド

| メソッド | 引数 | 戻り値 | 仕様 |
|---|---|---|---|
| `evens` | `List<Integer> nums` | `List<Integer>` | 偶数だけを取り出す（filter） |
| `doubled` | `List<Integer> nums` | `List<Integer>` | 各要素を2倍にする（map） |
| `sum` | `List<Integer> nums` | `int` | 合計（reduce または mapToInt().sum()） |
| `names` | `List<Item> items` | `List<String>` | 商品名だけを集める（map） |
| `totalPriceOver` | `List<Item> items, int min` | `int` | priceがmin以上の商品のpriceの合計（filter + 合計） |

### ひな型（`Ex12.java`）

```java
import java.util.List;
import java.util.stream.Collectors;

public class Ex12 {

    public List<Integer> evens(List<Integer> nums) {
        return null; // TODO: stream().filter(...).collect(...)
    }

    public List<Integer> doubled(List<Integer> nums) {
        return null; // TODO: stream().map(...).collect(...)
    }

    public int sum(List<Integer> nums) {
        return 0; // TODO: stream().mapToInt(...).sum() など
    }

    public List<String> names(List<Item> items) {
        return null; // TODO
    }

    public int totalPriceOver(List<Item> items, int min) {
        return 0; // TODO
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
```

---

## 3. 完成条件

- `Ex12Test.java` を実行して **`ALL PASS ✅`** が出ること。

---

## 4. 検証方法

```bash
javac Ex12.java Ex12Test.java && java Ex12Test
```

### 自己採点用テストランナー（`Ex12Test.java`）

```java
import java.util.Arrays;
import java.util.List;

public class Ex12Test {

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
        Ex12 ex = new Ex12();

        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6);
        check("evens", Arrays.asList(2, 4, 6), ex.evens(nums));
        check("doubled", Arrays.asList(2, 4, 6, 8, 10, 12), ex.doubled(nums));
        check("sum", 21, ex.sum(nums));

        List<Item> items = Arrays.asList(
                new Item("りんご", 100),
                new Item("みかん", 80),
                new Item("メロン", 500)
        );
        check("names", Arrays.asList("りんご", "みかん", "メロン"), ex.names(items));
        // 100以上: りんご(100) + メロン(500) = 600
        check("totalPriceOver", 600, ex.totalPriceOver(items, 100));

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
<summary>Stream の基本の流れ</summary>

`list.stream()` で流れを始め、`.filter(...)` や `.map(...)` で加工し、最後に `.collect(Collectors.toList())` でListに戻します。`import java.util.stream.Collectors;` が必要です（ひな型に書いてあります）。
</details>

<details>
<summary>filter と map の書き方（ラムダ式）</summary>

`filter(n -> n % 2 == 0)` は「nが偶数なら残す」。`map(n -> n * 2)` は「nを2倍に変換」。`n ->` の右側に「その要素に対してやること」を書きます。
</details>

<details>
<summary>合計（sum）はどう書く？</summary>

`list.stream().mapToInt(Integer::intValue).sum()` が簡単です。`mapToInt` で「int の流れ」に変えると `.sum()` が使えます。`Item` の場合は `mapToInt(Item::getPrice)`。
</details>

<details>
<summary>メソッド参照（Item::getName）とは</summary>

`map(i -> i.getName())` と `map(Item::getName)` は同じ意味です。後者は「メソッド参照」という短い書き方。最初はラムダ式 `i -> i.getName()` で書いてOKです。
</details>

---

## 6. 模範解答への導線

先に自分で解いてから、[solutions/Ex12.java](../solutions/Ex12.java) と見比べてください。

これで週1は完了です。お疲れさまでした。[週1のREADME](../README.md) に戻って、到達目標「仕様を渡されればJavaで書ける」を自分の言葉で説明できるか確認してください。
