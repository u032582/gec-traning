package com.example.training.customer;

import java.time.LocalDateTime;

/**
 * 顧客（customers テーブル1行ぶん）を表す入れ物クラス（エンティティ）。
 *
 * <p>DBの customers テーブルの1行が、このクラスの1つのオブジェクトに対応する。
 * MyBatisが、SQLの結果（列の値）を、この各フィールドに詰めてくれる。
 *
 * <p>用語メモ: エンティティ … DBのテーブルと対応する「データの入れ物」クラス。
 * 列名と項目名の対応は application.yml の map-underscore-to-camel-case で自動化している
 * （customer_id → customerId、created_at → createdAt）。
 */
public class Customer {

    private Long customerId;
    private String name;
    private String email;
    private LocalDateTime createdAt;

    // --- getter / setter ---
    // MyBatisは setter を使って結果を詰め、Springは getter を使ってJSONに変換する。

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
