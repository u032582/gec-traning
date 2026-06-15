package com.example.training.customer;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 顧客のDBアクセス窓口。お手本の findById に、登録用 insert を追加した版。
 */
@Mapper
public interface CustomerMapper {

    /** IDで1件取得。見つからなければ null。 */
    Customer findById(@Param("customerId") Long customerId);

    /**
     * 顧客を1件登録する。
     * 採番された customer_id は、渡した customer の customerId に書き戻される
     * （XMLの useGeneratedKeys / keyProperty 設定による）。
     */
    void insert(Customer customer);
}
