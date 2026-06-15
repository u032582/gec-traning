# 課題03: 顧客を登録する `POST /api/customers`（入力チェック付き）

**目安時間: 80分**

> AI利用区分: ✅ AIにコードを書かせてよい。ただし**生成コードを自分の言葉で説明でき、なぜその実装かを言えること**が必須。下の「AI生成コードの説明記入欄」を必ず埋めること。

---

## 0. AI生成コードの説明記入欄（AIに書かせた場合は必ず埋める）

**(1) このコードは何をしているか（自分の言葉で）:**
```
（例: リクエストボディのJSONを受け取り、入力チェックを通ったらDBにINSERTし、登録結果を返す）
（ここに記入）
```

**(2) なぜこの実装にしたか（なぜリクエスト用DTOを別に作るか、なぜ@Validを付けるか 等）:**
```
（ここに記入）
```

---

## 1. 背景・狙い（現場でどう使うか）

これまでは「データを読む（GET）」でしたが、今回は「データを作る（POST）」です。
登録・更新の処理では、**外から来たデータをそのまま信じてはいけません**。「名前が空」「メールの形が変」など、
おかしな入力をDBに入れる前に**門前払い（バリデーション）**するのが鉄則です。
この課題では、リクエストボディ（JSON）の受け取りと、**Bean Validation による入力チェック**を学びます。

> 用語メモ: **POST（ポスト）** … サーバーに「これを登録して」とデータを送るときのメソッド。
> 用語メモ: **リクエストボディ** … リクエストに乗せて送る本体データ。今回はJSON。
> 用語メモ: **バリデーション** … 入力が正しいかどうかの検査。例:「名前は必須」「メールは正しい形式」。

---

## 2. 詳細設計（この通りに作る）

### エンドポイント
| 項目 | 内容 |
|---|---|
| メソッド | `POST` |
| パス | `/api/customers` |
| 対象テーブル | `customers`（列: `customer_id`(自動採番), `name`, `email`, `created_at`(自動)） |

### リクエスト（ボディ JSON）
```json
{
  "name": "田中 次郎",
  "email": "jiro.tanaka@example.com"
}
```

### 入力チェックのルール（バリデーション）
| 項目 | ルール | 違反時のイメージ |
|---|---|---|
| `name` | 必須（空文字・空白だけ・未指定はNG）。100文字以内 | 空なら登録させない |
| `email` | 必須。メールアドレスの形式であること | `abc`（@が無い等）はNG |

### レスポンス（成功時）
- ステータス: `201 Created`（「新しく作った」を表すステータス）
- ボディ（JSON）: 登録された顧客（自動採番された id を含む）。課題1と同じ形。
```json
{
  "id": 4,
  "name": "田中 次郎",
  "email": "jiro.tanaka@example.com"
}
```

### 異常時の挙動
- 入力チェックに違反したとき: ステータス `400 Bad Request`（「リクエストがおかしい」を表す）。
  - この課題では、Spring が**自動で返す**400のままでOKです（ボディの中身の整形は問いません）。
  - エラーレスポンスをきれいに整える対応は、**課題4**でまとめて作ります。

> 用語メモ: **201 / 400** … HTTPステータスコード。200番台は成功、400番台は「依頼側（クライアント）の誤り」。
> 201は「作成成功」、400は「リクエストが不正」。

---

## 3. 作るもの

| 作る/触るファイル | 役割 |
|---|---|
| `customer/CustomerCreateRequest.java` | **リクエスト用DTO**。name, email を持ち、バリデーション注釈を付ける |
| `customer/CustomerMapper.java` | `insert` メソッドを追加 |
| `resources/mapper/CustomerMapper.xml` | INSERT文を追加（採番されたidを取得する設定も） |
| `customer/CustomerService.java` | 登録メソッドを追加（INSERTして、結果をDTOで返す） |
| `customer/CustomerController.java` | `POST /api/customers` を追加（`@RequestBody @Valid`、201で返す） |

> 用語メモ: **リクエスト用DTO** … 「外から受け取る入力の形」を表す入れ物。返す用（Response）とは別に作ります。
> 入力と出力で項目が違うことが多いので、分けておくと安全です（例: 入力に id は無いが、出力には id がある）。

---

## 4. 完成条件（自己判定基準）

- 正しいJSONをPOSTすると、`201` と、id付きの顧客JSONが返る。
- name が空、または email の形式が不正なJSONをPOSTすると、`400` が返る（DBには入らない）。

---

## 5. 検証方法（自分で正解判定する）

> コードを変えたら**アプリを起動し直す**こと。

