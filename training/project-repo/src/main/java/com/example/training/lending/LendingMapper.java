package com.example.training.lending;

import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * lendings テーブルに対するDBアクセス役（Mapper）。
 *
 * <p>実際のSQLは {@code src/main/resources/mapper/LendingMapper.xml} に書く。
 */
@Mapper
public interface LendingMapper {

    /**
     * 貸出記録を1件登録する。登録後、採番された id が引数 lending の id にセットされる
     * （XML側で useGeneratedKeys を使っているため）。
     */
    int insert(Lending lending);

    /** id を指定して1件取得。いなければ null。 */
    Lending findById(@Param("id") Long id);

    /** ある利用者の貸出履歴を、貸出日の新しい順で取得。 */
    List<Lending> findByMemberId(@Param("memberId") Long memberId);

    /**
     * 返却済みにする（returned_at に返却日を入れる）。
     * まだ返却されていない（returned_at IS NULL）ものだけを対象にする。
     * 更新できた件数を返す（0なら「すでに返却済み」を意味する）。
     */
    int markReturned(@Param("id") Long id, @Param("returnedAt") LocalDate returnedAt);

    int updateDueDate(@Param("id") Long id, @Param("dueDate") LocalDate dueDate);
}
