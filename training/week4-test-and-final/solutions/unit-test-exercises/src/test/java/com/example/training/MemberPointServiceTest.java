package com.example.training;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 課題1-2 模範解答: MemberPointService の単体テスト（Mockito使用）。
 *
 * <p>ポイント:
 * <ul>
 *   <li>{@code @ExtendWith(MockitoExtension.class)} … JUnit5でMockitoを使う宣言。</li>
 *   <li>{@code @Mock} … この依存をモック（ニセモノ）にする。ここでは MemberMapper。</li>
 *   <li>{@code @InjectMocks} … 上のモックを差し込んでテスト対象を組み立てる。</li>
 *   <li>{@code when(...).thenReturn(...)} … 「モックがこう呼ばれたらこう返す」を仕込む（スタブ）。</li>
 *   <li>{@code verify(...)} … 「モックの特定メソッドが呼ばれたか」を後から検証する。</li>
 * </ul>
 *
 * <p>用語メモ: <b>スタブ</b> … モックに「こう返してね」と決めた振る舞いのこと。
 */
@ExtendWith(MockitoExtension.class)
class MemberPointServiceTest {

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberPointService service;

    // ---- 正常系 ----------------------------------------------------------

    @Test
    @DisplayName("加算成功: 既存会員(100pt)に50pt加算 → 150ptを返し、updateが呼ばれる")
    void addPoint_success() {
        // 1. モックの振る舞いを仕込む: id=1 で会員(100pt)を返す
        when(memberMapper.findById(1)).thenReturn(new Member(1, "佐藤", 100));

        // 2. テスト対象を実行
        int result = service.addPoint(1, 50);

        // 3. 戻り値を確認
        assertEquals(150, result);

        // 4. Mapper.updatePoint が「id=1, 150pt」で1回呼ばれたか検証
        verify(memberMapper).updatePoint(1, 150);
    }

    // ---- 異常系: 会員が存在しない ---------------------------------------

    @Test
    @DisplayName("会員なし: findByIdがnull → 例外。updateは呼ばれない")
    void addPoint_memberNotFound() {
        // モックは null を返す（=会員が見つからない状況を再現）
        when(memberMapper.findById(99)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.addPoint(99, 50));

        // 更新処理まで進んでいないことを確認（never = 0回）
        verify(memberMapper, never()).updatePoint(org.mockito.ArgumentMatchers.anyInt(),
                org.mockito.ArgumentMatchers.anyInt());
    }

    // ---- 異常系: 加算ポイントが不正 -------------------------------------

    @Test
    @DisplayName("加算0以下: 例外。DBアクセスは一切しない")
    void addPoint_invalidAddPoint() {
        // 入力チェックで弾かれるはずなので、findById すら呼ばれない。
        // → when(...) でスタブを仕込む必要がない（仕込むと「未使用」と怒られる）。
        assertThrows(IllegalArgumentException.class, () -> service.addPoint(1, 0));

        verify(memberMapper, never()).findById(org.mockito.ArgumentMatchers.anyInt());
        verify(memberMapper, never()).updatePoint(org.mockito.ArgumentMatchers.anyInt(),
                org.mockito.ArgumentMatchers.anyInt());
    }
}
