# 模範: 課題02 読解ノート（記入例）

> これは**記入の見本**です。行番号は「この教材を書いた時点」のものです。題材リポが更新されると数行ズレることがあります。
> **大事なのは行番号の丸暗記ではなく、自分でファイルを開いて確かめたこと**。ズレていたら、それはあなたが本当に開いた証拠です。

---

【GET /api/books/{id} の処理フロー（ファイル名:行 で書く）】
 1. 窓口   : BookController.java:43  の `get(@PathVariable Long id)` が受ける（`@GetMapping("/{id}")` は42行目）
 2. 判断   : BookService.java:35    の `findById(Long id)` を呼ぶ
 3. DB出入 : BookMapper.java:25     の `findById(@Param("id") Long id)` を呼ぶ
 4. SQL    : BookMapper.xml:40      の `<select id="findById">` が実行される
 5. 戻り   : 結果の Book を BookResponse（book/BookResponse.java）に詰め替えて返す（Controller 44行目 `new BookResponse(...)`）

【存在しないIDのとき、404はどこで決まるか（ファイル名:行）】
 - 例外を投げる場所   : BookService.java:38 `throw new NotFoundException("書籍", id);`（findById で book が null のとき）
 - 例外を404に変える場所: GlobalExceptionHandler.java:30-31 の `@ExceptionHandler(NotFoundException.class)` が受けて 404 を返す

【AIが挙げた行番号と、自分で開いて確かめた行番号で、ズレはあったか】
 - AIは「BookController の30行目あたりで受ける」と言ったが、実際に開くと `get` は43行目だった（AIは `list` と混同していた気配）。行番号は自分で数えて正した。SQLが `BookMapper.xml` にあること自体は合っていた。

---

## この記入例で見てほしいポイント

- Controller→Service→Mapper→XML を、**それぞれ実ファイル・実行**で押さえている。
- 戻り値が **`BookResponse` に詰め替えられて返る**ことまで追えている（エンティティをそのまま返していない、という3層の作法に気づいている）。
- 404の流れを「**投げる場所**」と「**404に変える場所**」の2箇所に分けて押さえている。例外処理は「投げる側」と「受ける側」がペアだと分かっている。
- AIの行番号のズレを、自分で数えて正している（＝本当に開いた）。
