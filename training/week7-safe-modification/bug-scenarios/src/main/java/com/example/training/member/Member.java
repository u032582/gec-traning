package com.example.training.member;

import java.time.LocalDateTime;

/**
 * members テーブルの1行に対応する入れ物（エンティティ）。
 *
 * <p>MyBatis が SQL の結果をこのクラスに詰めてくれる。
 * そのため、setter とデフォルトコンストラクタ（暗黙）を用意している。
 */
public class Member {

    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
