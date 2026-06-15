# 課題01: 商品を1件取得する `GET /api/products/{id}`

**目安時間: 60分**

> AI利用区分: ✅ AIにコードを書かせてよい。ただし**生成コードを自分の言葉で説明でき、なぜその実装かを言えること**が必須。下の「AI生成コードの説明記入欄」を必ず埋めること。

---

## 0. AI生成コードの説明記入欄（AIに書かせた場合は必ず埋める）

> AIを使わず自力で書いた場合は「自力で実装」と書けばOKです。

**(1) このコードは何をしているか（自分の言葉で）:**
```
（例: URLの {id} を受け取り、Serviceに渡す。ServiceがMapper経由でDBから商品を1件取り、DTOに詰めて返す）
（ここに記入）
```

**(2) なぜこの実装にしたか（なぜ3層に分けるか、なぜDTOに詰め替えるか等）:**
```
（ここに記入）
```

---

## 1. 背景・狙い（現場でどう使うか）

「IDを1つ渡すと、その1件の情報をJSONで返す」——これはAPIの中で**最も基本かつ最も多い**形です。
商品ページ、ユーザー詳細、注文確認……現場のАPIの多くがこの「1件取得」をベースにしています。
この課題では、雛形のお手本（顧客取得）を**真似して**、商品取得を3層構造で作ります。
まずは「お手本どおりに別テーブル用を作る」ことで、3層の手の動かし方を体に入れるのが狙いです。

---

## 2. 詳細設計（この通りに作る）

### エンドポイント
| 項目 | 内容 |
|---|---|
| メソッド | `GET` |
| パス | `/api/products/{id}` |
| 対象テーブル | `products`（列: `product_id`, `name`, `price`, `category_id`） |

### リクエスト
- パスパラメータ `id`: 取得したい商品のID（整数）。例: `/api/products/1`
- リクエストボディ: なし

### レスポンス（成功時）
- ステータス: `200 OK`
- ボディ（JSON）: 次の3項目を返す。`category_id` は今回は返さない。

```json
{
  "id": 1,
  "name": "Java入門書",
  "price": 2800
}
```

### 異常時の挙動（この課題の範囲）
- 存在しないIDを指定した場合: 今回は**空（ボディなし）＋200**でも可とします。
  （「存在しないときに404を返す」整った対応は、**課題4**でまとめて作ります。今はまだ気にしなくてOK）

---

## 3. 作るもの（お手本との対応）

お手本の顧客（customer）の6ファイルを参考に、商品（product）版を作ります。
パッケージは `com.example.training.product` を新しく作るのがおすすめです。

| 作るファイル | お手本にあたるファイル | 役割 |
|---|---|---|
| `product/Product.java` | `customer/Customer.java` | products 1行を表すエンティティ |
| `product/ProductResponse.java` | `customer/CustomerResponse.java` | 返すJSONの形（id, name, price） |
| `product/ProductMapper.java` | `customer/CustomerMapper.java` | `findById` メソッドの形 |
| `resources/mapper/ProductMapper.xml` | `resources/mapper/CustomerMapper.xml` | 実際のSELECT文 |
| `product/ProductService.java` | `customer/CustomerService.java` | Mapperを呼びDTOに詰める |
| `product/ProductController.java` | `customer/CustomerController.java` | `GET /api/products/{id}` の窓口 |

---

## 4. 完成条件（自己判定基準）

- アプリを起動し直したあと、下の検証方法のcurlを叩くと、**設計どおりのJSON（200）**が返る。

---

## 5. 検証方法（自分で正解判定する）

> 事前に、週READMEの手順でDB（products テーブル）にデータが入っていること。コードを変えたら**アプリを起動し直す**こと。

```bash
# 商品ID=1 を取得
curl http://localhost:8080/api/products/1
```
**期待レスポンス**:
```json
{"id":1,"name":"Java入門書","price":2800}
```

```bash
# 別のID（家電カテゴリの商品）も試す
curl http://localhost:8080/api/products/3
```
**期待レスポンス**:
```json
{"id":3,"name":"ワイヤレスイヤホン","price":8900}
```

> ステータスコードも確認したい場合は `-i` を付けると、先頭に `HTTP/1.1 200` が表示されます。
> ```bash
> curl -i http://localhost:8080/api/products/1
> ```

---

## 6. つまずきポイントとヒント

<details>
<summary>ヒント1: どこに何を書くか（3層の役割）</summary>

- **Controller**: URL `/api/products/{id}` を受け取り、`@PathVariable` で id を取り出して Service に渡すだけ。判断は書かない。
- **Service**: Mapper を呼んで Product を取り、`ProductResponse` に詰め替えて返す。
- **Mapper（インターフェース）**: `Product findById(@Param("productId") Long productId);` の「形」だけ。
- **Mapper.xml**: `id="findById"` の `<select>` に、`products` から `product_id = #{productId}` で1件取るSQLを書く。

お手本の customer 版とほぼ同じ形になります。テーブル名・列名・クラス名を product 用に置き換えるのが中心です。
</details>

<details>
<summary>ヒント2: MyBatisのMapperインターフェースとXMLの対応づけ</summary>

- XMLの `namespace` を、**Mapperインターフェースの完全なクラス名**にする: `com.example.training.product.ProductMapper`
- `<select>` の `id` を、**インターフェースのメソッド名**と同じ `findById` にする。
- これで「Javaの `findById()` を呼ぶ」と「XMLの `findById` のSQL」が結びつきます。
- `@MapperScan("com.example.training")` がメインクラスに付いているので、`product` パッケージに置けば自動で読み込まれます。
</details>

<details>
<summary>ヒント3: 列名とJavaの項目名の対応（price と category_id）</summary>

- DBの列は `product_id`, `name`, `price`, `category_id`。
- `application.yml` の `map-underscore-to-camel-case: true` により、`product_id` → `productId`、`category_id` → `categoryId` に自動対応します。
- なので `Product.java` のフィールド名は `productId`, `name`, `price`, `categoryId` にしておけば、手で対応を書く必要はありません。
- `price` は数値なので、Javaの型は `Integer` にします。
</details>

<details>
<summary>ヒント4: 返すJSONのキー名（id, name, price）に注意</summary>

- 返すJSONは `{"id":..., "name":..., "price":...}` です。`productId` ではなく `id` で返します。
- これは `ProductResponse`（DTO）の項目名を `id`, `name`, `price` にすることで実現します。
- お手本の `CustomerResponse` が `id`（中身は customerId）を返しているのと同じやり方です。
</details>

<details>
<summary>ヒント5: 変更が反映されない</summary>

コードを変えたら、アプリを**起動し直す**必要があります。起動中のターミナルで Ctrl+C → もう一度 `./gradlew bootRun`。
</details>

---

## 7. 模範解答への導線

自力で完成させてから、[solutions/01-get-product/](../solutions/01-get-product/) を見て、自分のコードと比べてください。
解答フォルダには、追加した6ファイルと、要点の解説（README）が入っています。
**先に答えを見ると力がつきません。** まず自分で（またはAIと一緒に理解しながら）作ってから。
