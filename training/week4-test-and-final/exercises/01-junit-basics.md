# 課題1-1: JUnit5で「純粋ロジック」のテストを書く

**所要時間の目安**: 60〜90分

**AI利用区分**: ✅ AIにコードを書かせてよい。ただし生成されたテストを**自分の言葉で説明でき、なぜそのケースを選んだか言えること**が必須。

---

## AI生成コードの説明記入欄（提出時に必ず埋める）

AIにテストを書かせた場合は、ここを自分の言葉で埋めてください。空欄のまま提出は不可です。

```
【何を確かめるテストを書いたか（自分の言葉で）】


【なぜこのテストケースを選んだか（正常系・境界値・異常系の観点で）】


【assertEquals と assertThrows の違いを説明すると】

```

---

## 1. 背景・狙い（現場でどう使うか）

現場では「自分が書いたコードが正しく動くこと」を、**自動で何度でも確認できる形（=テスト）**で残します。手で画面をポチポチ確認するのは、変更のたびに崩れるし、見落とすからです。

> 用語メモ: **単体テスト（たんたいテスト / ユニットテスト）** … 関数やクラスといった「1つの部品」が正しく動くかを、コードで自動チェックすること。
> 用語メモ: **JUnit5（ジェイユニットファイブ）** … Javaで単体テストを書くための定番の道具。`spring-boot-starter-test` に最初から入っている。

この課題では、一番テストを書きやすい「純粋ロジック（入力だけで答えが決まる計算）」を題材に、JUnit5の基本を体に入れます。

---

## 2. テスト対象（この内容のクラスが `solutions/` に動く形で置いてある）

`com.example.training.PriceCalculator` クラス。**このクラスはすでに完成しています**。あなたが書くのは、このクラスに対する**テスト**です。

```java
public class PriceCalculator {

    // 税込み価格を返す。税込み = 税抜き × 1.10 を「切り捨て」て整数で返す。
    // 例: 100→110, 150→165, 199→218。税抜きが負なら IllegalArgumentException。
    public int withTax(int priceWithoutTax) { ... }

    // 割引後価格を返す。割引後 = 価格 × (100 - 割引率) ÷ 100 を「切り捨て」て整数。
    // 例: (1000,20)→800, (150,10)→135。
    // 割引率は0〜100のみ許可（範囲外は例外）。価格が負も例外。
    public int applyDiscount(int price, int discountRate) { ... }
}
```

---

## 3. 詳細仕様: あなたが書くテスト

`src/test/java/com/example/training/PriceCalculatorTest.java` を作り、次の**観点**を満たすテストメソッドを書いてください。メソッド名は自由ですが、何を確かめるか分かる名前にすること。

### `withTax` のテスト（最低5本）
| # | 観点 | 入力 | 期待 |
|---|---|---|---|
| 1 | 正常系（基本） | 100 | 110 |
| 2 | 正常系（基本） | 150 | 165 |
| 3 | 正常系（切り捨て確認） | 199 | 218 |
| 4 | 境界値（0円） | 0 | 0 |
| 5 | 異常系（負の価格） | -1 | `IllegalArgumentException` が投げられる |

### `applyDiscount` のテスト（最低7本）
| # | 観点 | 入力 | 期待 |
|---|---|---|---|
| 1 | 正常系 | (1000, 20) | 800 |
| 2 | 正常系 | (150, 10) | 135 |
| 3 | 境界値（0%引き） | (500, 0) | 500 |
| 4 | 境界値（100%引き） | (500, 100) | 0 |
| 5 | 異常系（割引率が負） | (1000, -1) | 例外 |
| 6 | 異常系（割引率が100超） | (1000, 101) | 例外 |
| 7 | 異常系（価格が負） | (-1, 10) | 例外 |

### 使うJUnit5の道具
```java
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

@Test
void 何を確かめるか() {
    PriceCalculator calc = new PriceCalculator();
    assertEquals(110, calc.withTax(100));                 // 期待, 実際 の順
    assertThrows(IllegalArgumentException.class, () -> calc.withTax(-1)); // 例外確認
}
```

> 用語メモ: **境界値（きょうかいち）テスト** … 「ちょうど0」「上限ちょうど」など、条件の切り替わり目を狙うテスト。バグはこの境目に潜みやすい。

---

## 4. 完成条件（自分で判定できる）

- [ ] `withTax` のテストを5本以上書いた（正常系・境界値・異常系を含む）
- [ ] `applyDiscount` のテストを7本以上書いた（正常系・境界値・異常系を含む）
- [ ] `./gradlew test` が**緑（全テスト通過）**で終わる
- [ ] 上の「AI生成コードの説明記入欄」を埋めた

---

## 5. 検証方法（叩くコマンドと期待結果）

このGradleプロジェクトの場所（`build.gradle` がある階層）で:

```bash
./gradlew test
```

**期待結果**: 最後に次が出る。

```
BUILD SUCCESSFUL in ...
```

失敗すると、どのテストが落ちたか（`FAILED`）と、期待値と実際値の差が表示されます。これを読んで直すのも練習のうちです。

---

## 6. つまずきポイントとヒント

<details>
<summary>`./gradlew` が動かない / Permission denied と出る</summary>

- 実行権限が必要なことがあります。`chmod +x gradlew` を一度実行。
- 初回はライブラリのダウンロードで時間がかかります。ネットにつながっているか確認。
- それでもダメなら、Cursor（WSL接続状態）でプロジェクトを開き、テストクラスのテストメソッド横に出る実行ボタン（▶）から実行しても構いません。
</details>

<details>
<summary>`assertThrows` の書き方が分からない</summary>

第1引数に「期待する例外クラス」、第2引数に「その例外を起こすはずの処理」をラムダ式 `() -> ...` で渡します。
```java
assertThrows(IllegalArgumentException.class, () -> calc.withTax(-1));
```
処理が例外を投げれば成功、投げなければ失敗になります。
</details>

<details>
<summary>「テストクラスはどこに置く？」</summary>

`src/test/java/com/example/training/` の下です。`src/main` ではなく `src/test` なのがポイント。パッケージ宣言は `package com.example.training;` にします。
</details>

---

## 7. 模範解答への導線

自分で書いて `./gradlew test` が緑になったら、`solutions/unit-test-exercises/src/test/java/com/example/training/PriceCalculatorTest.java` と見比べてください。**先に答えを見ないこと。** 自分のケースの選び方と模範解答の差を見ると、テスト観点の感覚が育ちます。
