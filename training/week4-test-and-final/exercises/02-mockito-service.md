# 課題1-2: Mockitoで「依存をモック化」してServiceをテストする

**所要時間の目安**: 90〜120分

**AI利用区分**: ✅ AIにコードを書かせてよい。ただし**「なぜモックを使うのか」「when と verify が何をしているのか」を自分の言葉で説明できること**が必須。

---

## AI生成コードの説明記入欄（提出時に必ず埋める）

```
【なぜ本物のMapperではなくモックを使うのか】


【when(...).thenReturn(...) は何をしているか】


【verify(...) は何をしているか。assertEqualsとの役割の違いは】

```

---

## 1. 背景・狙い（現場でどう使うか）

Service（判断・処理役）は、たいてい Mapper（DB出し入れ役）に**依存**しています。Serviceだけをテストしたいのに、本物のDBにつなぐと「DBが立っていないと落ちる」「データの状態でテスト結果が変わる」など面倒が増えます。

そこで、依存する Mapper を**モック（ニセモノ）**に置き換え、「Mapperがこう返したら、Serviceはこう振る舞うはず」だけをピンポイントで確認します。これが現場の単体テストの基本形です。

> 用語メモ: **モック** … 本物のフリをするニセの部品。好きな戻り値を返させたり、「呼ばれたか」を後から確認できる。
> 用語メモ: **スタブ** … モックに仕込む「こう呼ばれたらこう返す」という振る舞い（`when(...).thenReturn(...)`）。
> 用語メモ: **Mockito（モキート）** … Javaでモックを作る定番ライブラリ。`spring-boot-starter-test` に同梱。

---

## 2. テスト対象（`solutions/` に動く形で置いてある）

3つのクラスが関係します（すべて完成済み。あなたが書くのは**テスト**）。

- `Member`（id, name, point を持つ入れ物）
- `MemberMapper`（インタフェース。`Member findById(int id)` と `int updatePoint(int id, int newPoint)`）
- `MemberPointService`（テスト対象）

```java
public class MemberPointService {

    private final MemberMapper memberMapper;
    public MemberPointService(MemberMapper memberMapper) { this.memberMapper = memberMapper; }

    // 仕様:
    //  - addPoint が0以下なら IllegalArgumentException（DBアクセスはしない）
    //  - findById で会員を取得。null なら IllegalArgumentException
    //  - 「今のポイント + 加算分」を計算し、updatePoint で更新
    //  - 更新後の合計ポイントを返す
    public int addPoint(int memberId, int addPoint) { ... }
}
```

---

## 3. 詳細仕様: あなたが書くテスト

`src/test/java/com/example/training/MemberPointServiceTest.java` を作り、次の3ケースを書いてください。

### テストクラスの骨組み（このシグネチャで始める）
```java
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberPointServiceTest {

    @Mock
    private MemberMapper memberMapper;       // ← モックにする依存

    @InjectMocks
    private MemberPointService service;       // ← モックを差し込んだテスト対象

    // ここにテストメソッドを書く
}
```

### 確認すべきケース一覧
| # | ケース | 仕込み（スタブ） | 確認すること |
|---|---|---|---|
| 1 | **加算成功** | `findById(1)` が `new Member(1,"佐藤",100)` を返す | 戻り値が `150`。かつ `updatePoint(1, 150)` が**呼ばれた**（`verify`） |
| 2 | **会員が存在しない** | `findById(99)` が `null` を返す | `IllegalArgumentException` が投げられる。かつ `updatePoint` が**呼ばれていない**（`verify(..., never())`） |
| 3 | **加算ポイントが0以下** | （スタブ不要） | `addPoint(1, 0)` で例外。かつ `findById` も `updatePoint` も**呼ばれていない** |

> ケース3でなぜスタブが要らないか: 入力チェックで先に弾かれるので `findById` まで処理が進まない。使わないスタブを書くとMockitoが「未使用だ」と警告するので、書かないのが正解。

---

## 4. 完成条件（自分で判定できる）

- [ ] 3ケースすべてを書いた
- [ ] 少なくとも1つで `verify(...)`、1つで `verify(..., never())` を使った
- [ ] `./gradlew test` が**緑**で終わる
- [ ] 説明記入欄を埋めた

---

## 5. 検証方法

```bash
./gradlew test
```
**期待結果**: `BUILD SUCCESSFUL`。

---

## 6. つまずきポイントとヒント

<details>
<summary>`verify` の書き方が分からない</summary>

「そのメソッドが、その引数で呼ばれたか」を後から確認します。
```java
verify(memberMapper).updatePoint(1, 150);          // 1回呼ばれたことを確認
verify(memberMapper, never()).updatePoint(anyInt(), anyInt()); // 一度も呼ばれていない
```
`anyInt()` は「どんなint引数でも」の意味（`org.mockito.ArgumentMatchers.anyInt`）。
</details>

<details>
<summary>「UnnecessaryStubbingException」と出て落ちる</summary>

`when(...).thenReturn(...)` で仕込んだスタブが、テスト中に一度も使われていません。ケース3のように「処理がそこまで進まない」テストでは、スタブを書かないのが正解です。
</details>

<details>
<summary>assertEqualsとverifyの使い分けが分からない</summary>

- `assertEquals` … Serviceの**戻り値**が正しいかを見る。
- `verify` … Serviceが**依存（モック）を正しく呼んだか**を見る。
両方を使うことで「結果」も「過程」も確認できます。
</details>

---

## 7. 模範解答への導線

緑になったら `solutions/unit-test-exercises/src/test/java/com/example/training/MemberPointServiceTest.java` と比較してください。
