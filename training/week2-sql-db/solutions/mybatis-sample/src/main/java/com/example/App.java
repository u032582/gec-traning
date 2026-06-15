package com.example;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * 実行の入口（main）。完成済み・書き換え不要。
 *
 * やっていること:
 *   1. MyBatis の窓口工場（SqlSessionFactory）を受け取る
 *   2. そこから1回ぶんの対話（SqlSession）を開く
 *   3. CustomerMapper を取り出し、findById(1) で顧客id=1を取得
 *   4. 取得した顧客を表示する
 *
 * 期待される出力:
 *   取得した顧客: Customer{id=1, name='佐藤 太郎', email='sato@example.com', prefecture='東京都'}
 */
public class App {

    public static void main(String[] args) {
        SqlSessionFactory factory = MyBatisSessionFactory.getSqlSessionFactory();

        // try-with-resources: 使い終わった SqlSession を自動で閉じる（後始末を忘れない書き方）
        try (SqlSession session = factory.openSession()) {
            // マッパー（あなたが findById を宣言したインターフェース）を取り出す
            CustomerMapper mapper = session.getMapper(CustomerMapper.class);

            // id=1 の顧客を1件取得
            int targetId = 1;
            Customer customer = mapper.findById(targetId);

            if (customer == null) {
                System.out.println("id=" + targetId + " の顧客は見つかりませんでした。"
                        + " schema.sql を training DB に流し込んだか確認してください。");
            } else {
                System.out.println("取得した顧客: " + customer);
            }
        }
    }
}
