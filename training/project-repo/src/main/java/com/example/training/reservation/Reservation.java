package com.example.training.reservation;

import java.time.LocalDate;

/**
 * reservations テーブルの1行に対応する入れ物（エンティティ）。
 *
 * <p>「誰が・どの本を・いつ予約したか」の記録。
 * {@code status} は waiting（待ち中） / canceled（キャンセル済み）。
 */
public class Reservation {

    private Long id;
    private Long bookId;
    private Long memberId;
    private LocalDate reservedAt;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDate getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(LocalDate reservedAt) {
        this.reservedAt = reservedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
