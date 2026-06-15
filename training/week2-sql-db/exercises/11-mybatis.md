# 課題11（最終課題）: MyBatisでJavaからDBを叩く

- **所要時間の目安**: 90分
- **AI利用区分**: 🚫 AIにコードを書かせるのは禁止。家庭教師用途のみ（エラーの意味/レビュー/概念説明）。設定ファイルの意味を聞くのはOK。

> 用語メモ: **MyBatis（マイバティス）** … Javaのプログラムから、自分で書いたSQLを実行し、その結果をJavaのオブジェクト（クラス）に詰めてくれる道具。「SQLは自分で書きたいが、結果の受け取りは楽にしたい」というときに使います。この研修ではJPAではなくMyBatisに絞ります。

これは週2の総仕上げです。これまで `psql` で叩いていたSQLを、**Javaプログラムから**実行します。週3でSpring Bootを使う前の、その土台になります。

---

## 背景・狙い（現場でどう使うか）

現場のバックエンドは「Javaのプログラムが、DBからデータを取ってきて、加工して、画面やAPIに返す」のが基本動作です。その「DBから取ってくる」部分をMyBatisが担います。この課題で、**SQL（あなたが書く）→ MyBatisが実行 → 結果がJavaオブジェクトに入る → 表示** という一連の流れを、最小構成で体験します。

> ⚠️ ライブラリ導入で詰まりすぎないよう、**動く完全な構成を `solutions/mybatis-sample/` に用意してあります**。あなたが書くのは、マッパー（SQLを実行するJavaの窓口）の **1メソッドの中身だけ**です。それ以外（ビルド設定・接続設定・main・ドメインクラス）は完成済みです。

---

## 前提

- 週0で `training` データベースを作成済み、`schema.sql` を流し込み済みであること。
  - まだなら: `psql -h localhost -U postgres -d training -f schema.sql` を実行。
- JDK 21 が使えること（`java -version` で 21.x）。
- ビルドは **Gradle** を使います（`solutions/mybatis-sample/` に Gradle 設定一式あり）。Gradle 自体のインストールは不要です（後述の `gradlew` を使うため）。

> 用語メモ: **Gradle（グレイドル）** … Javaのビルド（必要なライブラリのダウンロード＋コンパイル＋実行）をまとめてやってくれる道具。**gradlew（グレイドルラッパー）** … Gradle本体が無くても、プロジェクト同梱のスクリプトが自動でGradleを用意して動かしてくれる仕組み。

---

## 課題で使うプロジェクトの場所

完成済みのサンプル一式が次にあります。

```
solutions/mybatis-sample/
  build.gradle                         # 依存（MyBatis 3.5系 + PostgreSQL JDBC 42系）を書いたビルド設定
  settings.gradle
  src/main/java/com/example/
      App.java                         # main。ここから実行する（完成済み）
      Customer.java                    # 顧客1件を表すドメインクラス（完成済み）
      CustomerMapper.java              # ★ ここに 1メソッドを実装する（あなたの作業）
      MyBatisSessionFactory.java       # MyBatisの初期化（完成済み）
  src/main/resources/
      mybatis-config.xml               # DB接続情報・マッパー登録（完成済み・要パスワード書き換え）
      com/example/CustomerMapper.xml   # SQLを書くXML（完成済み）
```

> 用語メモ:
> **ドメインクラス** … DBの1行を、Javaのオブジェクトとして表すための入れ物クラス（今回は `Customer`）。
> **マッパー（Mapper）** … 「このメソッドが呼ばれたら、このSQLを実行する」という対応づけ。Javaのインターフェース（`CustomerMapper.java`）と、SQLを書いたXML（`CustomerMapper.xml`）がペアになっています。

---

## やること（あなたの作業はここだけ）

### ステップ1: 接続パスワードを自分のものに書き換える

`solutions/mybatis-sample/src/main/resources/mybatis-config.xml` を開き、`<property name="password" value="postgres"/>` の `postgres` の部分を、**自分が週0で設定したPostgreSQLのパスワード**に書き換えてください（ユーザー名 `postgres`・DB名 `training` はそのままでOK）。

### ステップ2: `CustomerMapper.java` の1メソッドを完成させる

`CustomerMapper.java` を開くと、次のような **未完成のメソッド** があります（`// TODO` の部分があなたの作業）。

```java
public interface CustomerMapper {

    // 指定したidの顧客を1件取得して Customer オブジェクトで返す。
    // 該当が無ければ null が返る。
    // TODO: このメソッドに、XMLのSQL(id="findById")を呼び出すための
    //       アノテーションは不要。メソッド名とXMLのidを一致させることで紐づく。
    //       やること: 引数と戻り値の型を正しく宣言してメソッドを完成させる。
    Customer findById(int id);   // ← この1行が正しく宣言できていればOK（穴埋めの主眼）
}
```

