# 模範: 課題01 読解ノート（記入例）

> これは「こう書けていれば十分」という**記入の見本**です。文言をそのまま写すためのものではありません。
> 大事なのは、**自分でフォルダを開いて確かめた事実**が書けていること。

---

【パッケージ構成（実際に開いて確認した）】
 - book/     : 書籍まわり。Book(エンティティ) / BookResponse(DTO) / BookController / BookService / BookMapper(+resources/mapper/BookMapper.xml)。書籍の一覧・1件取得を担当。
 - member/   : 利用者まわり。Member / MemberMapper(+XML) / MemberService。利用者の取得（実在チェック）を担当。Controllerは無い（外向けAPIを持たないため）。
 - lending/  : 貸出・返却。Lending / LendingRequest / LendingResponse / LendingController / LendingService / LendingMapper(+XML)。**このアプリの核**。在庫の増減を伴う。
 - common/   : 共通部品。GlobalExceptionHandler / NotFoundException / BusinessRuleException。特定エンティティに属さない「例外を適切なHTTPステータスに変換する」仕組み。

【3層構造の地図（このリポではどのファイルがどの層か）】
 - 窓口（Controller）        : BookController.java / LendingController.java（member にはControllerが無い）
 - 判断・処理（Service）      : BookService.java / MemberService.java / LendingService.java
 - DB出し入れ（Mapper+XML）   : BookMapper.java+BookMapper.xml / MemberMapper.java+MemberMapper.xml / LendingMapper.java+LendingMapper.xml
 - データの入れ物（エンティティ/DTO）: Book/Member/Lending（エンティティ）、BookResponse/LendingRequest/LendingResponse（DTO）

【エントリポイント（アプリの入口）はどのファイルか】
 - LibraryApplication.java（`com.example.training` 直下）。`main` メソッドがあり、`@SpringBootApplication` と `@MapperScan("com.example.training")` が付いている。ここを実行するとアプリが起動する。

【AIの要約と、自分で確かめた事実で、食い違いはあったか】
 - AIは「4パッケージともController/Service/Mapperが揃っている」と要約したが、実際に開くと **member にはController が無かった**（外向けのAPIを持たず、lending から呼ばれるだけ）。AIの「揃っている」は思い込み。自分で開いて気づけた。

---

## この記入例で見てほしいポイント

- 各パッケージを**実際に開いて**、ファイルの顔ぶれと役割を確認している（推測で書いていない）。
- **member に Controller が無い**という、AIが見落としがちな非対称に自分で気づいている。これがこの課題の裏取りの成果。
- エントリポイントを、`main` と `@SpringBootApplication` という**根拠つき**で特定している。
