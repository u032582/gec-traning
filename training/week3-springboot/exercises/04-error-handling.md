# 課題04: 例外ハンドリングで「無いものは404」を返す

**目安時間: 80分**

> AI利用区分: ✅ AIにコードを書かせてよい。ただし**生成コードを自分の言葉で説明でき、なぜその実装かを言えること**が必須。下の「AI生成コードの説明記入欄」を必ず埋めること。

---

## 0. AI生成コードの説明記入欄（AIに書かせた場合は必ず埋める）

**(1) このコードは何をしているか（自分の言葉で）:**
```
（例: 商品が見つからないとき例外を投げ、@RestControllerAdvice がそれを受け取って404と整形済みJSONに変換する）
（ここに記入）
```

**(2) なぜこの実装にしたか（なぜ各Controllerでtry-catchせず共通の仕組みにするか 等）:**
```
（ここに記入）
```

---

## 1. 背景・狙い（現場でどう使うか）

課題1では「存在しないIDでも200＋空」でよしとしました。でも本当は、無いものを頼まれたら
**「404 Not Found（見つかりません）」** を返すのが正しい作法です。
さらに現場では、エラーのときに返すJSONの形も**統一**しておきたい（フロント側が処理しやすいように）。

そこで使うのが **`@RestControllerAdvice`** という「全Controller共通のエラー係」です。
各Controllerで毎回 try-catch を書く代わりに、**例外を投げれば、共通の係がまとめて捕まえて整形して返す**——
これがSpringの定番のやり方です。「同じ処理を一箇所に集める」ことで、重複が消え、変更も一箇所で済みます。

> 用語メモ: **例外（れいがい）を投げる（throw）** … 「異常が起きた」と知らせる仕組み（週1で学んだ try-catch の片割れ）。
> 用語メモ: **@RestControllerAdvice** … アプリ全体のControllerをまとめて見張り、特定の例外を捕まえて
> 共通のレスポンスに変換する「横断的なエラー処理係」。

---

## 2. 詳細設計（この通りに作る）

この課題は2つのパートに分かれます。

### パートA: 「見つからない」例外と共通ハンドラを作る
1. **自作の例外クラス** `ResourceNotFoundException`（実行時例外 = `RuntimeException` を継承）を作る。
2. **共通エラーハンドラ** `GlobalExceptionHandler`（`@RestControllerAdvice`）を作り、
   - `ResourceNotFoundException` を捕まえたら **404** ＋ 整形JSON を返す。
   - （任意・発展）課題3の入力チェック違反（`MethodArgumentNotValidException`）を捕まえて **400** ＋ 整形JSON を返す。

#### エラーレスポンスの形（共通フォーマット）
```json
{
  "status": 404,
  "message": "customer not found: id=999"
}
```
> `status` は数値のステータスコード、`message` は人が読める説明。この2項目に統一します。

### パートB: 既存のAPIを「無ければ404」に直す
- `GET /api/customers/{id}`（お手本）と、課題1の `GET /api/products/{id}` を修正し、
  対象が見つからなければ `ResourceNotFoundException` を投げるようにする。

### まとめ（エンドポイント別の期待挙動）
| エンドポイント | 存在するID | 存在しないID |
|---|---|---|
| `GET /api/customers/{id}` | 200 + 顧客JSON | **404 + エラーJSON** |
| `GET /api/products/{id}` | 200 + 商品JSON | **404 + エラーJSON** |

---

## 3. 作るもの

| 作る/触るファイル | 役割 |
|---|---|
| `（共通パッケージ）/ResourceNotFoundException.java` | 「見つからない」を表す自作例外 |
| `（共通パッケージ）/GlobalExceptionHandler.java` | `@RestControllerAdvice` の共通エラー係 |
| `（共通パッケージ）/ErrorResponse.java` | エラーJSONの形（status, message）のDTO |
| `CustomerService.java` | 見つからなければ例外を投げるよう修正 |
| `ProductService.java` | 同上（課題1で作ったもの） |

> 配置の目安: 共通の3ファイルは `com.example.training.common` のような新パッケージにまとめると整理しやすいです。

---

## 4. 完成条件（自己判定基準）

- 存在するIDの取得は、これまで通り 200 ＋ データJSON が返る（壊していない）。
- 存在しないIDの取得は、**404 ＋ `{"status":404,"message":"..."}`** が返る。
- （発展をやった場合）課題3の不正入力が **400 ＋ 整形JSON** で返る。

