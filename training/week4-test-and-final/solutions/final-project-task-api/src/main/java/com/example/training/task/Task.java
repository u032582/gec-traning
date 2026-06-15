package com.example.training.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * tasks テーブルの1行に対応する入れ物（エンティティ）。
 *
 * <p>MyBatis が SQL の結果をこのクラスに詰めてくれる。
 * そのため、setter とデフォルトコンストラクタを用意している。
 */
public class Task {

    private Long id;
    private String title;
    private String description;
    private String status;     // "todo" / "doing" / "done" のいずれか
    private LocalDate dueDate;  // 締め切り日（任意。未設定なら null）
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
