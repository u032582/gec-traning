package com.example.training.book;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * books テーブルに対するDBアクセス役（Mapper）。
 *
 * <p>実際のSQLは {@code src/main/resources/mapper/BookMapper.xml} に書く。
 * このインタフェースのメソッド名とXMLの id を一致させることで、MyBatisが
 * 両者をつないでくれる。
 */
@Mapper
public interface BookMapper {

    /**
     * 書籍を一覧取得（id昇順）。
     *
     * @param category 分類で絞り込む。null のときは全件返す（XML側で分岐）。
     */
    List<Book> findAll(@Param("category") String category);

    /** id を指定して1件取得。いなければ null。 */
    Book findById(@Param("id") Long id);
}
