# 課題02: 商品一覧をカテゴリで絞り込む `GET /api/products?categoryId=...`

**目安時間: 70分**

> AI利用区分: ✅ AIにコードを書かせてよい。ただし**生成コードを自分の言葉で説明でき、なぜその実装かを言えること**が必須。下の「AI生成コードの説明記入欄」を必ず埋めること。

---

## 0. AI生成コードの説明記入欄（AIに書かせた場合は必ず埋める）

**(1) このコードは何をしているか（自分の言葉で）:**
```
（例: クエリパラメータ categoryId を受け取り、指定があればそのカテゴリの商品一覧、無ければ全件を返す）
（ここに記入）
```

**(2) なぜこの実装にしたか（categoryId が省略されたときに全件にした理由、リストで返す理由 等）:**
```
（ここに記入）
```

---

## 1. 背景・狙い（現場でどう使うか）

一覧画面では「条件で絞り込む」のがふつうです。「このカテゴリの商品だけ見たい」「この日付以降の注文だけ」など。
この絞り込み条件は、URLの後ろに `?categoryId=2` のようにくっつけて渡します。これを**クエリパラメータ**と呼びます。
この課題では、課題1の「1件取得」を「複数件（一覧）取得＋絞り込み」に広げます。
**リストで返す**ことと、**条件の有無で動きを変える**ことが新しいポイントです。

> 用語メモ: **クエリパラメータ** … URLの `?` 以降に `キー=値` の形で付ける、絞り込みや検索の条件。
> 例: `/api/products?categoryId=2` の `categoryId=2` の部分。複数なら `&` でつなぐ（`?a=1&b=2`）。

---

## 2. 詳細設計（この通りに作る）

### エンドポイント
| 項目 | 内容 |
|---|---|
| メソッド | `GET` |
| パス | `/api/products` |
| クエリパラメータ | `categoryId`（任意。整数。**省略可**） |
| 対象テーブル | `products` |

### リクエスト
- `categoryId` を指定: そのカテゴリの商品だけを返す。例: `/api/products?categoryId=2`
- `categoryId` を省略: **全商品**を返す。例: `/api/products`
- リクエストボディ: なし

### レスポンス（成功時）
- ステータス: `200 OK`
- ボディ（JSON）: 商品オブジェクトの**配列（リスト）**。各要素は課題1と同じ `id`, `name`, `price`。
- 並び順: `id` の昇順（小さい順）。

`/api/products?categoryId=2` の例:
```json
[
  {"id":3,"name":"ワイヤレスイヤホン","price":8900},
  {"id":4,"name":"USB充電器","price":1500}
]
```

該当が0件のとき（例: 存在しないカテゴリ）:
```json
[]
```
> 0件でも「空の配列 `[]` ＋ 200」を返します。エラーにはしません（「条件に合うものが無い」のは正常な結果だから）。

---

## 3. 作るもの

課題1で作った product のファイル群に、**一覧取得用のメソッドを足していく**のが中心です。

| 触るファイル | 何をする |
|---|---|
| `ProductMapper.java` | `findByCategory` のようなメソッドを追加（戻り値は `List<Product>`） |
| `resources/mapper/ProductMapper.xml` | 一覧取得のSELECTを追加（categoryIdの有無で条件を切り替える） |
| `ProductService.java` | 一覧取得メソッドを追加し、`List<ProductResponse>` に詰め替える |
| `ProductController.java` | `GET /api/products` を追加し、`@RequestParam` で categoryId を受ける |

---

## 4. 完成条件（自己判定基準）

- `categoryId` 指定あり: そのカテゴリの商品だけが id 昇順で返る。
- `categoryId` 省略: 全商品が id 昇順で返る。
- 該当0件: `[]` が返る（200）。

---

## 5. 検証方法（自分で正解判定する）

> コードを変えたら**アプリを起動し直す**こと。データは週READMEのサンプル（書籍/家電/食品の3カテゴリ）前提。

