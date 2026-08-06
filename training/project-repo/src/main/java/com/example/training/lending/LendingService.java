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
 * <p>貸出も返却も「2つのテーブルを整合させながら」更新する:
 * <ul>
 *   <li>貸出: {@code lendings} に1件足す ＋ {@code books.available_count} を1減らす</li>
 *   <li>返却: {@code lendings} を返却済みにする ＋ {@code books.available_count} を1戻す</li>
 * </ul>
 * この2つの更新は「両方成功」か「両方失敗」でなければ在庫が狂う。
 * だから {@code @Transactional} で1つのまとまり（トランザクション）にしている。
 *
 * <p>返却期限は貸出日の2週間後に固定している（この題材の簡易ルール）。
 */
@Service
public class LendingService {

    /** 返却期限は貸出日から何日後か（この題材の簡易ルール）。 */
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
     * <p>手順: ①借り主が実在するか確認 → ②在庫を1減らす（在庫0なら失敗）→
     * ③貸出記録を作る。②で減らせなかったら在庫切れなので {@link BusinessRuleException}（→409）。
     *
     * @param today 貸出日（＝今日）。呼び出し側から渡す。
     */
    @Transactional
    public Lending lend(LendingRequest request, LocalDate today) {
        // ① 借り主が実在するか（いなければ404）。書籍の存在は次の在庫更新で兼ねて確認する。
        memberService.findById(request.getMemberId());

        // ② 在庫を1減らす。在庫が残っていなければ更新件数0が返る＝貸せない。
        int decremented = bookMapper.decrementAvailable(request.getBookId());
        if (decremented == 0) {
            // 在庫0か、そもそも本が存在しないか。どちらかを見分けて適切なエラーにする。
            if (bookMapper.findById(request.getBookId()) == null) {
                throw new NotFoundException("書籍", request.getBookId());
            }
            throw new BusinessRuleException(
                    "貸出できません（在庫がありません）: bookId=" + request.getBookId());
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
     * <p>手順: ①貸出記録が実在するか確認 → ②返却済みにする（すでに返却済みなら失敗）→
     * ③在庫を1戻す。②で更新できなかったら二重返却なので {@link BusinessRuleException}（→409）。
     *
     * @param today 返却日（＝今日）。
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

        // ③ その本の在庫を1戻す。
        bookMapper.incrementAvailable(lending.getBookId());

        // 返却後の最新状態を返す。
        return lendingMapper.findById(lendingId);
    }

    /**
     * ある利用者の貸出履歴を取得。利用者が実在しなければ {@link NotFoundException}（→404）。
     */
    public List<Lending> findByMember(Long memberId) {
        memberService.findById(memberId);  // 実在チェック（いなければ404）
        return lendingMapper.findByMemberId(memberId);
    }

    public List<Lending> findOverdue(LocalDate today){
        return lendingMapper.findOverdue(today);
    }
}
