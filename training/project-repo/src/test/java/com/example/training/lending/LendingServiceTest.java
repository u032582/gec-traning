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
import com.example.training.member.Member;
import com.example.training.member.MemberService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * {@link LendingService} の主要な分岐の単体テスト。
 *
 * <p>DBには触れず、Mapper と MemberService をモックにして、
 * 「Serviceの判断（在庫を減らす／二重返却を弾く 等）」だけを確かめる。
 * 週4のMockito課題と同じ形。
 *
 * <p>⚠️ これは「最低限」のテストです。<b>わざと手薄に</b>してあります
 * （正常な返却の流れ、貸出履歴、404系、返却で在庫が戻ること 等は未カバー）。
 * この不足を、週8「テストと品質をAIで担保する」でAIと協働して埋めます。
 */
@ExtendWith(MockitoExtension.class)
class LendingServiceTest {

    @Mock
    private LendingMapper lendingMapper;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private LendingService lendingService;

    private static final LocalDate TODAY = LocalDate.of(2026, 7, 18);

    @Test
    void 貸出成功_在庫が減り貸出記録が作られる() {
        LendingRequest request = new LendingRequest();
        request.setBookId(1L);
        request.setMemberId(1L);

        when(memberService.findById(1L)).thenReturn(new Member());
        when(bookMapper.decrementAvailable(1L)).thenReturn(1);  // 在庫を減らせた

        Lending result = lendingService.lend(request, TODAY);

        // 在庫を1減らし、貸出記録をinsertしたことを確認。
        verify(bookMapper).decrementAvailable(1L);
        verify(lendingMapper).insert(any(Lending.class));
        // 返却期限は貸出日の2週間後になっているか。
        assertThat(result.getLentAt()).isEqualTo(TODAY);
        assertThat(result.getDueDate()).isEqualTo(TODAY.plusDays(14));
        assertThat(result.getReturnedAt()).isNull();  // 貸出中
    }

    @Test
    void 貸出失敗_在庫が無いと例外で貸出記録は作られない() {
        LendingRequest request = new LendingRequest();
        request.setBookId(3L);
        request.setMemberId(1L);

        when(memberService.findById(1L)).thenReturn(new Member());
        when(bookMapper.decrementAvailable(3L)).thenReturn(0);   // 在庫0で減らせない
        when(bookMapper.findById(3L)).thenReturn(new Book());    // 本自体は存在する（＝在庫切れ）

        assertThatThrownBy(() -> lendingService.lend(request, TODAY))
                .isInstanceOf(BusinessRuleException.class);

        // 在庫切れなら、貸出記録は作られていないこと。
        verify(lendingMapper, never()).insert(any(Lending.class));
    }

    @Test
    void 返却失敗_すでに返却済みなら例外で在庫は戻さない() {
        Lending alreadyLent = new Lending();
        alreadyLent.setId(10L);
        alreadyLent.setBookId(1L);

        when(lendingMapper.findById(10L)).thenReturn(alreadyLent);
        when(lendingMapper.markReturned(eq(10L), any(LocalDate.class))).thenReturn(0); // 二重返却

        assertThatThrownBy(() -> lendingService.returnBook(10L, TODAY))
                .isInstanceOf(BusinessRuleException.class);

        // 二重返却をはじいたので、在庫を戻す処理は呼ばれないこと。
        verify(bookMapper, never()).incrementAvailable(any());
    }

    @Test
    void 貸出失敗_未返却が5冊なら上限到達で断られ在庫も記録も触らない() {
        LendingRequest request = new LendingRequest();
        request.setBookId(3L);
        request.setMemberId(1L);

        when(lendingMapper.countUnreturnedByMemberId(1L)).thenReturn(5);
        when(memberService.findById(1L)).thenReturn(new Member());

        assertThatThrownBy(() -> lendingService.lend(request, TODAY))
                .isInstanceOf(BusinessRuleException.class);

        verify(lendingMapper, never()).insert(any());
        verify(bookMapper, never()).decrementAvailable(any());

    }

    @Test
    void 貸出成功_未返却が4冊なら上限内で借りられる() {
        LendingRequest request = new LendingRequest();
        request.setBookId(3L);
        request.setMemberId(1L);

        when(memberService.findById(1L)).thenReturn(new Member());
        when(bookMapper.decrementAvailable(3L)).thenReturn(1);
        when(lendingMapper.countUnreturnedByMemberId(1L)).thenReturn(4);

        Lending result = lendingService.lend(request, TODAY);

        verify(bookMapper).decrementAvailable(3L);
        verify(lendingMapper).insert(any());

        assertThat(result.getLentAt()).isEqualTo(TODAY);
        assertThat(result.getDueDate()).isEqualTo(TODAY.plusDays(14));
        assertThat(result.getReturnedAt()).isNull();
    }
}
