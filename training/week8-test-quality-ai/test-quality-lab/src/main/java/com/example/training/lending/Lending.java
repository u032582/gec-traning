package com.example.training.lending;

import java.time.LocalDate;

/**
 * lendings テーブルの1行に対応する入れ物（エンティティ）。
 *
 * <p>「誰が・どの本を・いつ借りて・いつ返したか」の記録。
 * {@code returnedAt} が null なら「まだ貸出中」を意味する。
 *
 * <p>MyBatis が SQL の結果をこのクラスに詰めてくれる。
 */
public class Lending {

    private Long id;
    private Long bookId;
    private Long memberId;
    private LocalDate lentAt;
    private LocalDate dueDate;
    private LocalDate returnedAt;  // null なら貸出中

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

    public LocalDate getLentAt() {
        return lentAt;
    }

    public void setLentAt(LocalDate lentAt) {
        this.lentAt = lentAt;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDate returnedAt) {
        this.returnedAt = returnedAt;
    }
}
