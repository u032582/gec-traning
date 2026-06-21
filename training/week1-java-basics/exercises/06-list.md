# 課題06: List（ArrayList）

**所要時間の目安**: 50分
**AI利用区分**: 🚫 コードを書かせるのは禁止（家庭教師用途のみ）

---

## 1. 背景・狙い（なぜ学ぶか）

配列は「最初に長さを決める」必要があり、後から増やせません。実際の業務では「何件来るか分からないデータを、来た分だけ足していく」ことがほとんどです。そこで使うのが **List（リスト）** です。
`List` は長さが自由に変わる「可変長（かへんちょう）」の入れ物で、現場で最も頻繁に使うコレクションです。今回はその代表 `ArrayList` の基本操作を身につけます。

> 用語メモ: **List（リスト）** … 順番を保って値を並べる、長さが自由なコレクション。**ArrayList（アレイリスト）** … Listの代表的な実装。**コレクション** … 複数の値をまとめて扱う入れ物の総称。**`add` / `get` / `size`** … それぞれ「末尾に追加」「指定位置の取得」「個数」。

---

## 2. 詳細設計（この通りに実装する）

`Ex06` クラスに次のメソッドを実装してください。

| メソッド | 引数 | 戻り値 | 仕様 |
|---|---|---|---|
| `addAll` | `List<String> list, String... items` | `List<String>` | itemsを全部listの末尾に追加し、そのlistを返す |
| `removeShort` | `List<String> list, int minLen` | `List<String>` | 長さがminLen未満の文字列を除いた**新しいList** |
| `join` | `List<String> list, String sep` | `String` | 要素をsepでつないだ文字列（空なら`""`） |
| `indexOf` | `List<String> list, String target` | `int` | targetが最初に現れる位置（0始まり）。なければ`-1` |

> `String... items` は「可変長引数（かへんちょうひきすう）」です。`addAll(list, "a", "b", "c")` のように、いくつでも渡せます。メソッドの中では `items` を配列のように扱えます。

### ひな型（`Ex06.java`）

```java
import java.util.ArrayList;
import java.util.List;

public class Ex06 {

    public List<String> addAll(List<String> list, String... items) {
        return null; // TODO
    }

    public List<String> removeShort(List<String> list, int minLen) {
        return null; // TODO
    }

    public String join(List<String> list, String sep) {
        return null; // TODO
    }

    public int indexOf(List<String> list, String target) {
        return 0; // TODO
    }
}
```

---

## 3. まず動かしてみる（書いたコードを画面で確認）

テストの前に、**自分のコードが画面に結果を出す**ことを確認しましょう。「プログラムが動いた！」という実感があると、続ける力（自己効力感）になります。

1. 下の「動かしてみる用ランナー」を `Ex06Play.java` として `Ex06.java` と同じフォルダに保存する。
2. 次を実行する。

```bash
javac Ex06.java Ex06Play.java && java Ex06Play
```

3. 画面に `りんご / みかん / ぶどう` のような**リスト操作の結果**が出れば、まず成功です。

> 💡 `[PASS]` より先に、「動いた！」という実感を大事にしてください。このあと `Ex06Test` で正解判定します。
> 💡 ここで `null` やエラーが出ても大丈夫。あなたの実装がまだ途中なだけのサインです。エラーの行番号を見て、メソッドを1つずつ埋めていきましょう。
> 💡 `Ex06Play.java` 内のサンプル値（名前・数字など）を変えて再実行してみよう。

### 動かしてみる用ランナー（`Ex06Play.java`）

```java
/**
 * 課題06 動かしてみる用ランナー
 * 実行: javac Ex06.java Ex06Play.java && java Ex06Play
 */
import java.util.ArrayList;
import java.util.List;

public class Ex06Play {

    public static void main(String[] args) {
        Ex06 ex = new Ex06();

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        List<String> fruits = new ArrayList<>();
        ex.addAll(fruits, "りんご", "みかん", "ぶどう", "桃");
        System.out.println("果物リストに追加 → " + fruits);
        System.out.println("join(\" / \") → " + ex.join(fruits, " / "));
        System.out.println();

        List<String> filtered = ex.removeShort(fruits, 3);
        System.out.println("3文字未満を除く → " + filtered + " （「桃」だけ消える）");
        System.out.println("\"みかん\" の位置 → " + ex.indexOf(fruits, "みかん"));
        System.out.println();

        System.out.println("✨ リストの中身が変わって見えたら、List 操作は動いています！");
        System.out.println("次に Ex06Test で正解判定してください。");
    }
}
```

---

## 4. 完成条件

- `Ex06Test.java` を実行して **`ALL PASS ✅`** が出ること。

---

## 5. 検証方法

```bash
javac Ex06.java Ex06Test.java && java Ex06Test
```

### 自己採点用テストランナー（`Ex06Test.java`）

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ex06Test {

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
        Ex06 ex = new Ex06();

        List<String> base = new ArrayList<>();
        ex.addAll(base, "a", "bb", "ccc");
        check("addAll", Arrays.asList("a", "bb", "ccc"), base);

        List<String> src = new ArrayList<>(Arrays.asList("a", "bb", "ccc", "dddd"));
        check("removeShort", Arrays.asList("ccc", "dddd"), ex.removeShort(src, 3));
        // 元のListは壊さない
        check("removeShortKeepsOriginal", Arrays.asList("a", "bb", "ccc", "dddd"), src);

        check("join", "a,bb,ccc", ex.join(Arrays.asList("a", "bb", "ccc"), ","));
        check("joinEmpty", "", ex.join(new ArrayList<>(), ","));

        check("indexOf", 1, ex.indexOf(Arrays.asList("a", "bb", "ccc"), "bb"));
        check("indexOfNone", -1, ex.indexOf(Arrays.asList("a", "bb"), "x"));

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

## 6. つまずきポイントとヒント

<details>
<summary>List の長さや要素はどう取る？</summary>

`list.size()` で個数、`list.get(i)` で i番目の要素が取れます。配列の `arr.length` / `arr[i]` とは書き方が違うので注意。
</details>

<details>
<summary>removeShort で「新しいList」を作るには？</summary>

`new ArrayList<>()` で空のListを作り、条件に合う要素だけ `add` していきます。引数の `list` から要素を消す（`remove`）と、元のListを壊してしまうのでダメです。
</details>

<details>
<summary>文字列の比較は == ではダメ</summary>

文字列が等しいかは `a.equals(b)` で判定します。`==` は「同じ箱か」を見るので、見た目が同じでも `false` になることがあります。`indexOf` で使います。
</details>

<details>
<summary>join で末尾に余計な区切りが付く</summary>

`for` で位置 `i` を使い、`i > 0` のときだけ先に `sep` を足すと、末尾に余計な区切りが付きません。`StringBuilder` を使うと書きやすいです。
</details>

---

## 7. 模範解答への導線

先に自分で解いてから、[solutions/Ex06.java](../solutions/Ex06.java) と見比べてください。
