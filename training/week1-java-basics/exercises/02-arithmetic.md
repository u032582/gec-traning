# 課題02: 四則演算と型変換

**所要時間の目安**: 40分
**AI利用区分**: 🚫 コードを書かせるのは禁止（家庭教師用途のみ）

---

## 1. 背景・狙い（なぜ学ぶか）

計算はプログラムの中心です。とくに業務システムでは「金額」「個数」「平均」などの計算が頻繁に出てきます。
ここでつまずきやすいのが **型（かた）** の違いです。Javaでは整数（`int`）同士の割り算は、答えの小数が**切り捨て**られます（`7 / 2` は `3.5` ではなく `3`）。この落とし穴を理解し、必要なときに小数（`double`）へ**変換（キャスト）**できるようになるのが今回の狙いです。これを知らないと、現場で「金額がずれる」バグを生みます。

> 用語メモ: **double（ダブル）** … 小数を入れる型。**キャスト** … 型を別の型に変換すること。`(double) a` や `(int) b` のように書く。

---

## 2. 詳細設計（この通りに実装する）

`Ex02` クラスに次のメソッドを実装してください。

| メソッド | 引数 | 戻り値 | 仕様 |
|---|---|---|---|
| `add` | `int a, int b` | `int` | `a + b` |
| `subtract` | `int a, int b` | `int` | `a - b` |
| `multiply` | `int a, int b` | `int` | `a * b` |
| `divideAsDouble` | `int a, int b` | `double` | `a ÷ b` を**小数で**返す（例: 7,2 → 3.5） |
| `average` | `int a, int b, int c` | `double` | 3つの平均（例: 1,2,3 → 2.0） |
| `toInt` | `double value` | `int` | 小数を**切り捨て**て整数にする（例: 3.9 → 3） |

### ひな型（`Ex02.java`）

```java
public class Ex02 {

    public int add(int a, int b) {
        return 0; // TODO
    }

    public int subtract(int a, int b) {
        return 0; // TODO
    }

    public int multiply(int a, int b) {
        return 0; // TODO
    }

    public double divideAsDouble(int a, int b) {
        return 0; // TODO
    }

    public double average(int a, int b, int c) {
        return 0; // TODO
    }

    public int toInt(double value) {
        return 0; // TODO
    }
}
```

---

## 3. まず動かしてみる（書いたコードを画面で確認）

テストの前に、**自分のコードが画面に結果を出す**ことを確認しましょう。「プログラムが動いた！」という実感があると、続ける力（自己効力感）になります。

1. 下の「動かしてみる用ランナー」を `Ex02Play.java` として `Ex02.java` と同じフォルダに保存する。
2. 次を実行する。

```bash
javac Ex02.java Ex02Play.java && java Ex02Play
```

3. 画面に `5 + 3 = 8` のような**計算結果**が出れば、まず成功です。

> 💡 `[PASS]` より先に、「動いた！」という実感を大事にしてください。このあと `Ex02Test` で正解判定します。
> 💡 ここで `null` やエラーが出ても大丈夫。あなたの実装がまだ途中なだけのサインです。エラーの行番号を見て、メソッドを1つずつ埋めていきましょう。
> 💡 `Ex02Play.java` 内のサンプル値（名前・数字など）を変えて再実行してみよう。

### 動かしてみる用ランナー（`Ex02Play.java`）

```java
/**
 * 課題02 動かしてみる用ランナー
 * 実行: javac Ex02.java Ex02Play.java && java Ex02Play
 */
public class Ex02Play {

    public static void main(String[] args) {
        Ex02 ex = new Ex02();

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        System.out.println("5 + 3 = " + ex.add(5, 3));
        System.out.println("5 - 3 = " + ex.subtract(5, 3));
        System.out.println("5 × 3 = " + ex.multiply(5, 3));
        System.out.println("7 ÷ 2 = " + ex.divideAsDouble(7, 2) + " （小数）");
        System.out.println("(10 + 20 + 30) ÷ 3 = " + ex.average(10, 20, 30));
        System.out.println("3.9 を整数に = " + ex.toInt(3.9));
        System.out.println();

        System.out.println("✨ 数字が画面に出たら、計算処理は動いています！");
        System.out.println("次に Ex02Test で正解判定してください。");
    }
}
```

---

## 4. 完成条件

- `Ex02Test.java` を実行して **`ALL PASS ✅`** が出ること。

---

## 5. 検証方法

```bash
javac Ex02.java Ex02Test.java && java Ex02Test
```

### 自己採点用テストランナー（`Ex02Test.java`）

```java
public class Ex02Test {

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

    // double は誤差が出ることがあるので、ごく小さな差は同じとみなす
    static void checkDouble(String caseName, double expected, double actual) {
        total++;
        if (Math.abs(expected - actual) < 1e-9) {
            System.out.println("[PASS] " + caseName);
        } else {
            fail++;
            System.out.println("[FAIL] " + caseName + ": 期待=" + expected + ", 実際=" + actual);
        }
    }

    public static void main(String[] args) {
        Ex02 ex = new Ex02();

        check("add", 8, ex.add(5, 3));
        check("subtract", 2, ex.subtract(5, 3));
        check("multiply", 15, ex.multiply(5, 3));
        checkDouble("divideAsDouble", 3.5, ex.divideAsDouble(7, 2));
        checkDouble("divideAsDouble2", 2.5, ex.divideAsDouble(5, 2));
        checkDouble("average", 20.0, ex.average(10, 20, 30));
        checkDouble("average2", 2.0, ex.average(1, 2, 3));
        check("toInt", 3, ex.toInt(3.9));
        check("toInt2", 7, ex.toInt(7.1));

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
<summary>divideAsDouble が 3.0 になってしまう（小数が出ない）</summary>

`int / int` は整数の割り算になり、小数が切り捨てられます。`a / b` の前に片方を `double` にしてください。例: `(double) a / b`。`(double)(a / b)` だと**先に整数割り算してから**変換するので意味が変わります。カッコの位置に注意。
</details>

<details>
<summary>average が整数っぽい値になる</summary>

`(a + b + c) / 3` だと整数割り算です。`3.0`（小数）で割ると小数のまま計算されます。
</details>

<details>
<summary>toInt の切り捨てはどう書く？</summary>

`(int) value` と書くと、小数部分が切り捨てられて整数になります（3.9 → 3）。四捨五入ではない点に注意。
</details>

---

## 7. 模範解答への導線

先に自分で解いてから、[solutions/Ex02.java](../solutions/Ex02.java) と見比べてください。
