# 週4: 単体テスト + 総合課題 + コードリーディング

第1フェーズ（週0〜2・手書き）を終え、週3でAIを解禁したあとの週です。ここまでの「Java基礎 → SQL/DB → Spring BootでAPI」を土台に、**現場の実装工程を一通り自走できる**状態を目指します。
全体の進め方・質問テンプレート・日報フォーマットは、先に [研修トップのREADME](../README.md) を読んでください。

> この週は第2フェーズ（AI協働）の一部です。週3に続き、**AIにコードを書かせてよい**——ただし「読む・検証する・説明できる」が条件です。テストも総合課題も、AIを使ってよいぶん、生成物を鵜呑みにせず**自分で確かめる**癖をここで固めてください。週5以降の「AI協働の作法」「既存リポへの参画」の土台になります。

---

## 1. この週の狙い（到達目標）

> **現場の実装工程を一通り自走できる。**

具体的には、次の3つを身につけます。

1. **単体テストを書ける**: JUnit5 と Mockito で、自分のコードが正しいことを自動で確認できる。
2. **実装〜提出を一人で回せる**: 詳細設計を読んで、CRUD APIを実装し、テストを書き、Gitでプルリク（PR）として提出する。
3. **他人のコードを読んで直せる**: 既存コードを読み、悪いところを言語化し、振る舞いを変えずに改善する。

> 用語メモ: **単体テスト** … 関数やクラス1つが正しく動くかを自動で確認するコード。**JUnit5 / Mockito** … Javaでテストを書く道具。**CRUD** … Create/Read/Update/Delete の4操作。**プルリク（PR）** … 「このコードを取り込んでください」と提出する仕組み。

---

## 2. AI利用区分（この週のルール）

✅ **週4は第2フェーズ（AI協働）です。AIにコードを書かせてよい。** ただし条件があります。

- ✅ 生成されたコードを **自分の言葉で説明できること**
- ✅ **なぜその実装なのか** を言えること
- 各課題ファイルに「AI生成コードの説明記入欄」があります。**必ず埋めてください。**

