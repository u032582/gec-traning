package com.example.training.lending;

import java.time.LocalDate;

/**
 * 貸出情報を外（APIの利用者）に返すための入れ物（DTO）。
 *
 * <p>エンティティ {@link Lending} を詰め替えて返す。
 * {@code returned} は「返却済みかどうか」を分かりやすくした派生項目
 * （{@code returnedAt} が入っていれば true）。
 */
public class LendingResponse {

    private final Long id;
    private final Long bookId;
    private final Long memberId;
    private final LocalDate lentAt;
    private final LocalDate dueDate;
    private final LocalDate returnedAt;
    private final boolean returned;

    public LendingResponse(Lending lending) {
        this.id = lending.getId();
        this.bookId = lending.getBookId();
        this.memberId = lending.getMemberId();
        this.lentAt = lending.getLentAt();
        this.dueDate = lending.getDueDate();
        this.returnedAt = lending.getReturnedAt();
        this.returned = lending.getReturnedAt() != null;
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

    public LocalDate getLentAt() {
        return lentAt;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnedAt() {
        return returnedAt;
    }

    public boolean isReturned() {
        return returned;
    }
}
