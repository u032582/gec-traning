# 課題09: 例外処理（try-catch・自作例外）

**所要時間の目安**: 50分
**AI利用区分**: 🚫 コードを書かせるのは禁止（家庭教師用途のみ）

---

## 1. 背景・狙い（なぜ学ぶか）

プログラムは「想定外のこと」が起きると**例外（れいがい）**を投げて止まります。0で割る、数字でない文字列を数値に変換する、などです。
現場では「異常が起きたときにどう振る舞うか」を必ず設計します。そのまま落とすのか、既定値を返すのか、独自のエラーを投げて呼び出し側に知らせるのか。今回は `try-catch` で例外を受け止める方法と、**自作の例外クラス**を投げる方法を学びます。エラーハンドリングは「動けばいい」を卒業するための重要な一歩です。

> 用語メモ: **例外（れいがい）** … 異常が起きたことを表す仕組み。**try-catch** … `try` の中で起きた例外を `catch` で受け止める構文。**throw（スロー）** … 例外を投げること。**検査例外（けんされいがい）** … `Exception` を継承した、呼び出し側に `throws` か `try-catch` を強制するタイプの例外。

---

## 2. 詳細設計（この通りに実装する）

`Ex09` クラスに次のメソッドを実装してください。さらに、**自作例外 `InsufficientBalanceException`** を同じファイルに定義します。

| メソッド | 引数 | 戻り値 | 仕様 |
|---|---|---|---|
| `safeDivide` | `int a, int b` | `int` | `a / b`。bが0なら例外を**握って**0を返す |
| `parseOrDefault` | `String s, int defaultValue` | `int` | sを整数に変換。できない（数字でない・null）なら`defaultValue` |
| `withdraw` | `int balance, int amount` | `int` | balanceからamountを引いた残高。amountがbalanceより大きいなら`InsufficientBalanceException`を投げる |

`withdraw` は検査例外を投げるので、メソッド宣言に `throws InsufficientBalanceException` が必要です。

### ひな型（`Ex09.java`）

```java
public class Ex09 {

    public int safeDivide(int a, int b) {
        return 0; // TODO: try-catch で 0除算を握る
    }

    public int parseOrDefault(String s, int defaultValue) {
        return 0; // TODO: Integer.parseInt と try-catch
    }

    public int withdraw(int balance, int amount) throws InsufficientBalanceException {
        return 0; // TODO: 残高不足なら throw new InsufficientBalanceException(...)
    }
}

// 自作の検査例外（このファイル内に書く）
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
```

---

## 3. 完成条件

- `Ex09Test.java` を実行して **`ALL PASS ✅`** が出ること。

---

## 4. 検証方法

```bash
javac Ex09.java Ex09Test.java && java Ex09Test
```

### 自己採点用テストランナー（`Ex09Test.java`）

```java
public class Ex09Test {

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
        Ex09 ex = new Ex09();

        check("safeDivide", 3, ex.safeDivide(6, 2));
        check("safeDivideZero", 0, ex.safeDivide(6, 0));

        check("parseOK", 123, ex.parseOrDefault("123", -1));
        check("parseNG", -1, ex.parseOrDefault("abc", -1));
        check("parseNull", -1, ex.parseOrDefault(null, -1));

        // 正常系: 引き出せる
        try {
            check("withdrawOK", 700, ex.withdraw(1000, 300));
        } catch (InsufficientBalanceException e) {
            fail++;
            total++;
            System.out.println("[FAIL] withdrawOK: 例外が出てはいけないのに出た -> " + e.getMessage());
        }

        // 異常系: 残高不足なら例外が投げられること
        total++;
        try {
            ex.withdraw(1000, 1500);
            fail++;
            System.out.println("[FAIL] withdrawNG: 例外が投げられるべきなのに投げられなかった");
        } catch (InsufficientBalanceException e) {
            System.out.println("[PASS] withdrawNG");
        }

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
<summary>0で割ると何の例外が出る？</summary>

`int` の0除算は `ArithmeticException` が出ます。`try { return a / b; } catch (ArithmeticException e) { return 0; }` の形で握れます。
</details>

<details>
<summary>parseOrDefault で null や "abc" のとき</summary>

`Integer.parseInt(s)` は、数字でない文字列や null のとき `NumberFormatException` を投げます（null も同じ catch で受かります）。そこで `defaultValue` を返します。
</details>

<details>
<summary>自作例外を「投げる」書き方</summary>

`throw new InsufficientBalanceException("メッセージ");` で投げます。メソッド側には `throws InsufficientBalanceException` の宣言が必要です（ひな型に書いてあります）。
</details>

<details>
<summary>「unreported exception ... must be caught or declared to be thrown」と出る</summary>

検査例外を投げるメソッドには、`throws` 宣言が必要、というJavaのルールです。`withdraw` の宣言に `throws InsufficientBalanceException` があるか確認してください。
</details>

---

## 6. 模範解答への導線

先に自分で解いてから、[solutions/Ex09.java](../solutions/Ex09.java) と見比べてください。
