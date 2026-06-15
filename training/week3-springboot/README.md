# 週3: Spring BootでREST APIを作る

この週は、**Webの裏側（API）を1本ずつ自分で作れるようになる**ための週です。
これまでの週で学んだ Java（週1）と SQL / MyBatis（週2）が、ここで1つにつながります。
全体の進め方・AI利用ルール・質問テンプレート・日報フォーマットは、先に [研修トップのREADME](../README.md) を必ず読んでください。

> 用語メモ: **REST API（レストエーピーアイ）** … プログラム同士がインターネット越しにデータをやり取りする「窓口」。
> たとえばスマホアプリが「顧客の情報をください」とサーバーに頼むとき、この窓口を通します。
> 用語メモ: **エンドポイント** … APIの個々の窓口（URL）。「GET /api/customers/1」のように「メソッド＋パス」で1つ。

---

## 1. この週の狙い（到達目標）

> **詳細設計を渡されたら、APIを1本実装できる。**

現場であなたが最初に任されるのは、「この窓口を、こういう入力で、こういう出力になるように作って」という、
1本ずつのAPI実装です。この週はそれを、**実際にビルドが通る雛形プロジェクト**を土台に、
指定のエンドポイントを1本ずつ追加していく形で練習します。

この週で学ぶ範囲:

- **3層構造（さんそうこうぞう）**: 役割を Controller / Service / Repository(Mapper) の3つに分ける作り方
- **DI（ディーアイ / 依存性の注入）**: 必要な部品を外から差し込んでもらう仕組み
- **リクエストとレスポンス**: 外から来るデータと、外へ返すデータ。受け渡し用の入れ物 **DTO**
- **Bean Validation（ビーンバリデーション / 入力チェック）**: 「名前が空ならエラー」などの入力検査
- **例外ハンドリング**: エラーが起きたときに、きちんとした形（404など）で返す仕組み
- **MyBatis連携**: 週2で学んだMyBatisを、Spring Bootの中で使う

> 用語メモ: **3層構造** … 「窓口（Controller）」「判断・処理（Service）」「DB出し入れ（Repository = MyBatisのMapper）」の3つに役割を分ける作り方。
> 1つのファイルに全部書くと、後で直すときにどこを触ればいいか分からなくなる。役割を分けておくと、変更が一箇所で済む。

---

## 2. AI利用区分（この週のルール）

✅ **週3は、AIにコードを書かせてよいです。** これは現場でのAI活用の練習も兼ねています。

ただし、次の2つは**必須**です。守れないなら、たとえ動いていても「やり直し」です。

- ✅ 生成されたコードを **自分の言葉で説明できること**
- ✅ **なぜその実装なのか** を言えること

各課題ファイルに **「AI生成コードの説明記入欄」** を設けています。AIに書かせた場合は必ず埋めてください。
中身を理解せずに貼るだけの人は、現場で信用されません。逆に「AIに書かせて、理解して、説明できる」人は強いです。

