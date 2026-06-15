package com.example.training.task;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * tasks テーブルに対するDBアクセス役（Mapper）。
 *
 * <p>実際のSQLは {@code src/main/resources/mapper/TaskMapper.xml} に書く。
 * このインタフェースのメソッド名とXMLの id を一致させることで、MyBatisが
 * 両者をつないでくれる。
 */
@Mapper
public interface TaskMapper {

    /** 全タスクを取得（id昇順）。 */
    List<Task> findAll();

    /** id を指定して1件取得。いなければ null。 */
    Task findById(@Param("id") Long id);

    /**
     * 1件登録する。登録後、採番された id が引数 task の id にセットされる
     * （XML側で useGeneratedKeys を使っているため）。
     */
    int insert(Task task);

    /** 既存タスクを更新する。更新できた件数を返す（0なら対象なし）。 */
    int update(Task task);

    /** id を指定して削除する。削除できた件数を返す（0なら対象なし）。 */
    int delete(@Param("id") Long id);
}
