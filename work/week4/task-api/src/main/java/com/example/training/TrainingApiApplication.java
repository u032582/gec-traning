package com.example.training;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * アプリの入口（メインクラス）。
 *
 * <p>{@code @SpringBootApplication} … 「ここからSpring Bootのアプリを起動する」という目印（アノテーション）。
 * これ1つで、設定の自動読み込みや、部品（Bean）の自動登録など、たくさんの準備をまとめてやってくれる。
 *
 * <p>{@code @MapperScan} … MyBatisの「Mapper」（DBアクセス用のインターフェース）を、
 * このパッケージ以下から自動で探して使えるようにする指定。
 * 用語メモ: アノテーション … コードに付ける「目印」。@で始まる。Springはこの目印を見て動きを決める。
 */
@SpringBootApplication
@MapperScan("com.example.training")
public class TrainingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingApiApplication.class, args);
    }
}
