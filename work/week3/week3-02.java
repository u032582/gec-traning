//ProductService.java
//商品一覧を取得するメソッド
public List<ProductResponse> list(Long categoryId) {
//categoryIdがなければ全件取得、あればカテゴリで絞って取得
    List<Product> products = (categoryId == null)
            ? productMapper.findAll()
            : productMapper.findByCategory(categoryId);

//画面に返すためのProductResponseを入れるリストを作成
    List<ProductResponse> result = new ArrayList<>();
// 取得した商品を1件ずつ処理
    for (Product product : products) {
// ProductをProductResponseに変換してリストに追加
        result.add(ProductResponse.from(product));
    }
// 詰め替えた商品一覧をControllerへ返す
    return result;
}