---

## 5. 検証方法（自分で正解判定する）

> コードを変えたら**アプリを起動し直す**こと。

### 既存が壊れていないか（200のまま）
```bash
curl -i http://localhost:8080/api/customers/1
```
**期待**: `HTTP/1.1 200` ＋ `{"id":1,"name":"山田 太郎","email":"taro.yamada@example.com"}`

### 存在しない顧客 → 404
```bash
curl -i http://localhost:8080/api/customers/999
```
**期待**:
```
HTTP/1.1 404
...
{"status":404,"message":"customer not found: id=999"}
```

### 存在しない商品 → 404
```bash
curl -i http://localhost:8080/api/products/999
```
**期待**: `HTTP/1.1 404` ＋ `{"status":404,"message":"product not found: id=999"}`

> message の文言は上と完全一致でなくてもOK（「何が無かったか」が分かる内容ならよい）。
> 大事なのは **ステータスが404** で、**status と message を持つJSON** が返ること。

### （発展）不正入力 → 400 が整形されて返る
```bash
curl -i -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"","email":"foo@example.com"}'
```
**期待**: `HTTP/1.1 400` ＋ status/message を持つJSON。

---

## 6. つまずきポイントとヒント

<details>
<summary>ヒント1: 自作例外クラス</summary>

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```
- `RuntimeException` を継承すると、メソッドに `throws` を書かなくても投げられます（扱いが楽）。
- メッセージ（何が無かったか）をコンストラクタで受け取り、`super(message)` で親に渡しておくと、
  ハンドラ側で `e.getMessage()` として取り出せます。
</details>

<details>
<summary>ヒント2: 共通エラーハンドラ（@RestControllerAdvice）の骨組み</summary>

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)               // 404
    public ErrorResponse handleNotFound(ResourceNotFoundException e) {
        return new ErrorResponse(404, e.getMessage());
    }
}
```
- `@ExceptionHandler(○○.class)` … 「この例外が投げられたら、このメソッドで処理する」という指定。
- `@ResponseStatus(HttpStatus.NOT_FOUND)` … 返すステータスを404にする。
- 返した `ErrorResponse` は、`@RestController` と同様に自動でJSONになります。
- このクラスは**どのControllerにも属さず、全体を見張る**ので、各Controllerでtry-catchは不要になります。
</details>

<details>
<summary>ヒント3: エラーレスポンスDTO</summary>

```java
public record ErrorResponse(int status, String message) {}
```
これだけ。`{"status":404,"message":"..."}` というJSONになります。
</details>

<details>
<summary>ヒント4: Serviceで例外を投げる（404の出どころ）</summary>

課題1までは「見つからなければ null を返す」でした。これを「見つからなければ例外を投げる」に変えます。
```java
public CustomerResponse findById(Long id) {
    Customer customer = customerMapper.findById(id);
    if (customer == null) {
        throw new ResourceNotFoundException("customer not found: id=" + id);
    }
    return CustomerResponse.from(customer);
}
```
Controllerは何も変えなくてOK（例外は自動で上の共通ハンドラに飛んでいきます）。これが「一箇所に集める」効果です。
</details>

<details>
<summary>ヒント5（発展）: バリデーション違反(400)も整形する</summary>

課題3の `@Valid` 違反は `MethodArgumentNotValidException` という例外になります。これも同じハンドラで捕まえられます。
```java
@ExceptionHandler(MethodArgumentNotValidException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)                 // 400
public ErrorResponse handleValidation(MethodArgumentNotValidException e) {
    String msg = e.getBindingResult().getFieldErrors().stream()
            .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
            .collect(java.util.stream.Collectors.joining(", "));
    return new ErrorResponse(400, msg);
}
```
import: `org.springframework.web.bind.MethodArgumentNotValidException`。
</details>

---

## 7. 模範解答への導線

自力で完成させてから、[solutions/04-error-handling/](../solutions/04-error-handling/) を見て比べてください。
解答には、共通ハンドラ・自作例外・修正後のServiceと、「なぜ共通化するのか」の解説を載せています。

これで週3は完了です。お疲れさまでした。**到達目標「詳細設計を渡されたら、APIを1本実装できる」**に
近づけたか、各課題の説明記入欄を読み返して、自分の言葉で説明できるか確認してください。
