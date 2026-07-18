# 題材リポジトリ 設計仕様書（週6〜11で使い回す既存システム）

このファイルは、**教材を作る側**が読む設計仕様書です（新人向けではありません）。
第2フェーズ後半（週6〜11）で新人が「参画する既存システム」として使い回す、題材アプリの設計を定義します。

> なぜ1本に固定するのか: [CLAUDE.md §3](../../CLAUDE.md) と [curriculum.md](../curriculum.md) は「**題材リポを1本用意し、週6〜11で使い回す**」ことを不変条件としています。週6（読解）→週7（改修）→週8（テスト）→週9〜11（参画）が**同じ既存リポ**を対象にすることで、「既存システムに参画する」体験が連続する。バラバラの題材にしてはいけません。

---

## 0. この題材が満たすべき条件（なぜこの題材か）

curriculum.md が週6〜11で新人にさせることは決まっています。題材はそれを**全部支えられる**必要があります。

| 週 | させること | 題材に必要な性質 |
|---|---|---|
| 週6 | 処理フローを追い、3層のどこに何があるか地図化 | **複数エンティティが関連**し、読み解く価値がある規模 |
| 週7 | バグ修正・小機能追加、影響範囲を見極め、リグレッション確認 | **状態の整合**があり、自然なバグを仕込める／変更が波及しうる |
| 週8 | AIにテストを書かせ、品質を人間が担保 | テストしがいのある**分岐・異常系**がある |
| 週9〜11 | 複数機能をPR単位で追加、レビュー往復 | **機能追加の余地**が明確に複数ある |

### 選定した題材: 図書貸出管理システム（Library Lending）

図書館（社内文庫でもよい）の蔵書を、利用者が**借りて・返す**のを管理するシステム。

**なぜこれか**:
- **複数エンティティが関連する**（書籍 / 貸出記録 / 利用者）。単一テーブルのCRUDだった週4の総合課題（タスク管理）より一段複雑で、「読み解く」価値がある。
- **状態の整合がある**（「貸し出すと貸出可能数が減る」「返すと戻る」「貸出中の本は貸せない」）。ここに**自然なバグ**を仕込め（週7）、直すと**別の箇所に波及**しうる（リグレッション＝週8）。
- **機能追加の余地が複数明確**（延滞一覧・著者/カテゴリ絞り込み・貸出上限・予約 …）。週9〜11のタスク源になる。
- 既存教材の題材（顧客API・商品API・タスク管理API）と**被らない**。図書という誰でも直感の効くドメインなので、新人が仕様を理解する負荷が低い（本筋の"読解・改修"に集中できる）。

---

## 1. 既存資産との整合（先人の設計に合わせる）

この題材リポは、ゼロから独自流儀で作らず、**既存の週3/週4のSpring Boot資産の美学の延長**に置きます（CLAUDE.md §0「先人の設計思想に敬意を払う」）。

| 項目 | 既存資産に合わせる値 | 出典 |
|---|---|---|
| 言語/バージョン | Java 21 | 週3/週4 build.gradle |
| フレームワーク | Spring Boot 3.3系 | 同上 |
| DBアクセス | MyBatis（Mapperインターフェース + XML） | 週3 CustomerMapper, 週4 TaskMapper |
| DB | PostgreSQL（週0で作った `training` DB） | 週3 sample-schema.sql |
| ビルド | Gradle（gradlew同梱） | 全プロジェクト |
| ルートパッケージ | `com.example.training` | 週3/週4すべて |
| 層構成 | Controller / Service / Mapper(+XML) / エンティティ / DTO | 週3 customer, 週4 task |
| 例外処理 | `@RestControllerAdvice` による共通ハンドラ + 独自例外 | 週4 GlobalExceptionHandler |

> ただし**パッケージのサブ構成だけは意図的に変える**: 週3/4は `com.example.training.customer` のように「機能名の単一パッケージ」だった。題材リポは複数エンティティなので `com.example.training.book` / `.lending` / `.member` のように**エンティティごとにパッケージを分ける**。これは「中規模リポの歩き方」を学ぶ週6の題材として自然で、かつ現場の構成に近い。

---

## 2. ドメインモデル（テーブル設計）

3テーブル。関連は「1人の利用者が複数の貸出をする」「1冊の書籍が複数回貸し出される」。

### `members`（利用者）
| 列 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | BIGSERIAL | PK | |
| name | VARCHAR(100) | NOT NULL | 氏名 |
| email | VARCHAR(255) | NOT NULL UNIQUE | メール（会員識別） |
| created_at | TIMESTAMP | NOT NULL DEFAULT now | |

### `books`（書籍）
| 列 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | BIGSERIAL | PK | |
| title | VARCHAR(200) | NOT NULL | 書名 |
| author | VARCHAR(100) | NOT NULL | 著者 |
| category | VARCHAR(50) | NOT NULL | 分類（技術書 / 小説 / … ） |
| total_count | INT | NOT NULL CHECK(>=0) | 蔵書数（同じ本の総冊数） |
| available_count | INT | NOT NULL CHECK(>=0) | 貸出可能な残冊数。**貸出で減り返却で増える（状態の整合ポイント）** |
| created_at | TIMESTAMP | NOT NULL DEFAULT now | |

### `lendings`（貸出記録）
| 列 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | BIGSERIAL | PK | |
| book_id | BIGINT | NOT NULL FK→books | 借りた書籍 |
| member_id | BIGINT | NOT NULL FK→members | 借りた人 |
| lent_at | DATE | NOT NULL | 貸出日 |
| due_date | DATE | NOT NULL | 返却期限 |
| returned_at | DATE | NULL | 返却日。NULLなら**貸出中** |

