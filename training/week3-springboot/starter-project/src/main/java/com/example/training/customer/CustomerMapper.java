package com.example.training.customer;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * DBアクセスの窓口（MyBatisのMapperインターフェース）。3層でいう「Repository層」。
 *
 * <p>ここには「メソッドの形（名前・引数・戻り値）」だけを書く。
 * 実際のSQLは、同じ名前のXMLファイル（resources/mapper/CustomerMapper.xml）に書く。
 * MyBatisが「このインターフェースのメソッド」と「XMLのSQL」をidで結びつけてくれる。
 *
 * <p>用語メモ: Mapper（マッパー）… JavaのメソッドとSQLを対応づける部品。
 * {@code @Mapper} … 「これはMyBatisのMapperです」という目印。
 */
@Mapper
public interface CustomerMapper {

    /**
     * 顧客を1件、IDで取得する。
     * 見つからなければ null を返す（MyBatisの仕様）。
     *
     * @param customerId 探す顧客のID
     * @return 見つかった顧客。なければ null。
     */
    Customer findById(@Param("customerId") Long customerId);
}
