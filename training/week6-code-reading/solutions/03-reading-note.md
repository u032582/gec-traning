# 模範: 課題03 読解ノート（記入例）

> これは**記入の見本**です。行番号はこの教材を書いた時点のもの。ズレていても、自分で開いて確かめていれば正解です。
> この課題の山は「**SQL側の守り**」と「**@Transactional**」を自分で見つけられたか。そこを重点的に。

---

【POST /api/lendings で在庫が1減るまでの流れ（ファイル名:行）】
 1. 窓口       : LendingController.java:35  の `lend(@Valid @RequestBody LendingRequest request)` が受ける（`@PostMapping("/api/lendings")` は34行目）。ここで `LocalDate.now()`（今日）を Service に渡している（36行目）。
 2. 業務処理    : LendingService.java:52    の `lend(LendingRequest request, LocalDate today)` へ
 3. 借り主確認  : LendingService.java:54 `memberService.findById(request.getMemberId())`（→ MemberService.java の findById。いなければ404）
 4. 在庫を減らす : LendingService.java:57 `bookMapper.decrementAvailable(request.getBookId())` → BookMapper.java:32 の宣言
 5. 在庫SQL     : BookMapper.xml:53 の `<update id="decrementAvailable">`（実際のUPDATE。`available_count = available_count - 1`）
 6. 貸出記録作成 : LendingService.java:75 `lendingMapper.insert(lending)`

【在庫0を弾く守りは、どこに何個あるか】→ **2箇所ある**
 - Java側（Service）: LendingService.java:58 あたり。`decrementAvailable` の戻り値が 0（＝1行も減らせなかった）なら、在庫切れとみなして BusinessRuleException を投げる（→409）。
 - SQL側（XML）     : BookMapper.xml:57 の `WHERE id = #{id} AND available_count > 0`。**在庫が残っているときだけ減る**。この条件があるおかげで、2件の貸出が同時に来ても最後の1冊を二重に貸すことがない（DB側の安全弁）。

【2テーブル更新を1まとまりにしているアノテーションは何か・どのファイルの何行目か】
 - `@Transactional`。LendingService.java:51（lend の直前）と、返却の `returnBook` の直前（87行目あたり）にも付いている。「lendings に足す」と「books の在庫を減らす」を両方成功か両方失敗にして、在庫が狂わないようにしている。

【AIの要約が取りこぼした/間違えた点はあったか（特にSQL側の守り）】
 - AIは「LendingService で在庫が0かチェックして弾いています」とだけ要約し、**SQL側（BookMapper.xml の WHERE available_count > 0）の守りを取りこぼした**。自分で XML を開いて WHERE を見て、守りが2箇所あることに気づけた。ここが今回の裏取りの一番の収穫。

---

## この記入例で見てほしいポイント

- 入口から在庫減少まで、**lending → member → book と3パッケージを横断**して追い切っている。
- 在庫0の守りが「**Java側だけでなくSQL側にもある（2箇所）**」ことに、自分でXMLを開いて気づいている。AIの要約で一番こぼれやすい所。
- `@Transactional` を見つけ、「なぜ2テーブル更新をまとめる必要があるか（在庫が狂う）」まで理解している。
- 「AIはSQL側の守りを取りこぼした」と、**裏取りで見つけた具体的なギャップ**を書いている。これが週7（影響範囲）につながる読み方。
