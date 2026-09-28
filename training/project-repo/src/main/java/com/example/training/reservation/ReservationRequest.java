package com.example.training.reservation;

import jakarta.validation.constraints.NotNull;

/**
 * 予約（POST /api/reservations）のリクエストボディを受け取る入れ物（DTO）。
 *
 * <p>「どの本を、誰が予約するか」だけを受け取る。予約日・status は Service 側で決める。
 */
public class ReservationRequest {

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
