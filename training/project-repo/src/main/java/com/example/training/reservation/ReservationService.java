package com.example.training.reservation;

import com.example.training.book.BookService;
import com.example.training.member.MemberService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.training.common.BusinessRuleException;

/**
 * 予約の判断・処理を担当する Service。
 *
 * <p>設計判断（実装時に守ること）:
 * <ul>
 *   <li>同じ本・同じ人で waiting が既にあれば 409</li>
 *   <li>在庫の有無は問わない（予約を許す）</li>
 *   <li>status は waiting / canceled</li>
 * </ul>
 *
 * <p>お手本: {@link com.example.training.lending.LendingService}
 */
@Service
public class ReservationService {

    public static final String STATUS_WAITING = "waiting";
    public static final String STATUS_CANCELED = "canceled";

    private final ReservationMapper reservationMapper;
    private final BookService bookService;
    private final MemberService memberService;

    public ReservationService(ReservationMapper reservationMapper,
                              BookService bookService,
                              MemberService memberService) {
        this.reservationMapper = reservationMapper;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    /**
     * 本を予約する。
     *
     * <p>手順のヒント: ①本と利用者の実在確認 → ②waiting の重複チェック → ③insert
     */
    public Reservation reserve(ReservationRequest request, LocalDate today) {
        bookService.findById(request.getBookId());
        memberService.findById(request.getMemberId());
        Reservation existingReservation = reservationMapper.findWaitingByBookIdAndMemberId(request.getBookId(), request.getMemberId());
        if (existingReservation != null) {
            throw new BusinessRuleException("既に予約されています");
        }

        Reservation reservation = new Reservation();
        reservation.setBookId(request.getBookId());
        reservation.setMemberId(request.getMemberId());
        reservation.setReservedAt(today);
        reservation.setStatus(STATUS_WAITING);
        reservationMapper.insert(reservation);
        return reservation;
    }

    /**
     * ある利用者の予約一覧。利用者がいなければ 404。
     */
    public List<Reservation> findByMember(Long memberId) {
        memberService.findById(memberId);
        return reservationMapper.findByMemberId(memberId);
    }
}