> **状態の整合（バグと影響範囲の源泉）**: 「貸し出す」と `lendings` に1行増え、`books.available_count` が1減る。「返す」と `returned_at` が入り、`available_count` が1戻る。この2つが**噛み合っていること**が、このシステムの正しさの核。週7で「返却しても在庫が戻らない」等のバグを仕込む余地がここにある。

---

## 3. API仕様（初期実装として題材リポに載せるエンドポイント）

題材リポには、**すでに動く状態で**次を実装しておく（新人が読む既存コード）。

| メソッド | パス | 概要 | 層をまたぐ処理 |
|---|---|---|---|
| GET | /api/books | 書籍一覧（category で絞り込み可） | Controller→Service→Mapper |
| GET | /api/books/{id} | 書籍1件（無ければ404） | 〃 |
| POST | /api/lendings | 貸出する（貸出可能数を1減らす） | **在庫チェック＋更新の複合処理**（Serviceに集約） |
| POST | /api/lendings/{id}/return | 返却する（貸出可能数を1戻す） | **状態遷移＋在庫更新** |
| GET | /api/members/{id}/lendings | ある利用者の貸出履歴 | 結合的な取得 |

> 貸出APIには**業務ルール**が入る（在庫0なら貸せない＝異常系、返却済みをもう一度返せない＝異常系）。ここが週8で「意味のあるテスト」を書く対象になり、週7で「ルールの抜け」をバグとして仕込める。

---

## 4. 各週がこの題材をどう使うか（作問の指針）

実際の課題（exercises）は各週フォルダで作る。ここでは**題材リポ側が用意しておくべきもの**を定義する。

- **週6（読解）**: 課題は「`POST /api/lendings` を叩いたとき、貸出可能数が減るまでの処理をファイル名・行で追え」等。→ 題材リポは**素直だが追いがいのある3層**であること。奇をてらった構造にしない。
- **週7（改修）**: バグ票と小機能要望を出す。→ 題材リポに**意図的なバグを1〜2個仕込んだブランチ or issue** と、「これが直ればOK」のテストを用意。リグレッションを起こしうる作りにする。
- **週8（テスト）**: → 週7で触った箇所に**テストが手薄な状態**を作っておき、AIと協働で埋めさせる。
- **週9〜11（参画）**: 機能追加タスク（延滞一覧 `GET /api/lendings/overdue`、貸出上限、予約 …）を**PR単位**で複数出す。→ 題材リポにこれらの**着地点（どこに足すか）が自然にある**こと。

> Git運用は [CLAUDE.md §3](../../CLAUDE.md) のとおり「**同一リポでブランチを切ってPR**」で統一。フォークは使わない。題材リポは新人が clone してブランチを切る前提の単独Gradleプロジェクトとして置く。

---

## 5. 物理構成（これから作るファイル）

```
project-repo/
  DESIGN.md              ← このファイル（作り手用の設計仕様）
  README.md              ← 新人が最初に読む「このアプリは何か」（週6の入口）
  build.gradle / settings.gradle / gradlew ...   ← 週3/4と同じGradle一式
  db/
    schema.sql           ← 3テーブル + サンプルデータ
  src/main/java/com/example/training/
    LibraryApplication.java
    book/     Book.java / BookController.java / BookService.java / BookMapper.java / BookResponse.java
    lending/  Lending.java / LendingController.java / LendingService.java / LendingMapper.java / LendingRequest.java / LendingResponse.java
    member/   Member.java / MemberController.java / MemberService.java / MemberMapper.java
    common/   GlobalExceptionHandler.java / NotFoundException.java / BusinessRuleException.java
  src/main/resources/
    application.yml
    mapper/  BookMapper.xml / LendingMapper.xml / MemberMapper.xml
  src/test/java/...       ← 最低限のテスト（週8で"手薄さ"を残す設計）
```

---

## 6. 作成の段階（この設計に合意後の実装順）

一度に全部作らず、次の順で。各段で「動く」を確認しながら進める（教材の§0.6「動いた実感」を作り手側でも守る）。

1. **土台**: Gradle一式（週4のをベースに流用）+ `application.yml` + `schema.sql`（3テーブル+サンプル）。`./gradlew build` が通る空アプリまで。 ← **今回ここまで + 2**
2. **book 縦切り1本**: 書籍の一覧・1件取得を3層で。`bootRun`→`curl`で動作確認。 ← **今回ここまで**
3. **member + lending**: 利用者、貸出・返却（在庫の整合を含む業務ルール）。ここがこのアプリの核。
4. **common**: 例外ハンドラ・独自例外で異常系（404・在庫0・二重返却）を整える。
5. **最低限のテスト**: LendingService の主要分岐だけ。週8で埋める余地を意図的に残す。
6. **README.md**: 新人向けに「このアプリは何をするか・どう起動するか・全体像」を書く（週6の入口）。

> 各段でコミットを分け、コミット本文に「どの層まで動くか」を書く。新人がgit logを読んで成り立ちを追えるようにする（週6の副教材になる）。

---

## 7. 未確定・要判断（実装前に決めること）

- **意図的バグ（週7）をどこに仕込むか**: 「返却で在庫を戻し忘れる」「在庫0チェックが `<` で境界を1件見逃す」等の候補を、週7の作問時に確定。題材リポ本体は**正しく動く状態**で置き、バグは週7フォルダ側で「壊したブランチ/差分」として与えるのが安全（本体を汚さない）。
- **サンプルデータの量**: 動作確認しやすく、かつ一覧が"読める"程度（書籍5〜8件・利用者3件・貸出3〜4件）。
- **週9〜11の機能追加タスクの正式リスト**: 延滞一覧・カテゴリ集計・貸出上限・予約 から、難易度順に3〜4本を後で確定。
