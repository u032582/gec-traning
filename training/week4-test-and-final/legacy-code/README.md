# legacy-code: コードリーディング+改修の題材

このフォルダは、課題3（[exercises/03-code-reading-refactor.md](../exercises/03-code-reading-refactor.md)）で読む・直す対象です。

- `src/main/java/com/example/legacy/ShippingFeeCalculator.java` … 「他人が書いた」配送料計算クラス。**一応動きます**。
- `src/test/java/com/example/legacy/ShippingFeeCalculatorCharacterizationTest.java` … 振る舞いを固定するテスト（characterization test）。

## まず動かして「緑」を確認する

改修を始める前に、現状のテストが通ること（=今の振る舞い）を確認します。

```bash
./gradlew test
```

`BUILD SUCCESSFUL` になればOK。**この緑を、改修の最初から最後までずっと保つ**のがこの課題のルールです。

## 課題のやり方

詳しい手順・完成条件・ヒントは [exercises/03-code-reading-refactor.md](../exercises/03-code-reading-refactor.md) を見てください。

> このREADMEには「どこが悪いか」の答えは書いていません。コードを自分で読んで見つけるのが課題です。
