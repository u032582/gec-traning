# 課題08: Set

**所要時間の目安**: 40分
**AI利用区分**: 🚫 コードを書かせるのは禁止（家庭教師用途のみ）

---

## 1. 背景・狙い（なぜ学ぶか）

**Set（セット）**は「重複（じゅうふく）を許さない集合（しゅうごう）」です。同じ値を2回入れても1つにまとまります。「ユニークな（重複のない）値の数を数える」「2つのグループの共通部分を出す」といった処理にぴったりです。
現場では「重複を取り除きたい」「すでに登録済みか確認したい」場面でよく使います。Listとの違い（順番より重複の有無を気にする）を体で覚えるのが今回の狙いです。

> 用語メモ: **Set（セット）** … 重複しない値の集まり。**HashSet（ハッシュセット）** … Setの代表的な実装。**`add`** … 追加（すでにあれば追加されず`false`を返す）。**`contains`** … 含まれているか判定。**積集合（せきしゅうごう）** … 両方にある要素。**差集合（さしゅうごう）** … 片方にしかない要素。

---

## 2. 詳細設計（この通りに実装する）

`Ex08` クラスに次のメソッドを実装してください。

| メソッド | 引数 | 戻り値 | 仕様 |
|---|---|---|---|
| `unique` | `List<Integer> list` | `int` | 重複を除いた要素数 |
| `hasDuplicate` | `List<Integer> list` | `boolean` | 重複が1つでもあれば`true` |
| `intersection` | `Set<Integer> a, Set<Integer> b` | `Set<Integer>` | 両方に含まれる要素のSet（積集合） |
| `difference` | `Set<Integer> a, Set<Integer> b` | `Set<Integer>` | aにあってbにない要素のSet（差集合） |

### ひな型（`Ex08.java`）

```java
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ex08 {

    public int unique(List<Integer> list) {
        return 0; // TODO
    }

    public boolean hasDuplicate(List<Integer> list) {
        return false; // TODO
    }

    public Set<Integer> intersection(Set<Integer> a, Set<Integer> b) {
        return null; // TODO
    }

    public Set<Integer> difference(Set<Integer> a, Set<Integer> b) {
        return null; // TODO
    }
}
```

---

## 3. 完成条件

- `Ex08Test.java` を実行して **`ALL PASS ✅`** が出ること。

---

## 4. 検証方法

```bash
javac Ex08.java Ex08Test.java && java Ex08Test
```

### 自己採点用テストランナー（`Ex08Test.java`）

```java
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Ex08Test {

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

    static Set<Integer> setOf(Integer... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    public static void main(String[] args) {
        Ex08 ex = new Ex08();

        check("unique", 3, ex.unique(Arrays.asList(1, 2, 2, 3, 3, 3)));
        check("uniqueAll", 4, ex.unique(Arrays.asList(1, 2, 3, 4)));

        check("hasDupTrue", true, ex.hasDuplicate(Arrays.asList(1, 2, 2)));
        check("hasDupFalse", false, ex.hasDuplicate(Arrays.asList(1, 2, 3)));

        check("intersection", setOf(2, 3), ex.intersection(setOf(1, 2, 3), setOf(2, 3, 4)));
        check("intersectionEmpty", setOf(), ex.intersection(setOf(1, 2), setOf(3, 4)));

        check("difference", setOf(1), ex.difference(setOf(1, 2, 3), setOf(2, 3, 4)));
        check("differenceAll", setOf(1, 2), ex.difference(setOf(1, 2), setOf(3, 4)));

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
<summary>unique（重複を除いた数）を一発で出すには？</summary>

`new HashSet<>(list)` とすると、Listをそのまま重複なしのSetに変換できます。あとはその `size()` を返すだけです。
</details>

<details>
<summary>hasDuplicate を効率よく書くには？</summary>

`set.add(v)` は「すでに同じ値があった場合 `false` を返す」性質があります。これを使い、`add` が `false` になった瞬間に「重複あり」と判定できます。
</details>

<details>
<summary>積集合・差集合の考え方</summary>

積集合は「a の各要素のうち、b にも `contains` されるものだけ」を新しいSetに集めます。差集合は「b に `contains` されない（!）もの」を集めます。
</details>

---

## 6. 模範解答への導線

先に自分で解いてから、[solutions/Ex08.java](../solutions/Ex08.java) と見比べてください。
