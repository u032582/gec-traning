# 課題11 模範解答・解説: MyBatisでJavaからDBを叩く

※ 先に自分で `findById` を書き、`./gradlew run` で動かしてから読むこと。

完成プロジェクトは [mybatis-sample/](mybatis-sample/) にある（それ自体が模範解答）。ここでは「あなたが書く1行」と「各ファイルの役割」を解説する。

---

## あなたが書く1行（穴埋めの答え）

`CustomerMapper.java` の中身は、この1行のメソッド宣言。

```java
Customer findById(int id);
```

- 戻り値 `Customer`: 顧客1件を返すため。
- メソッド名 `findById`: `CustomerMapper.xml` の `<select id="findById">` と**同じ名前**にすることでSQLと結びつく。
- 引数 `int id`: 取得したい顧客のid。XMLの `#{id}` にこの値が入る。
- 末尾は `;`（インターフェースなので中身 `{}` は書かない）。

これだけで、`mapper.findById(1)` を呼ぶと `SELECT ... WHERE id = 1` が実行され、結果が `Customer` に詰まって返ってくる。

---

## 接続設定（ステップ1の答え）

`src/main/resources/mybatis-config.xml` の password を自分のPostgreSQLパスワードに書き換える。

```xml
<property name="password" value="（自分のパスワード）"/>
```

ユーザー名 `postgres`・DB名 `training`・ポート `5432` は `schema.sql` / 週0の設定と一致しているのでそのままでよい。

---

## 各ファイルの役割（説明できるようにする）

| ファイル | 役割 |
|---|---|
| `build.gradle` | 依存ライブラリ（MyBatis 3.5.16 / PostgreSQL JDBC 42.7.4）とJDK21・mainクラスを宣言。`./gradlew run` の動作はここで決まる。 |
| `App.java` | main。SqlSession を開き、Mapper を取り出し、`findById(1)` を呼んで結果を表示。 |
| `Customer.java` | DBの1行を入れるドメインクラス。列名と同名フィールドにMyBatisが自動で値を詰める。 |
| `CustomerMapper.java` | 「メソッド名 ↔ SQL」を結ぶインターフェース。あなたが `findById` を宣言した。 |
| `CustomerMapper.xml` | 実際のSQL。`namespace` がインターフェース完全名、`<select id>` がメソッド名と一致。 |
| `MyBatisSessionFactory.java` | `mybatis-config.xml` を読み込んで SqlSessionFactory（窓口工場）を構築。 |
| `mybatis-config.xml` | DB接続情報とマッパー登録。 |

### データが流れる順番

```
App.main
  └ MyBatisSessionFactory.getSqlSessionFactory()   … mybatis-config.xml を読み込む
       └ session.getMapper(CustomerMapper.class)    … マッパーを取得
            └ mapper.findById(1)                    … メソッド名でXMLのSQLを特定
                 └ CustomerMapper.xml の SELECT     … #{id} に 1 が入って実行
                      └ PostgreSQL (training.customers)
                 ← 結果1行を Customer に詰めて返す
  ← Customer{id=1, name='佐藤 太郎', ...} を表示
```

---

## 期待される出力

```
取得した顧客: Customer{id=1, name='佐藤 太郎', email='sato@example.com', prefecture='東京都'}
```

---

## 発展（もっとやりたい人へ）

- `App.java` の `targetId` を 6 に変えると伊藤さくらが取れる。存在しないid（例: 999）にすると `null` が返り「見つかりませんでした」が出る — null の扱いも確認できる。
- 全件取得に拡張: `CustomerMapper.xml` に `<select id="findAll" resultType="customer">SELECT id, name, email, prefecture FROM customers</select>` を足し、インターフェースに `List<Customer> findAll();`（`import java.util.List;` が必要）を宣言、App から呼んでループ表示する。
- 週3では、この初期化（SqlSessionFactory構築・設定読み込み）の多くをSpring Bootが自動でやってくれる。今回手で組んだ構造を理解しておくと、週3で「裏で何が起きているか」が見える。
