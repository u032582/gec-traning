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

     int insert(Reservation reservation);

     Reservation findById(@Param("id") Long id);

     List<Reservation> findByMemberId(@Param("memberId") Long memberId);

     Reservation findWaitingByBookIdAndMemberId(
             @Param("bookId") Long bookId,
             @Param("memberId") Long memberId);
}