実際の穴埋めの中身は、配布した `CustomerMapper.java` の中の `// TODO` コメントの指示に従ってください。**やることは「`findById` メソッドを正しい引数・戻り値で宣言する」だけ**です（SQL本体はXMLに書いてあります）。

> なぜメソッドを書くだけで動くのか: MyBatisは「インターフェースのメソッド名」と「XMLの `<select id="...">` のid」を**名前で結びつけ**ます。`findById` というメソッドを呼ぶと、XMLの `id="findById"` のSQLが実行され、結果が `Customer` に詰められて返ってきます。

### ステップ3: 実行する

WSL2のターミナル（Ubuntu）で `solutions/mybatis-sample/` フォルダに移動し、次を実行します。

```bash
./gradlew run
```

初回はライブラリのダウンロードで少し時間がかかります。

---

## 完成条件（自分で正解か判定する基準）

`./gradlew run` を実行すると、**顧客id=1（佐藤 太郎）の情報**がコンソールに表示されること。

`App.java` は「id=1 の顧客を取得して表示する」よう作ってあります。あなたの `findById` が正しく書けていれば、次が表示されます。

---

## 検証方法

`./gradlew run` の出力の末尾に、次のような行が出れば成功です。

**期待される出力**:

```
取得した顧客: Customer{id=1, name='佐藤 太郎', email='sato@example.com', prefecture='東京都'}
```

> 数字や文字が `schema.sql` の顧客1（佐藤 太郎 / sato@example.com / 東京都）と一致していればOKです。`BUILD SUCCESSFUL` も併せて表示されます。

### うまくいかないときの切り分け（順に確認）

1. **DBに接続できない系のエラー**（`Connection refused` / `password authentication failed`）
   → ステップ1のパスワード書き換えを確認。PostgreSQLが起動しているかも確認（週0参照）。
2. **`schema.sql` 未投入**（`relation "customers" does not exist`）
   → `psql -h localhost -U postgres -d training -f schema.sql` を実行。
3. **`findById` 未完成**（コンパイルエラー / 結果がnull）
   → `CustomerMapper.java` のメソッド宣言を見直す（戻り値 `Customer`、引数 `int id`）。

---

## つまずきポイントとヒント

<details>
<summary>「Javaのインターフェースにメソッドを宣言する」って？</summary>

インターフェースの中では、メソッドは「中身（処理）」を書かず、「こういう名前・引数・戻り値のメソッドがある」という**宣言だけ**を書きます（末尾は `{}` ではなく `;`）。今回の `findById` も、中身はMyBatisがXMLのSQLから自動で用意してくれるので、あなたは宣言だけ書けばよいのです。
</details>

<details>
<summary>メソッド名とXMLのidが一致していないと動かない</summary>

`CustomerMapper.xml` の `<select id="findById" ...>` と、`CustomerMapper.java` の `findById` は**同じ名前**でなければなりません。タイプミスがあると「対応するSQLが見つからない」エラーになります。
</details>

<details>
<summary>結果のカラムとJavaのフィールドの対応</summary>

`customers` 表の `registered_at` のように、JavaのCustomerクラスに対応フィールドが無い列は、今回は単に使われないだけです（エラーにはなりません）。Customerクラスが持つ `id` / `name` / `email` / `prefecture` に、同名の列が自動で詰められます。
</details>

<details>
<summary>Gradle が何かをダウンロードして止まったように見える</summary>

初回は依存ライブラリ（MyBatisやJDBCドライバ）のダウンロードで時間がかかります。ネットにつながっていれば待てば進みます。会社のネットワークでプロキシが必要な場合は講師に相談してください。
</details>

<details>
<summary>もっと理解したい: ファイルどうしの関係</summary>

`App.java`（main）→ `MyBatisSessionFactory`（設定を読み込んで「DBとの対話窓口」を作る）→ `CustomerMapper`（あなたが宣言したメソッド）→ `CustomerMapper.xml`（実際のSQL）→ PostgreSQL、という流れでデータが取れます。`mybatis-config.xml` は接続先（DB名・ユーザー・パスワード）とマッパーの登録場所を教えています。週3のSpring Bootでは、この初期化の多くを自動でやってくれるようになります。
</details>

---

## 模範解答への導線

`./gradlew run` で佐藤 太郎が表示できたら完了です。`solutions/mybatis-sample/` の各ファイルが完成形なので、`CustomerMapper.java` の `// TODO` を**自分で埋めた版**と、解説 [../solutions/11-mybatis.md](../solutions/11-mybatis.md) を見比べて、各ファイルの役割を説明できるようにしておきましょう（週3の土台になります）。

> 🎉 ここまで来たら週2は完了です。「表とほしいデータからSQLを書け、Javaから実行できる」——到達目標に到達しました。お疲れさまでした。
