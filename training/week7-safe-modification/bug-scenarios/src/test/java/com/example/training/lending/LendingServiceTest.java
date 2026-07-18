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
 * {@link LendingService} の単体テスト（週7・バグ修正の判定用）。
 *
 * <p>このテストは「こう動くべき」という仕様を表しています。
 * いま LendingService にはバグがあるので、<b>いくつかのテストが落ちます</b>。
 * バグを直すと、落ちていたテストが緑になります。
 *
 * <p>⚠️ <b>テストは変更しないでください</b>。直すのは {@code LendingService.java} の方です。
 * テストを書き換えて緑にするのは「体温計を割って熱が下がったことにする」のと同じです。
 *
 * <p>ヒント: どのテストが・何を期待して落ちているかを読むと、バグの場所が分かります。
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

    // =====================================================================
    // これらは「もともと通る」テスト。直すときに壊してはいけない（リグレッション確認）。
    // =====================================================================

    @Test
    void 貸出成功_在庫が減り貸出記録が作られる() {
        LendingRequest request = new LendingRequest();
        request.setBookId(1L);
        request.setMemberId(1L);
        when(memberService.findById(1L)).thenReturn(new Member());
        when(bookMapper.decrementAvailable(1L)).thenReturn(1);  // 在庫を減らせた

        Lending result = lendingService.lend(request, TODAY);

        verify(bookMapper).decrementAvailable(1L);
        verify(lendingMapper).insert(any(Lending.class));
        assertThat(result.getDueDate()).isEqualTo(TODAY.plusDays(14));
    }

    @Test
    void 返却成功_返却済みにする処理が呼ばれる() {
        Lending lent = new Lending();
        lent.setId(10L);
        lent.setBookId(1L);
        when(lendingMapper.findById(10L)).thenReturn(lent);
        when(lendingMapper.markReturned(eq(10L), any(LocalDate.class))).thenReturn(1); // 返却できた

        lendingService.returnBook(10L, TODAY);

        verify(lendingMapper).markReturned(eq(10L), any(LocalDate.class));
    }

    @Test
    void 返却失敗_二重返却は例外() {
        Lending lent = new Lending();
        lent.setId(10L);
        lent.setBookId(1L);
        when(lendingMapper.findById(10L)).thenReturn(lent);
        when(lendingMapper.markReturned(eq(10L), any(LocalDate.class))).thenReturn(0); // 二重返却

        assertThatThrownBy(() -> lendingService.returnBook(10L, TODAY))
                .isInstanceOf(BusinessRuleException.class);
    }

    // =====================================================================
    // これらは「いまバグで落ちている」テスト。直すと緑になる。
    // =====================================================================

    /** bug-1: 在庫切れ（在庫0）なら、貸出記録を作ってはいけない。 */
    @Test
    void 貸出_在庫切れなら貸出記録は作られない() {
        LendingRequest request = new LendingRequest();
        request.setBookId(3L);
        request.setMemberId(1L);
        when(memberService.findById(1L)).thenReturn(new Member());
        when(bookMapper.decrementAvailable(3L)).thenReturn(0);  // 在庫0で減らせない
        when(bookMapper.findById(3L)).thenReturn(new Book());   // 本自体は存在する（＝在庫切れ）

        // 在庫切れなので、業務ルール違反の例外が投げられるべき。
        assertThatThrownBy(() -> lendingService.lend(request, TODAY))
                .isInstanceOf(BusinessRuleException.class);

        // そして貸出記録は作られていないべき（在庫が無いのに記録だけできてはいけない）。
        verify(lendingMapper, never()).insert(any(Lending.class));
    }

    /** bug-2: 返却したら、その本の在庫（available_count）を1戻すべき。 */
    @Test
    void 返却_在庫が1戻る() {
        Lending lent = new Lending();
        lent.setId(10L);
        lent.setBookId(1L);
        when(lendingMapper.findById(10L)).thenReturn(lent);
        when(lendingMapper.markReturned(eq(10L), any(LocalDate.class))).thenReturn(1); // 返却できた

        lendingService.returnBook(10L, TODAY);

        // 返した本（bookId=1）の在庫を1戻す処理が呼ばれるべき。
        verify(bookMapper).incrementAvailable(1L);
    }
}
