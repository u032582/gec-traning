# 課題04: for文・while文

**所要時間の目安**: 50分
**AI利用区分**: 🚫 コードを書かせるのは禁止（家庭教師用途のみ）

---

## 1. 背景・狙い（なぜ学ぶか）

同じ処理を何度も繰り返すのが**ループ（繰り返し）**です。「リストの全件を処理する」「合計を求める」など、現場のコードはループだらけです。
回数が決まっているときは `for`、終わる条件で回すときは `while` が向いています。今回は両方を使い分けます。**この週で一番大事な基礎**なので、Stream APIに進む前にここを必ず固めてください。

> 用語メモ: **for文** … 「何回繰り返すか」を書きやすいループ。**while文** … 「条件が成り立つ間ずっと」繰り返すループ。**階乗（かいじょう）** … `n! = 1×2×…×n`。例: `5! = 120`。

---

## 2. 詳細設計（この通りに実装する）

`Ex04` クラスに次のメソッドを実装してください。

| メソッド | 引数 | 戻り値 | 仕様 |
|---|---|---|---|
| `sumTo` | `int n` | `int` | 1からnまでの合計（nが0以下なら0） |
| `factorial` | `int n` | `long` | nの階乗（`0!`は1） |
| `countDigits` | `int n` | `int` | 整数nの桁数（nは0以上。0は1桁） |
| `fizzbuzz` | `int n` | `String` | 1からnを改行`\n`でつないだ文字列（下記） |

**fizzbuzzのルール**: 1からnまで順に、3の倍数は`"Fizz"`、5の倍数は`"Buzz"`、両方（15の倍数）は`"FizzBuzz"`、どれでもなければその数字。各行を`\n`でつなぐ。

```
fizzbuzz(5) -> "1\n2\nFizz\n4\nBuzz"
```

> `long`（ロング）は `int` より大きな整数を入れられる型です。階乗はすぐ大きくなるので `long` にしています。`return 1L;` のように末尾に `L` を付けると long のリテラル（定数）になります。

### ひな型（`Ex04.java`）

```java
public class Ex04 {

    public int sumTo(int n) {
        return 0; // TODO
    }

    public long factorial(int n) {
        return 0; // TODO
    }

    public int countDigits(int n) {
        return 0; // TODO
    }

    public String fizzbuzz(int n) {
        return null; // TODO
    }
}
```

---

## 3. 完成条件

- `Ex04Test.java` を実行して **`ALL PASS ✅`** が出ること。

---

## 4. 検証方法

```bash
javac Ex04.java Ex04Test.java && java Ex04Test
```

### 自己採点用テストランナー（`Ex04Test.java`）

```java
public class Ex04Test {

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
        Ex04 ex = new Ex04();

        check("sumTo10", 55, ex.sumTo(10));
        check("sumTo1", 1, ex.sumTo(1));
        check("sumTo0", 0, ex.sumTo(0));

        check("factorial5", 120L, ex.factorial(5));
        check("factorial0", 1L, ex.factorial(0));
        check("factorial1", 1L, ex.factorial(1));

        check("countDigits0", 1, ex.countDigits(0));
        check("countDigits7", 1, ex.countDigits(7));
        check("countDigits100", 3, ex.countDigits(100));
        check("countDigits12345", 5, ex.countDigits(12345));

        check("fizzbuzz5", "1\n2\nFizz\n4\nBuzz", ex.fizzbuzz(5));
        check("fizzbuzz15tail", "FizzBuzz", ex.fizzbuzz(15).substring(ex.fizzbuzz(15).lastIndexOf("\n") + 1));

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
<summary>for文の基本の形がわからない</summary>

`for (int i = 1; i <= n; i++) { ... }` で「iを1からnまで1ずつ増やしながら繰り返す」になります。`{}` の中で `i` を使って処理します。
</details>

<details>
<summary>factorial の倍率を掛けるとき、初期値は何にする？</summary>

掛け算で積み上げるときの初期値は **1** です（0だと全部0になります）。`0!` と `1!` がどちらも1になるよう、ループの初期値と範囲に注意してください。
</details>

<details>
<summary>countDigits を while でどう数える？</summary>

`n` を10で割り続け、`n` が0になるまで「割った回数」を数えると桁数になります。`n /= 10;` で1桁ずつ削れます。ただし `n == 0` のときだけは特別扱い（答えは1桁）にしてください。
</details>

<details>
<summary>fizzbuzz の改行のつなぎ方</summary>

`StringBuilder` を使うと文字列を効率よく組み立てられます。最初の要素以外の前に `\n` を付けると、末尾に余計な改行が付きません。15の倍数（FizzBuzz）の判定を**先に**書くのがコツです。
</details>

---

## 6. 模範解答への導線

先に自分で解いてから、[solutions/Ex04.java](../solutions/Ex04.java) と見比べてください。
