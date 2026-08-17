package com.example.training.book;

import com.example.training.common.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 書籍に関する判断・処理を担当する Service。
 *
 * <p>3層構造の真ん中。Controller（窓口）から呼ばれ、必要に応じて
 * {@link BookMapper}（DB出し入れ）を使う。
 *
 * <p>「存在しないIDなら例外を投げる」といった業務ルールはここに集約する。
 */
@Service
public class BookService {

    private final BookMapper bookMapper;

    // コンストラクタインジェクション（テスト時にモックを差し込みやすい形）。
    public BookService(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    /**
     * 一覧取得。category が指定されていればその分類だけ、null なら全件。
     */
    public List<Book> findAll(String category) {
        return bookMapper.findAll(category);
    }

    public List<CategoryStatsResponse> countByCategory(){
        return bookMapper.countByCategory();
    }

    /**
     * 1件取得。見つからなければ {@link NotFoundException}（→404）。
     */
    public Book findById(Long id) {
        Book book = bookMapper.findById(id);
        if (book == null) {
            throw new NotFoundException("書籍", id);
        }
        return book;
    }
}
