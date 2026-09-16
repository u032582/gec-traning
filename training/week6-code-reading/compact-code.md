# 畳まれた書き方を、ほどいて読む

> **これは参照用のページです。** 週6でひととおり目を通したら、あとは**困ったときに戻ってくる**使い方をしてください。週7以降も、既存コードを読むたびに出てきます。

既存のコードが読めない原因は、たいてい文法ではありません。**書いた人が短く畳んでいる**からです。畳まれると、途中の段階が見えなくなります。

読みにくいのは、あなたの理解が足りないからではありません。**省略されているものを、頭の中で戻す必要がある**だけです。戻し方は決まっています。

---

## 0. 道具は3つだけ

畳まれた書き方は、全部この3つでほどけます。

1. **内側から外へ読む**。括弧が重なっていたら、いちばん内側が最初に動きます。
2. **途中を変数に置く**。名前を付けて1行ずつに分けると、動く順番が「上から下」になります。
3. **`Ctrl` を押しながらクリックして、定義に飛ぶ**。そこに書いてある戻り値の型が、そのメソッドの「出口」です。

以下は、この3つを具体的な形に当てはめていくだけです。

---

## 1. 入れ子の式（括弧が重なっている）

題材リポの `LendingController` に、こんな行があります。

```java
return new LendingResponse(lendingService.returnBook(id, LocalDate.now()));
```

左から読むと「まず `LendingResponse` を作って…」と読みたくなります。でも**動く順番は、内側から外側へ**です。ここを読み違えるのは非常によくあることで、書いた本人が数日後に読み違えることもあります。

> 用語メモ: **入れ子（ネスト）** … メソッドを呼んだ結果を、そのまま別のメソッドの引数として渡す書き方。`a(b(c()))` のように括弧が重なります。

**いちばん内側の括弧から順に、変数に入れてほどきます。**

```java
LocalDate today = LocalDate.now();                       // ① 今日の日付を作って
Lending lending = lendingService.returnBook(id, today);  // ② それで返却処理をして、貸出を受け取って
LendingResponse response = new LendingResponse(lending); // ③ それを包んで
return response;                                         // ④ 外へ返す
```

ほどくと、**動く順番が「上から下」になります**。さっきの1行は、この4行を畳んだものでした。

そして、ほどくと見えてくるものがあります。**変数が、メソッドとメソッドの「つなぎ目」になっている**ことです。

- `lendingService.returnBook(...)` の**出口**が `lending`
- その `lending` が、`new LendingResponse(...)` の**入口**

メソッドは「何かを受け取って、何かを返す箱」で、変数はその箱と箱をつなぐ線です。**畳んだ1行では、この線が消えます。**

---

## 2. 繰り返しの畳み方（`stream()` と `->` と `::`）

題材リポの `LendingController` には、こんな3行もあります。

```java
return lendingService.findByMember(id).stream()
        .map(LendingResponse::new)
        .toList();
```

`::` を初めて見ると、たいてい手が止まります。**これは、週1で書いた for文を2段階畳んだもの**です。逆にたどれば、見慣れた形に戻ります。

### ① いちばんほどいた形（週1で書いたのと同じ）

```java
List<Lending> lendings = lendingService.findByMember(id);   // 貸出を全部もらって
List<LendingResponse> responses = new ArrayList<>();
for (Lending lending : lendings) {                          // 1個ずつ取り出して
    responses.add(new LendingResponse(lending));            // 包んで、詰めていく
}
return responses;
```

やっていることは「**1個ずつ取り出して、包んで、詰め直す**」だけです。

### ② 1段階畳む（`->` のラムダ）

```java
return lendingService.findByMember(id).stream()
        .map(lending -> new LendingResponse(lending))
        .toList();
```

