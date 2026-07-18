package com.example.training.lending;

import jakarta.validation.constraints.NotNull;

/**
 * 貸出（POST /api/lendings）のリクエストボディを受け取る入れ物（DTO）。
 *
 * <p>「どの本を、誰が借りるか」だけを受け取る。貸出日・返却期限は
 * Service 側で決める（貸出日＝今日、返却期限＝2週間後）ので、ここには持たない。
 *
 * <p>{@code @NotNull} … 未指定なら入力チェック違反（→400）。
 * 形式のチェックはここで、業務ルール（在庫があるか等）は Service で行う、という
 * 役割分担にしている。
 */
public class LendingRequest {

    @NotNull(message = "bookId は必須です")
    private Long bookId;

    @NotNull(message = "memberId は必須です")
    private Long memberId;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
