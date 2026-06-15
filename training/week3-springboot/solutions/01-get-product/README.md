# 模範解答 01: 商品1件取得 `GET /api/products/{id}`

## 置き場所（雛形プロジェクトのどこに置くか）

| このフォルダのファイル | 雛形プロジェクト内の置き場所 |
|---|---|
| `product/Product.java` | `src/main/java/com/example/training/product/Product.java` |
| `product/ProductResponse.java` | `src/main/java/com/example/training/product/ProductResponse.java` |
| `product/ProductMapper.java` | `src/main/java/com/example/training/product/ProductMapper.java` |
| `product/ProductService.java` | `src/main/java/com/example/training/product/ProductService.java` |
| `product/ProductController.java` | `src/main/java/com/example/training/product/ProductController.java` |
| `mapper/ProductMapper.xml` | `src/main/resources/mapper/ProductMapper.xml` |

`com.example.training.product` という新しいパッケージ（フォルダ）を作って、そこに5つの `.java` を置きます。
XMLは customers のお手本と同じ `resources/mapper/` に置きます。
メインクラスの `@MapperScan("com.example.training")` が効いているので、`product` パッケージのMapperも自動で読み込まれます。

## 要点の解説

- **お手本（customer）とほぼ同じ形**になっている。テーブル名・列名・クラス名を product 用に置き換えただけ。
  「3層構造は、対象が変わっても同じ型（かた）で書ける」ことを体感してほしい。
- **DTO（ProductResponse）で id / name / price だけを返す**。`categoryId` はエンティティ（Product）には持つが、
  外には出していない。「DBの形」と「外に見せる形」を分けることで、片方を変えても他方に影響しにくくしている。
- **列名→項目名の自動対応**: `product_id` → `productId`、`category_id` → `categoryId` は
  `application.yml` の `map-underscore-to-camel-case: true` のおかげ。手で対応を書く必要はない。
- **Service が null を返す**のは、この課題の範囲（存在しないIDは200＋空で可）に合わせたもの。
  「無ければ404」にする整った対応は課題4で追加する。

## 「自分の言葉で説明できるか」セルフチェック

- なぜ Controller / Service / Mapper の3つに分けるのか、を説明できるか？
- DTO（ProductResponse）をわざわざ作る理由を説明できるか？
- `@PathVariable` は何をしているか、説明できるか？
- Mapperインターフェースの `findById` と XMLの `id="findById"` がどう結びつくか、説明できるか？
