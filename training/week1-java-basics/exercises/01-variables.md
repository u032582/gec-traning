# 課題01: 変数と出力

**所要時間の目安**: 30分
**AI利用区分**: 🚫 コードを書かせるのは禁止（家庭教師用途のみ。エラーの意味を聞く／自分のコードのレビュー依頼／概念説明はOK）

---

## 1. 背景・狙い（なぜ学ぶか）

プログラムは「データを入れておく箱（**変数**）」を使って動きます。変数に名前を付けて値を入れ、それを組み立てて結果を作るのが、すべてのプログラムの基本です。
現場では「受け取った値（名前・年齢など）を、決められた形の文章に組み立てて返す」ような処理を山ほど書きます。今回はその一番やさしい形を、**メソッド（処理のまとまり）に切り出して書く**練習をします。

> 用語メモ: **変数（へんすう）** … 値を入れておく名前付きの箱。**String（ストリング）** … 文字列（もじれつ＝文章）を入れる型。**int（イント）** … 整数を入れる型。**型（かた）** … 箱に入れられる値の種類のこと。

### コラム: メソッドの形を読み解く（この課題でいちばん大事）

課題00では `main` という「決まった入口」を1つ書いただけでした。この課題からは、**自分でメソッドを定義する**ようになります。メソッドは、Javaのすべての処理の基本単位です。まず「形」を覚えましょう。

```java
public String greeting(String name) {
    return "こんにちは、" + name + "さん！";
}
```

この1つのメソッドは、次の部品でできています。

| 部品 | この例での値 | 意味 |
|---|---|---|
| 修飾子（しゅうしょくし） | `public` | 「外から呼んでいい」の印。今はこう書くもの、でOK（課題10で `private` と対で学びます）。 |
| **戻り値の型**（もどりちのかた） | `String` | このメソッドが**返す値の種類**。文字列を返すなら `String`、整数なら `int`。何も返さないなら `void`（課題10で出ます）。 |
| **メソッド名** | `greeting` | 処理に付けた名前。呼ぶときにこの名前を使います。 |
| **引数**（ひきすう） | `(String name)` | メソッドが**受け取る入力**。「`String` 型の値を `name` という名前で受け取る」という意味。 |
| 中身（処理） | `{ ... }` | 実際の処理。 |
| `return`（リターン） | `return "..."` | **値を呼び出し元に返して、メソッドを終える**命令。戻り値の型が `String` なら、`return` で返すのも `String` でなければなりません。 |

> 用語メモ: **メソッド** … 「入力（引数）を受け取り、何か処理して、結果（戻り値）を返す」処理のまとまり。**自動販売機**に似ています。お金とボタン（引数）を入れると、ジュース（戻り値）が出てくる。中の仕組みを知らなくても、入口と出口さえ分かれば使えます。

#### メソッドは「呼ばれて」初めて動く

メソッドは、**定義しただけでは動きません**。誰かが「呼ぶ」と動きます。この課題の `Ex01Play.java`（下記）を見ると、こう呼んでいます。

```java
Ex01 ex = new Ex01();              // ① Ex01 の「実物」を1つ用意する
String g1 = ex.greeting("田中");   // ② その実物の greeting を、"田中" を渡して呼ぶ
```

②で `"田中"` を渡すと、それが `greeting` の引数 `name` に入り、`return` で返ってきた文字列が `g1` に入ります。これが「引数を渡して、戻り値を受け取る」という、メソッドを使う基本動作です。

