package com.example.training;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 図書貸出管理APIの起動クラス。
 *
 * <p>{@code main} を実行すると、Spring Boot が内蔵Webサーバ（標準で8080番ポート）を
 * 立ち上げてAPIを公開する。
 *
 * <p>{@code @MapperScan} … MyBatisのMapperインタフェースがどのパッケージにあるかを
 * Springに教える。ここではルートの {@code com.example.training} 以下すべてを見るので、
 * book / lending / member それぞれのMapperが自動で使えるようになる。
 */
@SpringBootApplication
@MapperScan("com.example.training")
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }
}
