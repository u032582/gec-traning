# 模範解答 03: 顧客登録 `POST /api/customers`（入力チェック付き）

雛形の customer 一式に、登録（POST）と入力チェックを追加した版です。

## 置き場所

| このフォルダのファイル | 雛形プロジェクト内の置き場所 |
|---|---|
| `customer/CustomerCreateRequest.java` | `src/main/java/com/example/training/customer/CustomerCreateRequest.java`（新規） |
| `customer/CustomerMapper.java` | `.../customer/CustomerMapper.java`（insert を追加） |
| `customer/CustomerService.java` | `.../customer/CustomerService.java`（create を追加） |
| `customer/CustomerController.java` | `.../customer/CustomerController.java`（POSTを追加） |
| `mapper/CustomerMapper.xml` | `src/main/resources/mapper/CustomerMapper.xml`（insert を追加・差し替え） |

## 要点の解説

### なぜリクエスト用DTOを分けるのか
- 入力（CustomerCreateRequest）には `id` が無く、出力（CustomerResponse）には `id` がある。
  入力と出力で必要な項目が違うので、1つのクラスで兼ねると無理が出る。
- 入力DTOにだけバリデーション注釈を付けられる（出力には不要）。
- 「外から来る形」を専用クラスで受けることで、エンティティ（DBの形）を外部入力から守れる。

### バリデーション（入力チェック）
- 注釈は `jakarta.validation.constraints.*`（Spring Boot 3系は `javax` ではなく **`jakarta`**）。
- `@NotBlank` … null・空文字・空白だけを禁止（文字列の「必須」の定番。`@NotNull` より厳しい）。
- `@Email` … メール形式チェック。`@Size(max=100)` … 最大文字数。
- **Controllerで `@Valid` を付けないと検査が走らない**。これが一番よくある罠。
- 違反時、Springが自動で 400 を返す。レスポンスの中身を整える対応は課題4。

### INSERTと採番IDの取得（MyBatis）
- `useGeneratedKeys="true"` + `keyProperty="customerId"` で、DBが振った id を Customer に書き戻す。
- Service は「リクエストDTO → Customer に詰め替え → insert → 書き戻された id を使って ResponseDTO」を作る。
- `created_at` はDBの `DEFAULT CURRENT_TIMESTAMP` 任せなので、INSERT文に書かない。

### 成功ステータス 201
- `@ResponseStatus(HttpStatus.CREATED)` で「作成成功（201）」を返す。
  「取得（GET）は200、作成（POST）は201」が定番の作法。

## セルフチェック

- 入力用DTOと出力用DTOを分ける理由を説明できるか？
- `@Valid` を消すとどうなるか説明できるか？
- 採番された id が、なぜ insert 後の Customer に入っているのか説明できるか？
- 200 と 201、400 の違いを説明できるか？