### 正常系（201が返る）
```bash
curl -i -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"田中 次郎","email":"jiro.tanaka@example.com"}'
```
**期待**: 先頭に `HTTP/1.1 201`、ボディに登録された顧客（idは自動採番なので 4 以降のどれか）:
```json
{"id":4,"name":"田中 次郎","email":"jiro.tanaka@example.com"}
```
> 登録できたか、課題1のGETでも確認できます: `curl http://localhost:8080/api/customers/4`

### 異常系1: name が空（400が返る）
```bash
curl -i -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"","email":"foo@example.com"}'
```
**期待**: 先頭に `HTTP/1.1 400`。

### 異常系2: email の形式が不正（400が返る）
```bash
curl -i -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"田中 次郎","email":"これはメールではない"}'
```
**期待**: 先頭に `HTTP/1.1 400`。

> `-X POST` は「POSTで送る」、`-H "Content-Type: application/json"` は「中身はJSONです」という宣言、
> `-d '...'` が送るボディです。これらのコマンドはWSL2のターミナル（Ubuntu / bash）で実行してください。

---

## 6. つまずきポイントとヒント

<details>
<summary>ヒント1: リクエスト用DTOにバリデーション注釈を付ける</summary>

`spring-boot-starter-validation` が雛形に入っているので、注釈を付けるだけで検査できます。
```java
public record CustomerCreateRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Email String email
) {}
```
- `@NotBlank` … null・空文字・空白だけを禁止（「必須」の意味。文字列にはこれが定番）。
- `@Size(max = 100)` … 最大文字数。
- `@Email` … メールアドレスの形式かどうか。
- import は `jakarta.validation.constraints.*`（`javax` ではない点に注意。Spring Boot 3系は `jakarta`）。
</details>

<details>
<summary>ヒント2: Controllerで @RequestBody と @Valid を付ける</summary>

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)   // 成功時を201にする
public CustomerResponse create(@RequestBody @Valid CustomerCreateRequest request) {
    return customerService.create(request);
}
```
- `@RequestBody` … リクエストのJSONを、引数のDTOに変換して受け取る。
- `@Valid` … 「このDTOにバリデーションをかけて」という指示。これが**無いと検査されません**（よくある罠）。
- `@ResponseStatus(HttpStatus.CREATED)` … 成功時のステータスを201にする。
- `@Valid` で違反があると、Springが自動で400を返してくれます（この課題ではそれでOK）。
</details>

<details>
<summary>ヒント3: INSERTと「採番されたID」の取得（MyBatis）</summary>

INSERT後、DBが自動で振った `customer_id` を受け取りたい。MyBatisではXMLでこう書きます。
```xml
<insert id="insert" useGeneratedKeys="true" keyProperty="customerId"
        parameterType="com.example.training.customer.Customer">
    INSERT INTO customers (name, email)
    VALUES (#{name}, #{email})
</insert>
```
- `useGeneratedKeys="true"` + `keyProperty="customerId"` … 採番されたIDを、渡した Customer オブジェクトの `customerId` に書き戻してくれる。
- Mapperインターフェース側:
  ```java
  void insert(Customer customer);
  ```
- Service では、リクエストDTO → Customer に詰め替えて insert し、書き戻された customerId を使って ResponseDTO を作ります。
  ```java
  Customer c = new Customer();
  c.setName(request.name());
  c.setEmail(request.email());
  customerMapper.insert(c);          // ここで c.customerId にIDが入る
  return CustomerResponse.from(c);
  ```
</details>

<details>
<summary>ヒント4: created_at は書かなくていい</summary>

`customers.created_at` はDB側で `DEFAULT CURRENT_TIMESTAMP`（自動で今の時刻）になっています。
なのでINSERT文に created_at を入れる必要はありません。name と email だけ入れればOKです。
</details>

<details>
<summary>ヒント5: curlのボディ（JSON）の渡し方</summary>

このコマンドはWSL2のターミナル（Ubuntu / bash）で実行します。bashではシングルクォート `'...'` がそのまま使えるので、上の例のように `-d '{"name":"..."}'` で送れます。
- JSONが長くなる場合は、ファイルに保存して `-d @body.json` のようにファイル指定すると楽です。
- どうしても難しければ、Postman や VS Code の REST Client など、GUIツールで送ってもOKです。
</details>

---

## 7. 模範解答への導線

自力で完成させてから、[solutions/03-create-customer/](../solutions/03-create-customer/) を見て比べてください。
解答には、追加したファイルと「なぜリクエスト用DTOを分けるのか」の解説を載せています。
