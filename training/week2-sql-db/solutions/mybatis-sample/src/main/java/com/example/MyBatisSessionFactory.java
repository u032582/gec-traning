package com.example;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * MyBatis の初期化を担うクラス（完成済み・書き換え不要）。
 *
 * SqlSessionFactory とは、DBとの「対話の窓口（SqlSession）」を作り出す工場。
 * アプリの中で1つだけ作って使い回すのが定石なので、static で1度だけ構築している。
 *
 * 流れ:
 *   1. mybatis-config.xml（接続情報・マッパー登録）を読み込む
 *   2. それを元に SqlSessionFactory を組み立てる
 *   3. App.java から getSqlSessionFactory() で受け取って使う
 */
public final class MyBatisSessionFactory {

    private static final SqlSessionFactory FACTORY = build();

    private MyBatisSessionFactory() {
        // ユーティリティクラスなのでインスタンス化させない
    }

    private static SqlSessionFactory build() {
        try {
            // クラスパス上の mybatis-config.xml を読み込む
            String configPath = "mybatis-config.xml";
            try (InputStream in = Resources.getResourceAsStream(configPath)) {
                return new SqlSessionFactoryBuilder().build(in);
            }
        } catch (Exception e) {
            // 設定の読み込みに失敗したら、原因を分かるように包んで投げ直す
            throw new IllegalStateException(
                    "MyBatisの初期化に失敗しました。mybatis-config.xml を確認してください。", e);
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return FACTORY;
    }
}
