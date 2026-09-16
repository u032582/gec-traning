# 付録 課題01: 同じデータから3つ作る（List / Set / Map）

**目安時間**: 40分
**前に読むもの**: [付録のREADME](../README.md)（3つの違いと選び方）

---

## 1. この課題の狙い

**1つの貸出履歴から、List・Set・Map を3つとも作ります。**

覚えてほしいのは書き方ではありません。**同じデータでも、聞かれていることが違えば入れ物が変わる**——それを自分の画面で見ることです。

---

## 2. 作業場所

自分の作業フォルダ（`work/<あなたのハンドル>/appendix-collections/` など）を作り、その中で作業してください。

---

## 3. 書くもの

`Col01.java` を作り、次の3つのメソッドを埋めてください。**中身は空のまま貼って、上から順に実装**していきます。

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Col01 {

    /**
     * ① List —— 最後に借りた3冊を「新しい順」で返す。
     * 借りた履歴は古い順に入っている。3冊に満たなければ、ある分だけ返す。
     */
    public List<String> latest3(List<String> borrowed) {
        return null; // TODO: 後ろから3つ取って、新しい順に詰める
    }

    /**
     * ② Set —— これまでに借りたことのある本は、何種類か。
     */
    public int kinds(List<String> borrowed) {
        return 0; // TODO: 重複を消して数える
    }

    /**
     * ③ Map —— 本ごとの貸出回数を返す。
     */
    public Map<String, Integer> countByTitle(List<String> borrowed) {
        return null; // TODO: 本（キー）ごとに数える
    }
}
```

---

## 4. まず動かしてみる（Play）

**正解判定より先に、画面に結果を出します。** 下の `Col01Play.java` を同じフォルダに作って実行してください。

```bash
javac Col01.java Col01Play.java && java Col01Play
```

<details>
<summary><b>【クリックで開く】Col01Play.java の全文（コピーして使ってください）</b></summary>

```java
/**
 * 付録01 動かしてみる用ランナー
 * 実行: javac Col01.java Col01Play.java && java Col01Play
 */
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Col01Play {

    public static void main(String[] args) {
        Col01 col = new Col01();

        // 貸出履歴（古い順）。同じ本を何度も借りている。
        List<String> borrowed = Arrays.asList(
                "リーダブルコード", "ノルウェイの森", "リーダブルコード",
                "人月の神話", "ノルウェイの森", "リーダブルコード");

        System.out.println("=== 同じ1つのデータから、3つ作ってみます ===");
        System.out.println();
        System.out.println("元の貸出履歴（古い順・" + borrowed.size() + "件）");
        System.out.println("  " + borrowed);
        System.out.println();

        System.out.println("① List  最近借りた3冊（新しい順） → " + col.latest3(borrowed));
        System.out.println("     ねらい: 順番が意味を持つ。同じ本が何度出てきてもよい。");
        System.out.println();

        System.out.println("② Set   借りたことのある本の種類  → " + col.kinds(borrowed) + " 種類");
        System.out.println("     中身はこう: " + new HashSet<>(borrowed));
        System.out.println("     ねらい: 重複が消える。並び順は保証されない。");
        System.out.println();

        System.out.println("③ Map   本ごとの貸出回数          → " + col.countByTitle(borrowed));
        System.out.println("     ねらい: 名前（キー）から値をすぐ引ける。");
        System.out.println();

        System.out.println("--------");
        System.out.println("6件の履歴が、3冊・3種類・{本=回数} と別の形になりました。");
        System.out.println("同じデータでも、聞かれていることが違えば入れ物も変わります。");
        System.out.println();
        System.out.println("※ ここで null や空っぽが出ても、実装が途中なだけです。あわてなくて大丈夫。");
        System.out.println("✨ 3つとも中身が出たら動いています！ 次に Col01Test で正解判定してください。");
    }
}
```

</details>

うまくいくと、こんなふうに出ます。

```
元の貸出履歴（古い順・6件）
  [リーダブルコード, ノルウェイの森, リーダブルコード, 人月の神話, ノルウェイの森, リーダブルコード]

