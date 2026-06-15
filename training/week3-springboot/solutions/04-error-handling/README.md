# 模範解答 04: 例外ハンドリングで404を返す

課題1〜3の上に、共通のエラー処理（404 / 400の整形）を追加した版です。

## 置き場所

| このフォルダのファイル | 雛形プロジェクト内の置き場所 |
|---|---|
| `common/ResourceNotFoundException.java` | `src/main/java/com/example/training/common/ResourceNotFoundException.java`（新規） |
| `common/ErrorResponse.java` | `.../common/ErrorResponse.java`（新規） |
| `common/GlobalExceptionHandler.java` | `.../common/GlobalExceptionHandler.java`（新規） |
| `customer/CustomerService.java` | `.../customer/CustomerService.java`（findById を「無ければ例外」に修正・差し替え） |
| `product/ProductService.java` | `.../product/ProductService.java`（findById を「無ければ例外」に修正・差し替え） |

> `common` という新パッケージ（フォルダ）を作って、共通の3ファイルをまとめます。
> `@MapperScan` や `@SpringBootApplication` のスキャン範囲は `com.example.training` なので、
> その下の `common` パッケージに置けば、`@RestControllerAdvice` も自動で有効になります。

## 要点の解説

### なぜ各Controllerでtry-catchせず、共通化するのか
- もし各Controller・各メソッドで「null だったら404を返す」を毎回書くと、**同じコードが散らばる**。
  後でエラーJSONの形を変えたくなったら、全部直す羽目になる。
- `@RestControllerAdvice` に集約すれば、**エラー処理は一箇所**。変更も一箇所で済む。
  これは「重複を作らない＝変更点を一箇所に閉じる」という、きれいなコードの基本そのもの。

### 処理の流れ（404の場合）
1. `CustomerService.findById()` が、見つからないとき `ResourceNotFoundException` を**投げる**。
2. Controllerはその例外を捕まえない（try-catchしない）。例外はそのまま上に飛ぶ。
3. `GlobalExceptionHandler` の `@ExceptionHandler(ResourceNotFoundException.class)` が捕まえる。
4. ハンドラが `ErrorResponse(404, ...)` を返し、`@ResponseStatus(NOT_FOUND)` で404になる。
5. 結果、`404` ＋ `{"status":404,"message":"customer not found: id=999"}` が返る。

### 400（バリデーション違反）の整形（発展）
- 課題3で `@Valid` 違反は自動で400になっていたが、ボディはSpring既定の形だった。
- `MethodArgumentNotValidException` を同じハンドラで捕まえ、`ErrorResponse(400, ...)` に整形する。
- これで「404も400も、同じ `{status, message}` の形」に統一できる。

### Controllerを変えなくてよいのがポイント
- Service が例外を投げるだけで、Controllerは無修正。「例外を投げる側」と「整形して返す側」が
  きれいに分かれている。これが3層 + Advice の役割分担の利点。

## セルフチェック

- なぜ各Controllerでtry-catchせず `@RestControllerAdvice` に集約するのか説明できるか？
- Service が例外を投げてから、404のJSONが返るまでの流れを順番に説明できるか？
- 既存の「存在するIDの取得（200）」を壊していないことを、curlで確認したか？