> 💡 ①の `new Ex01()` で**なぜわざわざ「実物」を作る必要があるのか**は、今はおまじないのままで構いません。これは「クラス（設計図）」と「インスタンス（設計図から作った実物）」の違いが分かると腑に落ちます。その違いは [課題10: クラス設計](10-class.md) で学び、**[課題10の最後の回収パート（§8）](10-class.md#8-回収main-を読み解く--なぜ-main-は-static-なのか)** で「なぜ `new` が要るメソッドと、要らないメソッド（`main` など）があるのか」がスッキリつながります。今は「メソッドは呼ばれて動く・引数を渡して戻り値を受け取る」だけ押さえれば十分です。

---

## 2. 詳細設計（この通りに実装する）

`Ex01` というクラスに、次の2つのメソッドを実装してください。

| メソッド | 引数 | 戻り値 | 仕様 |
|---|---|---|---|
| `greeting` | `String name` | `String` | `"こんにちは、" + name + "さん！"` を返す |
| `introduce` | `String name, int age` | `String` | `name`（`age`歳）です。 の形を返す |

`introduce` の出力形式は厳密に次のとおりです（全角カッコと「歳」「です。」に注意）:

```
田中（25歳）です。
```

### 実装ファイルのひな型（`Ex01.java`）

まずこのファイルを作り、`return null;` の部分を自分で書き換えてください。

```java
public class Ex01 {

    public String greeting(String name) {
        return null; // TODO: ここを実装する
    }

    public String introduce(String name, int age) {
        return null; // TODO: ここを実装する
    }
}
```

---

## 3. まず動かしてみる（書いたコードを画面で確認）

テストの前に、**自分のコードが画面に結果を出す**ことを確認しましょう。「プログラムが動いた！」という実感があると、続ける力（自己効力感）になります。

1. 下の「動かしてみる用ランナー」を `Ex01Play.java` として `Ex01.java` と同じフォルダに保存する。
2. 次を実行する。

```bash
javac Ex01.java Ex01Play.java && java Ex01Play
```

3. 画面に `こんにちは、田中さん！` のような**自分の作った文章**が出れば、まず成功です。

> 💡 `[PASS]` より先に、「動いた！」という実感を大事にしてください。このあと `Ex01Test` で正解判定します。
> 💡 ここで `null` やエラーが出ても大丈夫。あなたの実装がまだ途中なだけのサインです。エラーの行番号を見て、メソッドを1つずつ埋めていきましょう。
> 💡 `Ex01Play.java` の `"田中"` を自分の名前に変えて再実行してみよう。

### 動かしてみる用ランナー（`Ex01Play.java`）

```java
/**
 * 課題01 動かしてみる用ランナー
 * 実行: javac Ex01.java Ex01Play.java && java Ex01Play
 */
public class Ex01Play {

    public static void main(String[] args) {
        Ex01 ex = new Ex01();

        System.out.println("=== あなたのコードを動かしてみます ===");
        System.out.println();

        String g1 = ex.greeting("田中");
        System.out.println("greeting(\"田中\") の結果:");
        System.out.println("  → " + g1);
        System.out.println();

        String i1 = ex.introduce("田中", 25);
        System.out.println("introduce(\"田中\", 25) の結果:");
        System.out.println("  → " + i1);
        System.out.println();

        System.out.println("✨ 自分で作った文章が画面に出たら、うまく動いています！");
        System.out.println("次に Ex01Test で正解判定してください。");
    }
}
```

---

## 4. 完成条件

- 自己採点用テスト（`Ex01Test.java`）を実行して **`ALL PASS ✅`** が出ること。

---

## 5. 検証方法（自分で正解を確認する）

1. 下の「自己採点用テストランナー」を、`Ex01Test.java` というファイル名で `Ex01.java` と同じフォルダに保存する。
2. 次のコマンドを実行する。

```bash
javac Ex01.java Ex01Test.java && java Ex01Test
```

`ALL PASS ✅` が出れば合格です。失敗したケースがあれば、`[FAIL]` の行に「何を期待して、実際どうだったか」が出ます。

### 自己採点用テストランナー（`Ex01Test.java`）

```java
public class Ex01Test {

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
        Ex01 ex = new Ex01();

        check("greeting1", "こんにちは、田中さん！", ex.greeting("田中"));
        check("greeting2", "こんにちは、佐藤さん！", ex.greeting("佐藤"));
        check("introduce1", "田中（25歳）です。", ex.introduce("田中", 25));
        check("introduce2", "鈴木（30歳）です。", ex.introduce("鈴木", 30));

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
<summary>文字列はどうやってつなげるの？</summary>

Javaでは `+` で文字列をつなげられます。`"あ" + "い"` は `"あい"` になります。数値も `"年齢:" + 25` のように文字列につなげると `"年齢:25"` になります。
</details>

<details>
<summary>全角カッコ（）が出せない／コンパイルは通るのに FAIL になる</summary>

`introduce` のカッコは**全角の（）**です。半角の `()` ではテストに通りません。期待値の文字列をそのままコピーして使うと確実です。
</details>

<details>
<summary>「class Ex01 is public, should be declared in a file named Ex01.java」と出る</summary>

`public class` の名前と、ファイル名（`Ex01.java`）は一致させる必要があります。ファイル名を確認してください。
</details>

---

## 7. 模範解答への導線

先に自分で解いてから、[solutions/Ex01.java](../solutions/Ex01.java) と見比べてください。先に答えを見ると力がつきません。
