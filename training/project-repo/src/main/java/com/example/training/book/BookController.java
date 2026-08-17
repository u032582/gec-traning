package com.example.training.book;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 書籍APIの窓口（Controller）。
 *
 * <p>URLとHTTPメソッドを、Serviceの処理に振り分けるのが役目。
 * 業務ロジックはここに書かず {@link BookService} に任せる（3層構造）。
 * 外に返すときは、エンティティ {@link Book} をそのまま返さず
 * {@link BookResponse}（DTO）に詰め替える。
 *
 * <p>ベースパスは {@code /api/books}。
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * 一覧取得: GET /api/books → 200 OK + 書籍配列。
     * {@code ?category=技術書} を付けると、その分類だけに絞り込む（任意）。
     */
    @GetMapping
    public List<BookResponse> list(@RequestParam(required = false) String category) {
        return bookService.findAll(category).stream()
                .map(BookResponse::new)
                .toList();
    }

    @GetMapping("/stats/by-category")
    public List<CategoryStatsResponse> statsByCategory(){
        return bookService.countByCategory();
    }

    /** 単体取得: GET /api/books/{id} → 200 OK（無ければ404）。 */
    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        return new BookResponse(bookService.findById(id));
    }
}