① List  最近借りた3冊（新しい順） → [リーダブルコード, ノルウェイの森, 人月の神話]
② Set   借りたことのある本の種類  → 3 種類
③ Map   本ごとの貸出回数          → {ノルウェイの森=2, 人月の神話=1, リーダブルコード=3}
```

**6件が、3冊・3種類・{本=回数} の3つの形になりました。** ここが今日いちばん見てほしいところです。

> **まだ実装していないうちは、`null` や `0` が出ます。** 実装が途中なだけなので、あわてなくて大丈夫です。1つ実装するたびに走らせて、出るものが増えていくのを見てください。

> 履歴の本のタイトルを**自分が読んだ本に変えて**実行してみるのもおすすめです。自分のデータだと、結果の意味が急にはっきりします。

---

## 5. 正解判定（Test）

3つとも画面に出たら、テストランナーで判定します。

```bash
javac Col01.java Col01Test.java && java Col01Test
```

`ALL PASS ✅` が出れば完了です。

<details>
<summary><b>【クリックで開く】Col01Test.java の全文（コピーして使ってください）</b></summary>

```java
/**
 * 付録01 自己採点用テストランナー
 * 実行: javac Col01.java Col01Test.java && java Col01Test
 */
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Col01Test {

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
        Col01 col = new Col01();

        List<String> borrowed = Arrays.asList(
                "リーダブルコード", "ノルウェイの森", "リーダブルコード",
                "人月の神話", "ノルウェイの森", "リーダブルコード");

        // ① List: 新しい順に3冊。重複はそのまま残る。
        check("latest3",
                Arrays.asList("リーダブルコード", "ノルウェイの森", "人月の神話"),
                col.latest3(borrowed));
        check("latest3Short",
                Arrays.asList("B", "A"),
                col.latest3(Arrays.asList("A", "B")));
        check("latest3Empty",
                Arrays.asList(),
                col.latest3(Arrays.asList()));

        // ② Set: 重複が消える。
        check("kinds", 3, col.kinds(borrowed));
        check("kindsAllSame", 1, col.kinds(Arrays.asList("A", "A", "A")));
        check("kindsEmpty", 0, col.kinds(Arrays.asList()));

        // ③ Map: キーごとの件数。
        Map<String, Integer> expected = new HashMap<>();
        expected.put("リーダブルコード", 3);
        expected.put("ノルウェイの森", 2);
        expected.put("人月の神話", 1);
        check("countByTitle", expected, col.countByTitle(borrowed));
        check("countByTitleEmpty", new HashMap<String, Integer>(), col.countByTitle(Arrays.asList()));

        System.out.println("--------");
        if (fail == 0) {
            System.out.println("ALL PASS ✅");
        } else {
            System.out.println(fail + " 件失敗（テスト合計 " + total + " 件）");
        }
    }
}
```

</details>

---

## 6. つまずきポイントとヒント

<details>
<summary>① 「新しい順」がうまく作れない</summary>

履歴は**古い順**に入っているので、**後ろから**取り出します。

```java
for (int i = borrowed.size() - 1; i >= 0; i--) {
    // borrowed.get(i) が、新しいほうから順に出てくる
}
```

3冊で止めるには、詰めたリストの `size()` が3になったらループを抜けます。

**3冊に満たないときも動くか**、確かめてください（2件しかない履歴を渡しても落ちないこと）。テストにそのケースが入っています。
</details>

<details>
<summary>② 重複を消す書き方が思い出せない</summary>

`Set` に入れ直すだけです。

```java
Set<String> titles = new HashSet<>(borrowed);
return titles.size();
```

`new HashSet<>(list)` で、**リストを丸ごと渡して重複を落とせます**。1行で済むので、これは覚えてしまってよい形です。
</details>

<details>
<summary>③ 数え方が分からない</summary>

決まり文句があります。

```java
counts.put(title, counts.getOrDefault(title, 0) + 1);
```

「いまの数（**まだ無ければ0**）に1を足して、入れ直す」。`getOrDefault` を使うと「初回だけ `null` になる」を気にしなくて済みます。

これを、履歴を1件ずつ回しながら実行します。
</details>

<details>
<summary>Map の比較で FAIL になる</summary>

`HashMap` は**並び順を保証しません**。でもテストは順番を見ていないので、中身（キーと値の組）が合っていれば PASS します。

FAIL が出ているなら、順番ではなく**数が合っていない**はずです。`Col01Play` の出力を見て、どの本の回数がずれているか確かめてください。
</details>

---

## 7. 模範解答

`ALL PASS` になってから見てください。

→ [solutions/Col01.java](../solutions/Col01.java) / [Col01Play.java](../solutions/Col01Play.java) / [Col01Test.java](../solutions/Col01Test.java)

---

## 8. 終わったら

[付録のREADME](../README.md) の「2. どれを選ぶか」の表を、もう一度見てください。**自分で3つ書いたあとだと、表の意味が変わって見える**はずです。

これで、次に「どれを使えばいい？」と迷ったとき、戻ってくる場所ができました。
