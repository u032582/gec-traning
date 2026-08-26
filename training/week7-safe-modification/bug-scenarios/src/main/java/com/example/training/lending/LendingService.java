package com.example.training.lending;

import com.example.training.book.BookMapper;
import com.example.training.common.BusinessRuleException;
import com.example.training.common.NotFoundException;
import com.example.training.member.MemberService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 貸出・返却の判断・処理を担当する Service。**このアプリの核**。
 *
 * <p>⚠️【週7 練習用】このファイルには <b>わざとバグが仕込んであります</b>。
 * 落ちているテスト（{@code ./gradlew test}）を手がかりに、バグを見つけて直してください。
 * 直し方の答えは書いてありません。テストが「何を期待して落ちているか」を読んで直します。
 *
 * <p>※ 正しい実装は題材リポ（../../project-repo）の同名ファイルにあります。ただし
 * <b>先に自分で直してから</b>見比べること（週7の狙いは「自分で影響範囲を見て直す」ことなので）。
 */
@Service
public class LendingService {

    private static final int LENDING_PERIOD_DAYS = 14;

    private final LendingMapper lendingMapper;
    private final BookMapper bookMapper;
    private final MemberService memberService;

    public LendingService(LendingMapper lendingMapper,
                          BookMapper bookMapper,
                          MemberService memberService) {
        this.lendingMapper = lendingMapper;
        this.bookMapper = bookMapper;
        this.memberService = memberService;
    }

    /**
     * 本を貸し出す。
     *
     * <p>本来の仕様: ①借り主が実在するか確認 → ②在庫を1減らす（在庫0なら貸せない）→
     * ③貸出記録を作る。在庫が減らせなかったら在庫切れなので例外を投げ、記録は作らない。
     */
    @Transactional
    public Lending lend(LendingRequest request, LocalDate today) {
        // ① 借り主が実在するか（いなければ404）。
        memberService.findById(request.getMemberId());

        // ② 在庫を1減らす。在庫が残っていなければ更新件数0が返る＝貸せない。
        int decremented = bookMapper.decrementAvailable(request.getBookId());

        if (decremented == 0) {
            if (bookMapper.findById(request.getBookId()) == null) {
                throw new NotFoundException("書籍", request.getBookId());
            }
            throw new BusinessRuleException(
                "貸出できません（在庫がありません)：bookId =" + request.getBookId());
        }
      

        // ③ 貸出記録を作る（貸出日＝今日、返却期限＝2週間後、返却日は未設定＝貸出中）。
        Lending lending = new Lending();
        lending.setBookId(request.getBookId());
        lending.setMemberId(request.getMemberId());
        lending.setLentAt(today);
        lending.setDueDate(today.plusDays(LENDING_PERIOD_DAYS));
        lending.setReturnedAt(null);

        lendingMapper.insert(lending);
        return lending;
    }

    /**
     * 本を返却する。
     *
     * <p>本来の仕様: ①貸出記録が実在するか確認 → ②返却済みにする（二重返却なら弾く）→
     * ③在庫を1戻す。
     */
    @Transactional
    public Lending returnBook(Long lendingId, LocalDate today) {
        // ① 貸出記録が実在するか（いなければ404）。
        Lending lending = lendingMapper.findById(lendingId);
        if (lending == null) {
            throw new NotFoundException("貸出記録", lendingId);
        }

        // ② 返却済みにする。すでに返却済みなら更新件数0＝二重返却。
        int marked = lendingMapper.markReturned(lendingId, today);
        if (marked == 0) {
            throw new BusinessRuleException(
                    "返却できません（すでに返却済みです）: lendingId=" + lendingId);
        }


        // 返却後の最新状態を返す。
        bookMapper.incrementAvailable(lending.getBookId());
        return lendingMapper.findById(lendingId);
    }

    /**
     * ある利用者の貸出履歴を取得。利用者が実在しなければ {@link NotFoundException}（→404）。
     */
    public List<Lending> findByMember(Long memberId) {
        memberService.findById(memberId);
        return lendingMapper.findByMemberId(memberId);
    }
}