> これは現場での「AI活用の練習」も兼ねています。中身を理解せず貼るだけの人は信用されません。コードリーディング課題では特に、**AIに「直して」と言う前に、まず自分で「どこが・なぜ悪いか」を言語化する**こと——これは第2フェーズでAIの改修案を評価する練習そのものです。
> AIにテストや改修を書かせて検証する作法は、この先の週（[週7: 安全な改修](../week7-safe-modification/)・[週8: テストと品質をAIで](../week8-test-quality-ai/)）でさらに深めます。
> 詳細は [研修トップのREADME「2. AI利用ルール」](../README.md#2-ai利用ルール最重要必ず守る) を参照。

---

## 3. 課題一覧（上から順に）

| # | 課題 | 学ぶこと | 目安 | 課題ファイル | 模範解答 |
|---|---|---|---|---|---|
| 1-1 | JUnit5の基本 | `@Test` / `assertEquals` / `assertThrows`、正常系・境界値・異常系 | 60-90分 | [01-junit-basics.md](exercises/01-junit-basics.md) | [PriceCalculatorTest.java](solutions/unit-test-exercises/src/test/java/com/example/training/PriceCalculatorTest.java) |
| 1-2 | Mockito | `@Mock` / `@InjectMocks` / `when().thenReturn()` / `verify()` | 90-120分 | [02-mockito-service.md](exercises/02-mockito-service.md) | [MemberPointServiceTest.java](solutions/unit-test-exercises/src/test/java/com/example/training/MemberPointServiceTest.java) |
| 2 | 総合課題（CRUD API） | 設計→実装→テスト→PR提出の一気通貫 | 2-3日 | [final-project-spec.md](final-project-spec.md) | [final-project-task-api/](solutions/final-project-task-api/) |
| 3 | コードリーディング+改修 | 既存コードを読み、振る舞いを変えず改善 | 120-180分 | [03-code-reading-refactor.md](exercises/03-code-reading-refactor.md) | [legacy-refactored/](solutions/legacy-refactored/) |

> 進め方の目安: Day1で課題1-1・1-2、Day2-3で総合課題、Day4で課題3、Day5（対面）で最終レビュー。

---

## 4. フォルダ構成

```
week4-test-and-final/
  README.md             ← いま読んでいるこのファイル
  final-project-spec.md ← 総合課題の詳細設計書
  exercises/            ← 課題（テストの小課題 + コードリーディング改修）
    01-junit-basics.md
    02-mockito-service.md
    03-code-reading-refactor.md
  legacy-code/          ← 課題3の題材（他人が書いた・動くが改善余地あり）
  solutions/            ← 模範解答
    unit-test-exercises/   課題1群のテスト対象クラス + 模範テスト
    final-project-task-api/ 総合課題の参考実装
    legacy-refactored/      課題3の改修後コード + 解説
```

---

## 5. テストの実行環境（Gradle）

週3と同じく **Gradle** を使います。週3との整合: **Java 21 / Spring Boot 3.3系 / JUnit5 / Mockito**。
テスト道具（JUnit5・Mockito）は `spring-boot-starter-test` に**最初から入っている**ので、自分で追加する必要はありません。

### 実行方法（全課題共通）

各Gradleプロジェクト（`build.gradle` がある階層）に移動して:

```bash
./gradlew test
```

- 緑（成功）なら最後に `BUILD SUCCESSFUL` が出ます。
- 赤（失敗）なら `FAILED` と、どのテストが・何を期待して・実際どうだったかが出ます。これを読んで直すのも練習です。

> 用語メモ: **Gradle（グレイドル）** … ビルドやテスト実行をまとめてやってくれる道具。`gradlew`（Gradle Wrapper）は、各自のPCに正しいバージョンのGradleを自動で用意してくれる仕組みなので、Gradle本体を別途インストールする必要はありません。
> 初回はライブラリのダウンロードで時間がかかります。ネット接続を確認してください。
> Antigravity（WSL接続状態）でも、内蔵ターミナルを開いて `./gradlew test` を実行できます。テストクラスを開けば各テストの横に出る実行ボタン（▶）からも実行できます。

---

## 6. Git / PR（プルリク）提出の手順 — 初心者向け

総合課題（課題2）は、最後に **PR（プルリク）として提出** します。Gitに不慣れでも大丈夫なように、手順を1つずつ書きます。

> 用語メモ: **ブランチ** … 作業を本流から枝分かれさせる仕組み。失敗しても本流を汚さない。**コミット** … 変更を1つの区切りとして記録すること。**push（プッシュ）** … 自分のPCの記録を、リモート（GitHubなど共有の置き場）に送ること。**PR（プルリク / プルリクエスト）** … 「このブランチの変更を取り込んでください」とレビュー依頼を出す仕組み。

### ステップ1: 作業用ブランチを作る
本流（main）から枝分かれします。ブランチ名は `feature/やること` の形にするのが現場の慣習です。
```bash
git switch -c feature/task-api
```
> `git switch -c` は「新しいブランチを作って、そこに移動する」コマンドです。

### ステップ2: こまめにコミットする
1機能できるたびに記録します。コミットメッセージは、研修の規約（[CLAUDE風の規約はトップREADME参照]、簡単には `feat: 〜` `fix: 〜` `test: 〜` のように頭に種類を付ける）に軽く沿わせます。
```bash
git add .
git commit -m "feat: タスク一覧取得APIを実装"
```
> メッセージは「何をしたか」が後から分かる短い日本語でOK。例: `feat: タスク作成APIとバリデーションを追加` / `test: TaskServiceの単体テストを追加`。

### ステップ3: リモートへ push する
共有の置き場（GitHub等）に送ります。初回は次のように上流（つながり先）を指定します。
```bash
git push -u origin feature/task-api
```

### ステップ4: PR（プルリク）を作る
- **GitHub等のリモートがある場合**: pushすると、GitHubの画面に「Compare & pull request」ボタンが出ます。押して、タイトルと「何を作ったか・どう確認したか」を書いて作成。タイトル例: `タスク管理APIの実装（総合課題）`。
- 本文には最低限、次を書きます:
  ```
  ## やったこと
  - タスク管理APIのCRUD 5エンドポイントを実装
  - TaskServiceの単体テストを5本追加（./gradlew test 緑）

  ## 動作確認
  - final-project-spec.md の検証curlを順に実行し、設計どおりの応答を確認
  ```

### リモートが無い / まだ用意されていない場合（フォールバック）
講師の指示に従ってください。リモートが無くても、次のどちらかで「提出」できます。

1. **ローカルでブランチと差分を見せる**（対面レビュー時）:
   ```bash
   git log --oneline           # コミットの履歴を見せる
   git diff main..feature/task-api  # mainとの差分（変更点）を見せる
   ```
2. **講師が指定するリモートに push する**: 講師からリポジトリURLを教わり、
   ```bash
   git remote add origin <講師が指定するURL>
   git push -u origin feature/task-api
   ```
   その後、上のステップ4でPRを作る。

> どの方法でも、見てもらうのは「ブランチを切って・意味のある単位でコミットして・差分が読める」状態になっているか、です。ここが提出の本質です。

---

## 7. この週の完了判定（自己採点）

- [ ] 課題1-1: `PriceCalculator` のテストを書き、`./gradlew test` が緑
- [ ] 課題1-2: `MemberPointService` のテストを Mockito で書き、緑
- [ ] 総合課題: タスク管理APIが設計どおり動き（curl確認）、単体テストが緑、**PRが出ている**
- [ ] 課題3: legacy-code を改修し、改修後も characterization test が緑。悪い点を4つ以上言語化した
- [ ] 各課題の「AI生成コードの説明記入欄」を埋めた

全部✅になれば、週4 = 研修全体の到達目標 達成です。おつかれさまでした。

---

## 8. もっとやりたい人へ（追加課題）

- 総合課題に「ステータスで絞り込む一覧」（例: `GET /api/tasks?status=todo`）を追加してみる。
- 総合課題の Controller を `@WebMvcTest` + `MockMvc` でテストしてみる（HTTPレベルのテスト）。
- legacy-code に「もし新しい地域を追加するなら、改修前と改修後でどちらが楽か」を、実際に1地域足して比べてみる。

---

## 9. 困ったときに見る場所

| 困りごと | 見る場所 |
|---|---|
| 進め方が分からない | [研修トップのREADME](../README.md) |
| Gradle/テストが動かない | このファイルの「5. テストの実行環境」 |
| Git/PRのやり方が分からない | このファイルの「6. Git / PR 提出の手順」 |
| 総合課題の仕様 | [final-project-spec.md](final-project-spec.md) |
| 質問の仕方 | [研修トップのREADME「3. 質問テンプレート」](../README.md#3-質問テンプレート詰まったときの聞き方) |
