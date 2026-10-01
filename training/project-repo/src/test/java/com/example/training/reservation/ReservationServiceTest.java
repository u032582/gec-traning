package com.example.training.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.training.book.BookService;
import com.example.training.member.MemberService;
import com.example.training.member.Member;
import com.example.training.book.Book;
import com.example.training.common.BusinessRuleException;
import com.example.training.common.NotFoundException;
import java.time.LocalDate;

/**
 * {@link ReservationService} の単体テスト。
 *
 * <p>お手本: {@link com.example.training.lending.LendingServiceTest}
 * 最低限確かめたいこと:
 * <ul>
 *   <li>予約成功 → waiting で insert される</li>
 *   <li>同じ本・同じ人の waiting が既にある → BusinessRuleException、insert しない</li>
 *   <li>本が無い → NotFoundException</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private BookService bookService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private ReservationService reservationService;
    private static final LocalDate TODAY =LocalDate.of(2026, 9, 29);

    @Test
    void 予約成功_予約記録が作られる() {
        ReservationRequest request = new ReservationRequest();
        request.setBookId(1L);
        request.setMemberId(1L);

        when(memberService.findById(1L)).thenReturn(new Member());
        when(bookService.findById(1L)).thenReturn(new Book());
        when(reservationMapper.findWaitingByBookIdAndMemberId(1L, 1L)).thenReturn(null);
        
        Reservation result =reservationService.reserve(request, TODAY);

        assertThat(result.getStatus()).isEqualTo(reservationService.STATUS_WAITING);

        verify(reservationMapper).insert(any(Reservation.class));
    }

    @Test
    void 予約失敗_同じ本を同じ人が二重予約できない() {
        ReservationRequest request = new ReservationRequest();
        request.setBookId(1L);
        request.setMemberId(1L);

        when(memberService.findById(1L)).thenReturn(new Member());
        when(bookService.findById(1L)).thenReturn(new Book());
        when(reservationMapper.findWaitingByBookIdAndMemberId(1L, 1L)).thenReturn(new Reservation());

        assertThatThrownBy(() -> reservationService.reserve(request, TODAY)).isInstanceOf(BusinessRuleException.class);

        verify(reservationMapper,never()).insert(any(Reservation.class));
    }

    @Test
    void 予約失敗_本が無ければ例外で予約記録は作られない() {
        ReservationRequest request = new ReservationRequest();
        request.setBookId(9999L);

        when(bookService.findById(9999L)).thenThrow(new NotFoundException("書籍", 9999L));
        
        assertThatThrownBy(() -> reservationService.reserve(request, TODAY)).isInstanceOf(NotFoundException.class);

        verify(reservationMapper, never()).insert(any(Reservation.class));
    }

    @Test
    void 予約失敗_利用者が無ければ例外で予約記録は作られない() {
        ReservationRequest request = new ReservationRequest();
        request.setBookId(1L);
        request.setMemberId(9999L);

        when(bookService.findById(1L)).thenReturn(new Book());
        when(memberService.findById(9999L)).thenThrow(new NotFoundException("利用者", 9999L));

        assertThatThrownBy(() -> reservationService.reserve(request, TODAY)).isInstanceOf(NotFoundException.class);

        verify(reservationMapper, never()).insert(any(Reservation.class));
    }
}