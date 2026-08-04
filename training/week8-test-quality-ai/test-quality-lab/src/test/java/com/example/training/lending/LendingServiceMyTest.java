package com.example.training.lending;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.training.book.Book;
import com.example.training.book.BookMapper;
import com.example.training.common.BusinessRuleException;
import com.example.training.common.NotFoundException;
import com.example.training.member.Member;
import com.example.training.member.MemberService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 課題02: AI候補のうち「採用」したテスト＋自分で補った抜け。
 * 採用: ①正常貸出 / ②在庫切れ / ③借り主不在
 * 却下: ④isNotNullだけ / ⑤呼ぶだけ / ⑥正常系の重複
 * 補足: 返却で在庫が戻る／二重返却（週7の抜け）
 */
@ExtendWith(MockitoExtension.class)
class LendingServiceMyTest {

    @Mock
    LendingMapper lendingMapper;
    @Mock
    BookMapper bookMapper;
    @Mock
    MemberService memberService;

    @InjectMocks
    LendingService lendingService;

    private LendingRequest request;
    private final LocalDate today = LocalDate.of(2026, 8, 4);

    @BeforeEach
    void setUp() {
        request = new LendingRequest();
        request.setBookId(10L);
        request.setMemberId(20L);
    }

    /** ① 正常な貸出: 期限が+14日、在庫減らしと insert が呼ばれる */
    @Test
    void 正常な貸出では期限が14日後で在庫減らしと記録作成が呼ばれる() {
        Member member = new Member();
        member.setId(20L);
        when(memberService.findById(20L)).thenReturn(member);
        when(bookMapper.decrementAvailable(10L)).thenReturn(1);

        Lending result = lendingService.lend(request, today);

        assertThat(result.getBookId()).isEqualTo(10L);
        assertThat(result.getMemberId()).isEqualTo(20L);
        assertThat(result.getLentAt()).isEqualTo(today);
        assertThat(result.getDueDate()).isEqualTo(today.plusDays(14));
        assertThat(result.getReturnedAt()).isNull();

        verify(bookMapper).decrementAvailable(10L);
        ArgumentCaptor<Lending> captor = ArgumentCaptor.forClass(Lending.class);
        verify(lendingMapper).insert(captor.capture());
        assertThat(captor.getValue().getDueDate()).isEqualTo(today.plusDays(14));
    }

    /** ② 在庫切れ（本はある）: 409系例外、insert は呼ばれない */
    @Test
    void 在庫切れならBusinessRuleExceptionで記録を作らない() {
        Member member = new Member();
        member.setId(20L);
        when(memberService.findById(20L)).thenReturn(member);
        when(bookMapper.decrementAvailable(10L)).thenReturn(0);
        Book book = new Book();
        book.setId(10L);
        when(bookMapper.findById(10L)).thenReturn(book);

        assertThatThrownBy(() -> lendingService.lend(request, today))
                .isInstanceOf(BusinessRuleException.class);

        verify(lendingMapper, never()).insert(any());
    }

    /** ③ 借り主がいない: 404、在庫減らしに進まない */
    @Test
    void 借り主がいないときNotFoundExceptionで在庫を減らさない() {
        when(memberService.findById(20L))
                .thenThrow(new NotFoundException("利用者", 20L));

        assertThatThrownBy(() -> lendingService.lend(request, today))
                .isInstanceOf(NotFoundException.class);

        verify(bookMapper, never()).decrementAvailable(eq(10L));
        verify(lendingMapper, never()).insert(any());
    }

    /** 抜け補足: 返却の正常系 — 在庫が戻る（週7で直したこと） */
    @Test
    void 返却の正常系では在庫を戻す() {
        Long lendingId = 100L;
        Lending existing = new Lending();
        existing.setId(lendingId);
        existing.setBookId(10L);
        existing.setMemberId(20L);
        existing.setReturnedAt(null);

        Lending afterReturn = new Lending();
        afterReturn.setId(lendingId);
        afterReturn.setBookId(10L);
        afterReturn.setMemberId(20L);
        afterReturn.setReturnedAt(today);

        when(lendingMapper.findById(lendingId))
                .thenReturn(existing)   // ①存在確認
                .thenReturn(afterReturn); // 返却後の再取得
        when(lendingMapper.markReturned(lendingId, today)).thenReturn(1);

        Lending result = lendingService.returnBook(lendingId, today);

        assertThat(result.getReturnedAt()).isEqualTo(today);
        verify(bookMapper).incrementAvailable(10L);
    }

    /** 抜け補足: 二重返却 — markReturned が 0 ならエラー、在庫は戻さない */
    @Test
    void 二重返却ならBusinessRuleExceptionで在庫を戻さない() {
        Long lendingId = 100L;
        Lending existing = new Lending();
        existing.setId(lendingId);
        existing.setBookId(10L);
        when(lendingMapper.findById(lendingId)).thenReturn(existing);
        when(lendingMapper.markReturned(lendingId, today)).thenReturn(0);

        assertThatThrownBy(() -> lendingService.returnBook(lendingId, today))
                .isInstanceOf(BusinessRuleException.class);

        verify(bookMapper, never()).incrementAvailable(any());
    }
}
