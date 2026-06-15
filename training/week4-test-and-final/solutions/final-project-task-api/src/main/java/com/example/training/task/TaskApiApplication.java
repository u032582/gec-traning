package com.example.training.task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * タスク管理APIの起動クラス。
 *
 * <p>{@code main} を実行すると、Spring Boot が内蔵Webサーバ（標準で8080番ポート）を
 * 立ち上げてAPIを公開する。
 *
 * <p>{@code @MapperScan} … MyBatisのMapperインタフェースがどのパッケージにあるかを
 * Springに教える。これで TaskMapper が自動で使えるようになる。
 */
@SpringBootApplication
@MapperScan("com.example.training.task")
public class TaskApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApiApplication.class, args);
    }
}
