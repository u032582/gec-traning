package com.example.training.task;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * アプリ全体で発生した例外を、適切なHTTPレスポンスに変換する場所。
 *
 * <p>各Controllerに try-catch を散らかさず、ここに集約する。これが
 * Spring Boot の「例外ハンドリング」の定番。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** タスクが見つからない → 404 Not Found */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(TaskNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /**
     * 入力チェック違反（@Valid 失敗）→ 400 Bad Request。
     * どの項目がなぜダメだったかを "errors" にまとめて返す。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, "入力内容に誤りがあります");
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() == null
                                ? "不正な値です"
                                : fieldError.getDefaultMessage(),
                        (a, b) -> a)); // 同じ項目に複数エラーが出たら先勝ち
        body.put("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(baseBody(status, message));
    }

    private Map<String, Object> baseBody(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return body;
    }
}
