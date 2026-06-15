package com.example.training.common;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * アプリ全体のControllerをまとめて見張る、共通のエラー処理係。
 *
 * <p>@RestControllerAdvice … どのControllerにも属さず、全体で起きた例外を横断的に捕まえる。
 * 各Controllerで毎回 try-catch を書く必要がなくなり、エラー処理が一箇所に集まる（重複が消える）。
 * Service が例外を投げるだけで、ここが自動で受け取り、整形したJSON＋ステータスに変換して返す。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 「見つからない」例外 → 404 + 整形JSON。 */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResourceNotFoundException e) {
        return new ErrorResponse(404, e.getMessage());
    }

    /**
     * 入力チェック(@Valid)違反 → 400 + 整形JSON（発展）。
     * どの項目がなぜダメだったかを1つの文字列にまとめて返す。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ErrorResponse(400, message);
    }
}
