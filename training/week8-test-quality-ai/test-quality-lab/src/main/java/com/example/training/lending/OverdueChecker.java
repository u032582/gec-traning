package com.example.training.lending;

import java.time.LocalDate;

/**
 * 貸出が「延滞（返却期限を過ぎて、まだ返していない）」かどうかを判定する部品。
 *
 * <p>⚠️【週8 練習用】このクラスには <b>silent failure（静かな失敗）</b> の匂いが
 * わざと仕込んであります。週8の課題で「このコードに潜む危うさ」を見抜き、
 * それを暴くテストを書きます。
 */
public class OverdueChecker {

    /**
     * 指定日時点で、その貸出が延滞しているか。
     *
     * <p>延滞の条件: まだ返却していない（returnedAt == null）かつ 返却期限（dueDate）が
     * 基準日（asOf）より前。
     *
     * @param lending 判定対象の貸出
     * @param asOf    基準日（「今日」を渡す想定）
     * @return 延滞していれば true
     */
    public boolean isOverdue(Lending lending, LocalDate asOf) {
        // すでに返却済みなら延滞ではない。
        if (lending.getReturnedAt() != null) {
            return false;
        }
        // 異常データは握りつぶさず、呼び出し側に気づかせる。
        if (lending.getDueDate() == null) {
            throw new IllegalArgumentException("dueDate must not be null");
        }
        // 返却期限が基準日より前なら延滞。
        return lending.getDueDate().isBefore(asOf);
    }
}
