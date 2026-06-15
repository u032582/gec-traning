package com.example.training.task;

/**
 * 指定したIDのタスクが見つからないときに投げる例外。
 *
 * <p>これを Service で投げ、{@link GlobalExceptionHandler} が受け取って
 * HTTP 404（Not Found）に変換する。
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("タスクが見つかりません: id=" + id);
    }
}