理由の詳細は [研修トップのREADME「2. AI利用ルール」](../README.md#2-ai利用ルール最重要必ず守る) を参照。

---

## 3. 雛形プロジェクト（starter-project）の動かし方

この週は、まず `starter-project/` を自分のPCで動かすところから始めます。
ここには **すでにビルドが通る最小限のアプリ** と、**お手本になる完成済みAPIが1本**入っています。

### 手順1: 練習用DBにテーブルとデータを入れる

このアプリは、週0で作った練習用DB `training` の中にテーブルがある前提で動きます。

> WSL2は再起動するとPostgreSQLが止まります。DBを使う前に `sudo service postgresql start` を実行してください（詳しくは週0参照）。これをしないと `Connection refused` で接続できません。

- **週2の `schema.sql` を持っている人**: それを `training` DB に流してください。
  （週2のスキーマには customers / products などのテーブルがあります）
  ```bash
  psql -h localhost -U postgres -d training -f （週2のschema.sqlのパス）
  ```
- **まだ週2のスキーマが手元に無い人**: 雛形に**フォールバック用のSQL**を用意しています。
  これを流せば、この週の全課題に必要なテーブル（customers / categories / products）とサンプルデータが入ります。
  ```bash
  psql -h localhost -U postgres -d training -f starter-project/src/main/resources/sample-schema.sql
  ```
  > パスワードを聞かれたら、週0で自分が設定したPostgreSQLのパスワードを入力します。

> 用語メモ: **スキーマ** … テーブルの設計（どんな列があるか）の定義。`schema.sql` はそれを作るSQLを集めたファイル。

### 手順2: DBパスワードを自分のものに直す

`starter-project/src/main/resources/application.yml` を開き、`password:` の値を、
**週0で自分が設定したPostgreSQLのパスワード**に書き換えてください。
（週0で分かりやすく `postgres` にした人は、そのままでOKです）

### 手順3: アプリを起動する

`starter-project/` フォルダでWSL2のターミナル（Ubuntu）を開き、次を実行します。

```bash
./gradlew bootRun
```

> 用語メモ: **bootRun** … Spring Bootアプリを起動するコマンド。`gradlew`（グレードルラッパー）は、
> Gradle（ビルド道具）を自動で用意して動かしてくれる仕組みなので、Gradleを別途インストールする必要はありません。
> 初回はGradle本体のダウンロードが走るので、少し時間がかかります（ネット接続が必要）。

次のような行が出れば起動成功です（ポート8080で待ち受けています）。

```
Tomcat started on port 8080 (http) with context path '/'
Started TrainingApiApplication in 1.x seconds ...
```

### 手順4: お手本APIの動作確認

アプリを起動したまま、**別の**WSL2のターミナル（Ubuntu）を開いて次を実行します。

```bash
curl http://localhost:8080/api/customers/1
```

次のようなJSONが返れば成功です（お手本どおりに3層が動いている証拠）。

```json
{"id":1,"name":"山田 太郎","email":"taro.yamada@example.com"}
```

> 用語メモ: **curl（カール）** … ターミナルからAPIにアクセスして、返事を見るための道具。WSL2のUbuntuには標準で入っていることが多いです。
> 入っていない場合は `sudo apt install curl` で入れるか、ブラウザで `http://localhost:8080/api/customers/1` を開いても確認できます。

アプリを止めるときは、起動しているターミナルで **Ctrl + C** を押します。

> つまずいたら: 起動や接続でエラーが出たら、下の「困ったときに見る場所」と、各課題の「つまずきポイント」を見てください。

---

## 4. 雛形のソースを読む（写経のすすめ）

本課題に入る前に、**お手本の1本（`GET /api/customers/{id}`）のソースを一通り読んで**ください。
この1本に、この週で使う3層構造のすべてが詰まっています。`starter-project/src/main/java/com/example/training/customer/` の中です。

| ファイル | 層 | 役割 |
|---|---|---|
| `CustomerController.java` | Controller（窓口） | URLを受け取り、Serviceに渡し、結果を返す |
| `CustomerService.java` | Service（判断・処理） | Mapperを使ってデータを取り、DTOに詰め替える |
| `CustomerMapper.java` | Repository（DBアクセス） | SQLを呼ぶメソッドの「形」だけを書いたインターフェース |
| `CustomerMapper.xml`（resources/mapper） | 〃 | 実際のSQLを書く場所。メソッド名とidを対応づける |
| `Customer.java` | （データの入れ物） | テーブル1行ぶんを表すエンティティ |
| `CustomerResponse.java` | （データの入れ物） | 外に返す形を表すDTO |

> おすすめ: 一度、この6ファイルを見ながら**自分の手で写経（書き写し）**してみると、どこに何を書くかが体に入ります。
> 本課題は、このお手本を「真似して別のテーブル用に作る」ことから始まります。

---

## 5. 課題一覧

上から順に解いてください。だんだん難しくなる順番に並んでいます。**1課題 = 1エンドポイント**です。

| # | 課題 | 学ぶこと | 目安時間 | 課題ファイル | 模範解答 |
|---|---|---|---|---|---|
| 01 | 商品1件取得 `GET /api/products/{id}` | お手本を真似て3層を作る | 60分 | [01-get-product.md](exercises/01-get-product.md) | [solutions/01-get-product/](solutions/01-get-product/) |
| 02 | 商品一覧（カテゴリ絞り込み）`GET /api/products?categoryId=...` | クエリパラメータ・一覧取得 | 70分 | [02-list-products.md](exercises/02-list-products.md) | [solutions/02-list-products/](solutions/02-list-products/) |
| 03 | 顧客登録 `POST /api/customers` | リクエストボディ・入力チェック | 80分 | [03-create-customer.md](exercises/03-create-customer.md) | [solutions/03-create-customer/](solutions/03-create-customer/) |
| 04 | 例外ハンドリングで404 | 共通例外処理・エラー整形 | 80分 | [04-error-handling.md](exercises/04-error-handling.md) | [solutions/04-error-handling/](solutions/04-error-handling/) |

> 目安時間はあくまで目安です。**理解を優先**してください。各課題は前の課題の上に積み上がります（課題4は課題1で作ったものを直します）。

---

## 6. 進め方（1課題のサイクル）

1. `exercises/NN-xxx.md` を開いて、**詳細設計（仕様）を読む**。エンドポイント・入力・出力・異常時を確認する。
2. お手本（customers）を参考に、**3層を実装する**（AIに書かせてもよいが、必ず理解する）。
3. AIに書かせた場合は、課題の **「AI生成コードの説明記入欄」を埋める**。
4. アプリを起動し直し、課題の **「検証方法」のcurlコマンドを叩いて、自分で正解か判定する**。
5. 設計どおりのレスポンス／ステータスが返れば完成。
6. 詰まったら「つまずきポイントとヒント」（折りたたみ）を開く。
7. それでもダメなら、模範解答 `solutions/NN-xxx/` を見て、自分のコードと比べる。

> コードを変えたら、**アプリを起動し直す**のを忘れずに（Ctrl+Cで止めて、もう一度 `bootRun`）。
> 変更が反映されないときは、たいてい起動し直し忘れです。

---

## 7. もっとやりたい人へ（追加課題）

基本の4本が終わって時間が余ったら挑戦してください（提出不要）。

- 課題1の商品取得も、存在しないIDなら404を返すように、課題4の仕組みを使って直してみる。
- `GET /api/customers`（顧客一覧）を、課題2を参考に自分で設計から作ってみる。
- 商品登録 `POST /api/products` を、課題3を参考に入力チェック付きで作ってみる。

---

## 8. 困ったときに見る場所

| 困りごと | 見る場所 |
|---|---|
| 進め方が分からない | [研修トップのREADME](../README.md) |
| DB接続でつまずいた | [week0-setup/SETUP.md](../week0-setup/SETUP.md) ／ このファイルの「3. 雛形の動かし方」 |
| 3層のどこに何を書くか分からない | このファイルの「4. 雛形のソースを読む」＋各課題の「つまずきポイント」 |
| AIを使っていいか分からない | このファイルの「2. AI利用区分」 |
| 質問の仕方が分からない | [研修トップのREADME「3. 質問テンプレート」](../README.md#3-質問テンプレート詰まったときの聞き方) |

それでは、まず `starter-project/` を動かしてから、[課題01: 商品1件取得](exercises/01-get-product.md) に進んでください。
