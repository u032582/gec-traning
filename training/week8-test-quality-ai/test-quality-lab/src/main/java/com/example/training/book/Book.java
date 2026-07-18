package com.example.training.book;

import java.time.LocalDateTime;

/**
 * books テーブルの1行に対応する入れ物（エンティティ）。
 *
 * <p>MyBatis が SQL の結果をこのクラスに詰めてくれる。
 * そのため、setter とデフォルトコンストラクタ（暗黙）を用意している。
 */
public class Book {

    private Long id;
    private String title;
    private String author;
    private String category;
    private Integer totalCount;      // 蔵書数（全部で何冊か）
    private Integer availableCount;  // いま貸出可能な残冊数（貸出で減り返却で増える）
    private LocalDateTime createdAt;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Integer availableCount) {
        this.availableCount = availableCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
