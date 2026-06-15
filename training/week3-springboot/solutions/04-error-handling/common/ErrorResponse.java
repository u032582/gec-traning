package com.example.training.common;

/**
 * エラー時に返すJSONの形（DTO）。
 * 例: {"status":404,"message":"customer not found: id=999"}
 * すべてのエラーをこの形に統一しておくと、APIを使う側（フロント等）が処理しやすい。
 */
public record ErrorResponse(int status, String message) {}
