package com.example.training.common;

/**
 * 「探したものが見つからなかった」ことを表す自作の例外。
 *
 * <p>RuntimeException を継承しているので、メソッドに throws を書かずに投げられる。
 * メッセージ（何が無かったか）をコンストラクタで受け取り、親に渡しておくと、
 * 共通ハンドラ側で getMessage() として取り出せる。
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
