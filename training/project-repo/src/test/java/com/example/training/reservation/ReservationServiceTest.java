package com.example.training.reservation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    // TODO: BookService / MemberService の @Mock を足す

    @InjectMocks
    private ReservationService reservationService;

    // TODO: テストメソッドを書く
}
