package com.example.training.common;

/**
 * 指定したものが見つからないときに投げる、アプリ共通の例外。
 *
 * <p>複数のエンティティ（書籍・利用者・貸出）で使い回すため、
 * 「何が」「どのID」で見つからなかったかをメッセージに持たせる汎用形にしている。
 * これを Service で投げ、{@link GlobalExceptionHandler} が受け取って
 * HTTP 404（Not Found）に変換する。
 *
 * <p>使用例: {@code throw new NotFoundException("書籍", id);}
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String what, Long id) {
        super(what + "が見つかりません: id=" + id);
    }
}
