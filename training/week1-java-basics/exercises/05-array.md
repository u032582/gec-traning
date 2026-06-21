# 課題05: 配列

**所要時間の目安**: 50分
**AI利用区分**: 🚫 コードを書かせるのは禁止（家庭教師用途のみ）

---

## 1. 背景・狙い（なぜ学ぶか）

**配列（はいれつ）**は「同じ型の値を、決まった個数だけ並べて持つ」入れ物です。「5教科の点数」「7日分の売上」のように、複数の値をまとめて扱うときに使います。
ループ（課題04）と組み合わせて「全部足す」「最大を探す」といった**集計（しゅうけい）**ができるようになるのが今回の狙いです。配列は次に学ぶ List の土台でもあります。

> 用語メモ: **配列** … `int[] scores` のように書く、同じ型の値を並べた入れ物。**要素（ようそ）** … 配列の中の1つ1つの値。**インデックス（添字）** … 何番目かを表す番号。**0から**始まる（先頭は `arr[0]`）。**`arr.length`** … 配列の長さ（個数）。

---

## 2. 詳細設計（この通りに実装する）

`Ex05` クラスに次のメソッドを実装してください。引数の配列は `null` ではないものとします。

| メソッド | 引数 | 戻り値 | 仕様 |
|---|---|---|---|
| `sum` | `int[] arr` | `int` | 合計（空配列は0） |
| `max` | `int[] arr` | `int` | 最大値（空配列は`Integer.MIN_VALUE`） |
| `count` | `int[] arr, int target` | `int` | targetと一致する要素の個数 |
| `reverse` | `int[] arr` | `int[]` | 逆順にした**新しい配列**（元配列は変更しない） |

> `Integer.MIN_VALUE` は int で表せる最小の値です。「空っぽで最大が決められない」ことを表す目印に使います。

### ひな型（`Ex05.java`）

```java
public class Ex05 {

    public int sum(int[] arr) {
        return 0; // TODO
    }

    public int max(int[] arr) {
        return 0; // TODO
    }

    public int count(int[] arr, int target) {
        return 0; // TODO
    }

    public int[] reverse(int[] arr) {
        return null; // TODO
    }
}
```

---

## 3. まず動かしてみる（書いたコードを画面で確認）

テストの前に、**自分のコードが画面に結果を出す**ことを確認しましょう。「プログラムが動いた！」という実感があると、続ける力（自己効力感）になります。

1. 下の「動かしてみる用ランナー」を `Ex05Play.java` として `Ex05.java` と同じフォルダに保存する。
2. 次を実行する。

```bash
javac Ex05.java Ex05Play.java && java Ex05Play
```

3. 画面に `合計 → 14` や `逆順 → [5, 1, 4, 1, 3]` のような**配列の変換結果**が出れば、まず成功です。

> 💡 `[PASS]` より先に、「動いた！」という実感を大事にしてください。このあと `Ex05Test` で正解判定します。

### 動かしてみる用ランナー（`Ex05Play.java`）

```java
/**
 * 課題05 動かしてみる用ランナー
 * 実行: javac Ex05.java Ex05Play.java && java Ex05Play
 */
public class Ex05Play {

    static String formatArray(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        Ex05 ex = new Ex05();
        int[] nums = {3, 1, 4, 1, 5};

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        System.out.println("配列 " + formatArray(nums));
        System.out.println("  合計 → " + ex.sum(nums));
        System.out.println("  最大 → " + ex.max(nums));
        System.out.println("  1 の個数 → " + ex.count(nums, 1));
        System.out.println("  逆順 → " + formatArray(ex.reverse(nums)));
        System.out.println();

        System.out.println("✨ 配列の数字が変換されて見えたら、配列処理は動いています！");
        System.out.println("次に Ex05Test で正解判定してください。");
    }
}
```

---

## 4. 完成条件

- `Ex05Test.java` を実行して **`ALL PASS ✅`** が出ること。

---

## 5. 検証方法

```bash
javac Ex05.java Ex05Test.java && java Ex05Test
```

### 自己採点用テストランナー（`Ex05Test.java`）

```java
import java.util.Arrays;

public class Ex05Test {

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

    static void checkArray(String caseName, int[] expected, int[] actual) {
        total++;
        if (Arrays.equals(expected, actual)) {
            System.out.println("[PASS] " + caseName);
        } else {
            fail++;
            System.out.println("[FAIL] " + caseName + ": 期待=" + Arrays.toString(expected)
                    + ", 実際=" + Arrays.toString(actual));
        }
    }

    public static void main(String[] args) {
        Ex05 ex = new Ex05();

        check("sum", 15, ex.sum(new int[]{1, 2, 3, 4, 5}));
        check("sumEmpty", 0, ex.sum(new int[]{}));

        check("max", 9, ex.max(new int[]{3, 9, 1, 7}));
        check("maxNeg", -1, ex.max(new int[]{-5, -1, -3}));

        check("count", 3, ex.count(new int[]{1, 2, 2, 3, 2}, 2));
        check("count0", 0, ex.count(new int[]{1, 2, 3}, 9));

        checkArray("reverse", new int[]{5, 4, 3, 2, 1}, ex.reverse(new int[]{1, 2, 3, 4, 5}));

        // reverse は元配列を壊さないこと
        int[] original = {1, 2, 3};
        ex.reverse(original);
        checkArray("reverseKeepsOriginal", new int[]{1, 2, 3}, original);

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
<summary>配列を全部回るには？</summary>

`for (int v : arr) { ... }`（拡張for文）で、要素を1つずつ `v` に取り出せます。番号が必要なときは `for (int i = 0; i < arr.length; i++)` を使います。
</details>

<details>
<summary>max の初期値を 0 にすると失敗する</summary>

すべて負の数の配列だと、初期値0が最大に残ってしまいます。**最初の要素 `arr[0]` を仮の最大値**にして、空配列のときだけ `Integer.MIN_VALUE` を返すようにしましょう。
</details>

<details>
<summary>reverse で「元配列は変更しない」とは？</summary>

引数の `arr` を書き換えてはいけません。`new int[arr.length]` で新しい配列を作り、そこに逆順で詰めて返します。`arr[arr.length - 1 - i]` が逆順の要素です。
</details>

---

## 7. 模範解答への導線

先に自分で解いてから、[solutions/Ex05.java](../solutions/Ex05.java) と見比べてください。
