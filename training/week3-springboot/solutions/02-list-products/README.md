# 模範解答 02: 商品一覧（カテゴリ絞り込み）`GET /api/products?categoryId=...`

これは課題1の product 一式に、一覧取得を**追加**した版です。課題1から差し替える形で置きます。

## 置き場所

| このフォルダのファイル | 雛形プロジェクト内の置き場所 |
|---|---|
| `product/Product.java` | `src/main/java/com/example/training/product/Product.java`（課題1から変更なし） |
| `product/ProductResponse.java` | `.../product/ProductResponse.java`（課題1から変更なし） |
| `product/ProductMapper.java` | `.../product/ProductMapper.java`（findAll / findByCategory を追加） |
| `product/ProductService.java` | `.../product/ProductService.java`（list を追加） |
| `product/ProductController.java` | `.../product/ProductController.java`（一覧用エンドポイントを追加） |
| `mapper/ProductMapper.xml` | `src/main/resources/mapper/ProductMapper.xml`（findAll / findByCategory を追加） |

## やり方A（この解答の本体）: Mapperを2メソッドに分ける

`ProductService.list()` の中で、`categoryId == null` なら `findAll()`、そうでなければ `findByCategory()` を呼ぶ。
**分かりやすさ重視**。初心者にはこちらを推奨。

ポイント:
- `@RequestParam(required = false)` … クエリパラメータを任意にする。省略時は `null`。
- `List<Product>` で受けると、MyBatisが複数行を自動でリストにしてくれる。0件なら空リスト（`[]`）。
- `stream().map(ProductResponse::from).toList()` で、List<Product> を List<ProductResponse> に詰め替える。
  （週1で学んだStream。for文で1件ずつ add しても同じ結果。）
- `ORDER BY product_id` で id 昇順を保証する。

## やり方B（発展）: 動的SQLで1メソッドにまとめる

Mapperを1つ（`findProducts`）にして、XMLの `<if>` で WHERE 句を付け外しする方法。コードは1本で済む。

`ProductMapper.java`:
```java
List<Product> findProducts(@Param("categoryId") Long categoryId);
```

`ProductMapper.xml`:
```xml
<select id="findProducts" resultType="com.example.training.product.Product">
    SELECT product_id, name, price, category_id
      FROM products
    <where>
        <if test="categoryId != null">
            category_id = #{categoryId}
        </if>
    </where>
    ORDER BY product_id
</select>
```
- `<where>` … 中の条件が1つでもあれば `WHERE` を付け、無ければ付けない。先頭の余分な `AND` も自動で消す。
- `<if test="categoryId != null">` … categoryId が渡されたときだけ条件を追加。
- Service は分岐不要になる:
  ```java
  public List<ProductResponse> list(Long categoryId) {
      return productMapper.findProducts(categoryId).stream()
              .map(ProductResponse::from).toList();
  }
  ```

**どちらを選ぶ？**: 条件が1つなら可読性でA、条件が増えていく見込みならB（動的SQL）が有利。
現場では条件が増えがちなのでBもよく使う。まずAで動かし、Bを写経して違いを体で覚えるとよい。

## セルフチェック

- `@PathVariable` と `@RequestParam` の違いを説明できるか？
- 該当0件のとき、なぜ特別な処理を書かなくても `[]` が返るのか説明できるか？
- やり方A と B のメリット・デメリットを言えるか？
