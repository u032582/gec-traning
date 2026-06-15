package com.example.training;

/**
 * 課題1-2（Mockitoでモック化）で使う、会員を表す入れ物クラス。
 *
 * <p>「DBの1行ぶん」を表すデータの箱（エンティティと呼ぶ）。
 * id（会員番号）、name（氏名）、point（保有ポイント）を持つ。
 */
public class Member {

    private final int id;
    private final String name;
    private final int point;

    public Member(int id, String name, int point) {
        this.id = id;
        this.name = name;
        this.point = point;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }
}
