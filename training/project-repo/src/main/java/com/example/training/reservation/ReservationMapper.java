package com.example.training.reservation;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * reservations テーブルに対するDBアクセス役（Mapper）。
 *
 * <p>実際のSQLは {@code src/main/resources/mapper/ReservationMapper.xml} に書く。
 * メソッド名と XML の id を一致させる。
 */
@Mapper
public interface ReservationMapper {

    // TODO: 予約を1件登録する（useGeneratedKeys で id を書き戻す）
     int insert(Reservation reservation);

    // TODO: id で1件取得。いなければ null
     Reservation findById(@Param("id") Long id);

    // TODO: ある利用者の予約一覧（新しい順）
     List<Reservation> findByMemberId(@Param("memberId") Long memberId);

    // TODO: 同じ本・同じ人で status=waiting の予約を探す（重複チェック用）
     Reservation findWaitingByBookIdAndMemberId(
             @Param("bookId") Long bookId,
             @Param("memberId") Long memberId);
}