```bash
# カテゴリ2（家電）で絞り込み
curl http://localhost:8080/api/products?categoryId=2
```
**期待レスポンス**:
```json
[{"id":3,"name":"ワイヤレスイヤホン","price":8900},{"id":4,"name":"USB充電器","price":1500}]
```

```bash
# カテゴリ省略 → 全件（id昇順）
curl http://localhost:8080/api/products
```
**期待レスポンス**:
```json
[{"id":1,"name":"Java入門書","price":2800},{"id":2,"name":"SQL実践ガイド","price":3200},{"id":3,"name":"ワイヤレスイヤホン","price":8900},{"id":4,"name":"USB充電器","price":1500},{"id":5,"name":"有機コーヒー豆","price":1200}]
```

```bash
# 存在しないカテゴリ → 空配列
curl http://localhost:8080/api/products?categoryId=999
```
**期待レスポンス**:
```json
[]
```

> 補足: ターミナルによっては `?` や `&` を特別扱いする場合があります。その場合はURL全体を `"` で囲んでください。
> ```bash
> curl "http://localhost:8080/api/products?categoryId=2"
> ```

---

## 6. つまずきポイントとヒント

<details>
<summary>ヒント1: クエリパラメータの受け取り方（@RequestParam）</summary>

- パスの一部（`{id}`）は `@PathVariable` でしたが、`?categoryId=...` は **`@RequestParam`** で受け取ります。
- 省略可能にするには `required = false` を付けます。省略時は `null` が入ります。
  ```java
  @GetMapping
  public List<ProductResponse> listProducts(
          @RequestParam(name = "categoryId", required = false) Long categoryId) {
      ...
  }
  ```
</details>

<details>
<summary>ヒント2: categoryId の有無で動きを変える（2つのやり方）</summary>

やり方は2つあります。どちらでもOKですが、**やり方A**の方が初心者にはおすすめです。

- **やり方A（Serviceで分岐）**: Service の中で「categoryId が null なら全件用のメソッド、そうでなければ絞り込み用のメソッド」と呼び分ける。Mapperには `findAll()` と `findByCategory(categoryId)` の2つを用意する。**分かりやすい**。
- **やり方B（XMLで動的SQL）**: Mapperは1つにして、XMLの `<if test="categoryId != null">` で WHERE 句を付けたり外したりする（MyBatisの「動的SQL」）。**コードは1本で済むが、XMLが少し難しい**。

まずはAで動かし、余裕があればBに挑戦すると理解が深まります。
</details>

<details>
<summary>ヒント3: リストを返すときのMapperとService</summary>

- Mapper: 戻り値を `List<Product>` にするだけ。MyBatisが複数行を自動でリストに詰めてくれます。
  ```java
  List<Product> findByCategory(@Param("categoryId") Long categoryId);
  List<Product> findAll();
  ```
- Service: 取れた `List<Product>` を、1件ずつ `ProductResponse` に詰め替えて `List<ProductResponse>` にします。
  Java（週1のStream）で書くと短いです。
  ```java
  return products.stream().map(ProductResponse::from).toList();
  ```
  Streamが不安なら、for文で1件ずつ新しいリストに add してもまったく同じ結果です。
</details>

<details>
<summary>ヒント4: 並び順（id昇順）</summary>

SQLの最後に `ORDER BY product_id` を付けると、id の小さい順に並びます。これを忘れると順番がバラバラになることがあります。
</details>

<details>
<summary>ヒント5: 0件のときに [] が返るか不安</summary>

該当行が無ければ、MyBatisは「空のリスト」を返します（null ではなく `[]`）。
なので特別な処理を書かなくても、自然と `[]`（200）になります。
</details>

---

## 7. 模範解答への導線

自力で完成させてから、[solutions/02-list-products/](../solutions/02-list-products/) を見て比べてください。
解答にはやり方A（分岐）とやり方B（動的SQL）の両方を載せています。
