# 課題07: Map（HashMap）

**所要時間の目安**: 50分
**AI利用区分**: 🚫 コードを書かせるのは禁止（家庭教師用途のみ）

---

## 1. 背景・狙い（なぜ学ぶか）

**Map（マップ）**は「キー（鍵）と値（あたい）の対応表」です。「商品コード→在庫数」「ユーザーID→名前」のように、**何かをキーにして値を引く**処理は業務システムの定番です。
今回は「単語の出現回数を数える」など、Mapの代表的な使い方を身につけます。`getOrDefault` という便利メソッドも覚えます。これは現場で頻出します。

> 用語メモ: **Map（マップ）** … キーと値をペアで持つ入れ物。**HashMap（ハッシュマップ）** … Mapの代表的な実装。**`put(key, value)`** … キーに値を登録（同じキーなら上書き）。**`get(key)`** … キーに対応する値を取得（無ければ`null`）。**`getOrDefault(key, 既定値)`** … 無いときに既定値を返す安全版。

---

## 2. 詳細設計（この通りに実装する）

`Ex07` クラスに次のメソッドを実装してください。

| メソッド | 引数 | 戻り値 | 仕様 |
|---|---|---|---|
| `countWords` | `List<String> words` | `Map<String,Integer>` | 単語ごとの出現回数 |
| `getOrZero` | `Map<String,Integer> map, String key` | `int` | keyがあればその値、なければ0 |
| `merge` | `Map<String,Integer> a, Map<String,Integer> b` | `Map<String,Integer>` | 2つを合算した**新しいMap**（同じキーは値を足す） |
| `maxKey` | `Map<String,Integer> map` | `String` | 値が最大のキー（空なら`null`。最大が複数なら任意の1つでよい） |

### ひな型（`Ex07.java`）

```java
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ex07 {

    public Map<String, Integer> countWords(List<String> words) {
        return null; // TODO
    }

    public int getOrZero(Map<String, Integer> map, String key) {
        return 0; // TODO
    }

    public Map<String, Integer> merge(Map<String, Integer> a, Map<String, Integer> b) {
        return null; // TODO
    }

    public String maxKey(Map<String, Integer> map) {
        return null; // TODO
    }
}
```

---

## 3. 完成条件

- `Ex07Test.java` を実行して **`ALL PASS ✅`** が出ること。

---

## 4. 検証方法

```bash
javac Ex07.java Ex07Test.java && java Ex07Test
```

### 自己採点用テストランナー（`Ex07Test.java`）

```java
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Ex07Test {

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
        Ex07 ex = new Ex07();

        Map<String, Integer> counted = ex.countWords(Arrays.asList("a", "b", "a", "c", "a", "b"));
        Map<String, Integer> expectedCount = new HashMap<>();
        expectedCount.put("a", 3);
        expectedCount.put("b", 2);
        expectedCount.put("c", 1);
        check("countWords", expectedCount, counted);

        Map<String, Integer> m = new HashMap<>();
        m.put("x", 5);
        check("getOrZeroHit", 5, ex.getOrZero(m, "x"));
        check("getOrZeroMiss", 0, ex.getOrZero(m, "y"));

        Map<String, Integer> a = new HashMap<>();
        a.put("x", 1);
        a.put("y", 2);
        Map<String, Integer> b = new HashMap<>();
        b.put("y", 3);
        b.put("z", 4);
        Map<String, Integer> expectedMerge = new HashMap<>();
        expectedMerge.put("x", 1);
        expectedMerge.put("y", 5);
        expectedMerge.put("z", 4);
        check("merge", expectedMerge, ex.merge(a, b));

        Map<String, Integer> maxMap = new HashMap<>();
        maxMap.put("p", 1);
        maxMap.put("q", 9);
        maxMap.put("r", 3);
        check("maxKey", "q", ex.maxKey(maxMap));
        check("maxKeyEmpty", null, ex.maxKey(new HashMap<>()));

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
<summary>countWords で「初めて見た単語は1、既に見た単語は+1」をどう書く？</summary>

`map.getOrDefault(w, 0) + 1` を `put` すれば、初出は `0+1=1`、2回目は `1+1=2`…と数えられます。これがMapで集計する定番パターンです。
</details>

<details>
<summary>Map を全部回るには？</summary>

`for (Map.Entry<String, Integer> e : map.entrySet()) { e.getKey(); e.getValue(); }` で、キーと値のペアを1つずつ取り出せます。`maxKey` や `merge` で使います。
</details>

<details>
<summary>merge で元のMapを壊さないには？</summary>

`new HashMap<>(a)` で a をコピーした新しいMapを作り、そこに b の分を足し込みます。「同じキーは足す」は `getOrDefault` を使えば書けます。
</details>

---

## 6. 模範解答への導線

先に自分で解いてから、[solutions/Ex07.java](../solutions/Ex07.java) と見比べてください。
