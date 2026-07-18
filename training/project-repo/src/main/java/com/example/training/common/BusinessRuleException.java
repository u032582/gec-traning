package com.example.training.common;

/**
 * 業務ルールに反する操作をしようとしたときに投げる例外。
 *
 * <p>「在庫0の本を貸そうとした」「返却済みの貸出をもう一度返そうとした」など、
 * 入力の形式は正しいが業務上できないケースで使う。
 * {@link GlobalExceptionHandler} が受け取って HTTP 409（Conflict）に変換する。
 *
 * <p>※ 貸出・返却（lending）の実装で本格的に使う。書籍取得だけの現段階では
 * まだ投げる箇所は無いが、共通の置き場所として先に用意しておく。
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
