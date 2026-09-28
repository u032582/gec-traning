package com.example.training.reservation;

import java.time.LocalDate;

/**
 * 予約情報を外（APIの利用者）に返すための入れ物（DTO）。
 */
public class ReservationResponse {

    private final Long id;
    private final Long bookId;
    private final Long memberId;
    private final LocalDate reservedAt;
    private final String status;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.bookId = reservation.getBookId();
        this.memberId = reservation.getMemberId();
        this.reservedAt = reservation.getReservedAt();
        this.status = reservation.getStatus();
    }

    public Long getId() {
        return id;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDate getReservedAt() {
        return reservedAt;
    }

    public String getStatus() {
        return status;
    }
}