> 用語メモ: **`stream()`（ストリーム）** … 「1個ずつ順番に流す」という意味。リストを、上の for文のように**1個ずつ処理していく形**に変える入口です。
> 用語メモ: **`.map(...)`（マップ）** … 流れてきたものを**1個ずつ別のものに変える**。上の for文の中でやっていた「包む」にあたります。
> 用語メモ: **`.toList()`** … 流し終わったものを、またリストに詰め直す。上の `responses` にあたります。
> 用語メモ: **ラムダ式（`->`）** … 名前のない、その場限りの小さなメソッド。`lending -> new LendingResponse(lending)` は「`lending` を受け取って、包んで返す」という意味です。左が引数、右が中身。

`->` の左右が、そのまま**入口と出口**になっています。ここは §1 と同じ話です。

### ③ もう1段階畳む（`::` のメソッド参照）

```java
return lendingService.findByMember(id).stream()
        .map(LendingResponse::new)
        .toList();
```

`lending -> new LendingResponse(lending)` は、「受け取ったものを、そのまま `new LendingResponse(...)` に渡すだけ」です。**渡すだけなら、渡し先の名前を書けば足りる**——それが `LendingResponse::new` です。

> 用語メモ: **メソッド参照（`::`）** … 「このメソッドに、そのまま渡してね」という書き方。`LendingResponse::new` は「`new LendingResponse(...)` に渡す」、`String::length` なら「`length()` を呼ぶ」。**ラムダ式が『受け取ってそのまま渡すだけ』のときに限って使える短縮形**です。

### 読み方のコツは1つ

**`::` を見たら `->` に戻す。`->` を見たら for文に戻す。**

慣れるまでは、頭の中で毎回2段階戻して構いません。戻せるなら、畳んだままでも読めるようになります。

---

## 3. もっと畳まれた例（いまは読めなくて大丈夫）

同じ題材リポの `common/GlobalExceptionHandler.java` には、こんな箇所があります。

```java
Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.toMap(
                fieldError -> fieldError.getField(),
                fieldError -> fieldError.getDefaultMessage() == null
                        ? "不正な値です"
                        : fieldError.getDefaultMessage(),
                (a, b) -> a)); // 同じ項目に複数エラーが出たら先勝ち
```

**いまの段階で読めなくても、まったく問題ありません。** 週6〜12は、ここが読めなくても進められます。

ただ、**道具は同じ**だと知っておいてください。内側から読む、途中を変数に置く、定義に飛ぶ。この3つで必ずほどけます。いつか必要になったとき、新しいことを覚え直す必要はありません。

---

## 4. では、なぜ現場のコードは畳んであるのか

**短いからです。** 慣れた人には、畳んであるほうが速く読めます。だから既存コードはたいてい畳んであります。

**あなたがやることは、書き方を変えることではなく、頭の中でほどけるようになること**です。ほどければ、畳んだままでも読めます。既存コードをほどいた形に書き直す必要はありません（勝手に書き換えると、それはそれで問題になります）。

---

## やってみる

題材リポの `lending/LendingController.java` を開いて、次の2つをほどいてみてください。**ファイルを書き換える必要はありません。紙かメモに書き出すだけ**です。

1. `returnBook` の1行を、§1 の①〜④のように分ける
2. `historyByMember` の3行を、§2 の①（for文）の形に戻す

ほどけたら、次に答えられるはずです。

- [ ] `returnBook` で、いちばん最初に動くのはどれか
- [ ] `returnBook` が返してきたものは、次にどこへ渡っているか
- [ ] `historyByMember` の `.map(LendingResponse::new)` は、1個ずつ何をしているか
- [ ] `historyByMember` が最後に外へ返しているものは何か

> 迷ったら `Ctrl` を押しながらメソッド名をクリックして、定義に飛んでください。**そこに書いてある戻り値の型が、その箱の「出口」**です。

---

関連: [週6のコラム「識別子の英語を読む」](README.md#コラム-識別子の英語を読む読解の地味だけど本当の急所)（`incrementAvailable` のような名前が読めないとき）
