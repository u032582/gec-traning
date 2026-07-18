package com.example.training.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * members テーブルに対するDBアクセス役（Mapper）。
 *
 * <p>実際のSQLは {@code src/main/resources/mapper/MemberMapper.xml} に書く。
 * このインタフェースのメソッド名とXMLの id を一致させることで、MyBatisが
 * 両者をつないでくれる。
 *
 * <p>この題材では利用者の「取得」だけを用意している（登録は範囲外）。
 * 貸出のときに「その利用者が実在するか」を確かめるために使う。
 */
@Mapper
public interface MemberMapper {

    /** id を指定して1件取得。いなければ null。 */
    Member findById(@Param("id") Long id);
}
