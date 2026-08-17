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

    List<CategoryStatsResponse> countByCategory();

    /** id を指定して1件取得。いなければ null。 */
    Book findById(@Param("id") Long id);

    /**
     * 貸出可能数を1減らす（貸出時に呼ぶ）。
     * ただし在庫が残っている（available_count > 0）ときだけ減らす。
     * 更新できた件数を返す（0なら「在庫が無くて減らせなかった」を意味する）。
     */
    int decrementAvailable(@Param("id") Long id);

    /**
     * 貸出可能数を1増やす（返却時に呼ぶ）。
     * ただし総数を超えない（available_count < total_count）ときだけ増やす。
     * 更新できた件数を返す（0なら「これ以上増やせない＝整合が崩れている」を意味する）。
     */
    int incrementAvailable(@Param("id") Long id);
}
