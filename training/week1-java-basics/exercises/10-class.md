# 課題10: クラス設計（フィールド・コンストラクタ・メソッド・カプセル化）

**所要時間の目安**: 60分
**AI利用区分**: 🚫 コードを書かせるのは禁止（家庭教師用途のみ）

---

## 1. 背景・狙い（なぜ学ぶか）

ここまでは「計算する処理」を書いてきました。ここからは **データと、それを扱う処理を1つにまとめた「クラス」** を作ります。これがオブジェクト指向（しこう）の入り口です。
たとえば「銀行口座」は、データ（名義・残高）と操作（入金・出金）を持ちます。これを `BankAccount` という1つのクラスにまとめます。ポイントは **カプセル化（かぷせるか）**: データ（残高）を外から勝手にいじれないように隠し、必ず決められたメソッド（入金・出金）を通させること。これにより「残高がマイナスになる」ようなおかしな状態を防げます。現場のクラス設計はすべてこの考え方が土台です。

> 用語メモ: **クラス** … データと処理をまとめた設計図。**フィールド** … クラスが持つデータ（変数）。**コンストラクタ** … クラスからモノ（インスタンス）を作るときに最初に呼ばれる初期化処理。**カプセル化** … フィールドを `private` で隠し、メソッド経由でだけ触らせること。**`this`** … 「このインスタンス自身」を指す。

---

## 2. 詳細設計（この通りに実装する）

`BankAccount` クラスを実装してください。`Ex10.java` というファイルの中に書きます（ファイル名に合わせて、空の `public class Ex10 {}` も置きます。`BankAccount` はその下に書きます）。

### `BankAccount` の仕様

- **フィールド**（どちらも `private`）:
  - `String owner` … 口座名義
  - `int balance` … 残高
- **コンストラクタ**: `BankAccount(String owner, int initialBalance)`
- **メソッド**:

| メソッド | 引数 | 戻り値 | 仕様 |
|---|---|---|---|
| `getOwner` | なし | `String` | 名義を返す |
| `getBalance` | なし | `int` | 残高を返す |
| `deposit` | `int amount` | `void` | 残高をamount増やす。amountが0以下なら何もしない |
| `withdraw` | `int amount` | `boolean` | amountが0以下、または残高超過なら何もせず`false`。成功したら残高を減らして`true` |

> `void`（ボイド）は「戻り値なし」を表します。

### ひな型（`Ex10.java`）

```java
public class Ex10 {
    // ファイル名(Ex10.java)に合わせるための入れ物。中身は空でよい。
}

class BankAccount {
    private String owner;
    private int balance;

    public BankAccount(String owner, int initialBalance) {
        // TODO: フィールドに値をセットする（this を使う）
    }

    public String getOwner() {
        return null; // TODO
    }

    public int getBalance() {
        return 0; // TODO
    }

    public void deposit(int amount) {
        // TODO
    }

    public boolean withdraw(int amount) {
        return false; // TODO
    }
}
```

---

## 3. まず動かしてみる（書いたコードを画面で確認）

テストの前に、**自分のコードが画面に結果を出す**ことを確認しましょう。「プログラムが動いた！」という実感があると、続ける力（自己効力感）になります。

1. 下の「動かしてみる用ランナー」を `Ex10Play.java` として `Ex10.java` と同じフォルダに保存する。
2. 次を実行する。

```bash
javac Ex10.java Ex10Play.java && java Ex10Play
```

3. 画面に `残高: 1200 円` のように**口座残高が変わる様子**が出れば、まず成功です。

> 💡 `[PASS]` より先に、「動いた！」という実感を大事にしてください。このあと `Ex10Test` で正解判定します。
> 💡 ここで `null` やエラーが出ても大丈夫。あなたの実装がまだ途中なだけのサインです。エラーの行番号を見て、メソッドを1つずつ埋めていきましょう。
> 💡 `Ex10Play.java` 内のサンプル値（名前・数字など）を変えて再実行してみよう。

### 動かしてみる用ランナー（`Ex10Play.java`）

```java
/**
 * 課題10 動かしてみる用ランナー
 * 実行: javac Ex10.java Ex10Play.java && java Ex10Play
 */
public class Ex10Play {

    public static void main(String[] args) {
        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        BankAccount account = new BankAccount("田中", 1000);
        System.out.println("口座を開設 → 名義: " + account.getOwner() + " / 残高: " + account.getBalance() + " 円");

        account.deposit(500);
        System.out.println("500円 入金後 → 残高: " + account.getBalance() + " 円");

        boolean ok = account.withdraw(300);
        System.out.println("300円 引き出し " + (ok ? "成功" : "失敗") + " → 残高: " + account.getBalance() + " 円");

        ok = account.withdraw(2000);
        System.out.println("2000円 引き出し " + (ok ? "成功" : "失敗") + " → 残高: " + account.getBalance() + " 円");
        System.out.println();

        System.out.println("✨ 口座の残高が変わって見えたら、クラス設計は動いています！");
        System.out.println("次に Ex10Test で正解判定してください。");
    }
}
```

---

## 4. 完成条件

- `Ex10Test.java` を実行して **`ALL PASS ✅`** が出ること。

---

## 5. 検証方法

```bash
javac Ex10.java Ex10Test.java && java Ex10Test
```

### 自己採点用テストランナー（`Ex10Test.java`）

```java
public class Ex10Test {

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
        BankAccount acc = new BankAccount("田中", 1000);

        check("owner", "田中", acc.getOwner());
        check("initialBalance", 1000, acc.getBalance());

        acc.deposit(500);
        check("afterDeposit", 1500, acc.getBalance());

        acc.deposit(-100); // 0以下は無視
        check("depositIgnoreNegative", 1500, acc.getBalance());

        check("withdrawOK", true, acc.withdraw(300));
        check("afterWithdraw", 1200, acc.getBalance());

        check("withdrawTooMuch", false, acc.withdraw(99999));
        check("balanceUnchanged", 1200, acc.getBalance());

        check("withdrawNegative", false, acc.withdraw(-50));
        check("balanceUnchanged2", 1200, acc.getBalance());

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
<summary>コンストラクタで this を使う理由</summary>

引数名 `owner` とフィールド名 `owner` が同じだと、区別が必要です。`this.owner = owner;` の `this.owner` は「フィールドのほう」を指します。
</details>

<details>
<summary>deposit / withdraw のガード（条件チェック）</summary>

仕様で「0以下なら何もしない」「残高超過なら何もしない」と決まっています。処理の先頭で `if` を使い、条件に外れたら早めに `return`（withdrawなら `return false;`）するのが読みやすい書き方です。
</details>

<details>
<summary>「cannot find symbol: BankAccount」と出る</summary>

`Ex10.java` の中に `BankAccount` クラスがちゃんと定義されているか確認してください。`public` は付けません（1ファイルにpublicクラスは1つだけ、という制約のため）。
</details>

---

## 7. 模範解答への導線

先に自分で解いてから、[solutions/Ex10.java](../solutions/Ex10.java) と見比べてください。
