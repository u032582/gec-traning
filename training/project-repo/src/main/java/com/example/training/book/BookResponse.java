package com.example.training.book;

/**
 * 書籍情報を外（APIの利用者）に返すための入れ物（DTO）。
 *
 * <p>エンティティ {@link Book} をそのまま返さず、外向けの形に詰め替える。
 * こうしておくと、DBの列を増やしても、外に見せる形を独立して選べる
 * （3層構造で「入れ物を分ける」意図。週3で学んだDTOと同じ考え方）。
 *
 * <p>この題材では {@code created_at} は外に見せていない（利用者に不要なため）。
 */
public class BookResponse {

    private final Long id;
    private final String title;
    private final String author;
    private final String category;
    private final Integer totalCount;
    private final Integer availableCount;

    public BookResponse(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.category = book.getCategory();
        this.totalCount = book.getTotalCount();
        this.availableCount = book.getAvailableCount();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public Integer getAvailableCount() {
        return availableCount;
    }
}
