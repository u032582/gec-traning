package com.example.training;

/**
 * 課題1-2 で使う「DBアクセス役（Mapper）」のインタフェース。
 *
 * <p>本来は MyBatis が SQL をつないで中身を動かすが、単体テストでは
 * 「本物のDBにつながない」。代わりに Mockito でこのインタフェースの
 * ニセモノ（モック）を作り、好きな戻り値を返させる。
 *
 * <p>用語メモ: <b>モック</b> … 本物のフリをするニセの部品。
 * テスト対象（Service）だけを切り離して試すために使う。
 */
public interface MemberMapper {

    /**
     * id を指定して会員を1件取得する。
     *
     * @param id 会員番号
     * @return 見つかれば Member、いなければ null
     */
    Member findById(int id);

    /**
     * 指定した会員のポイントを更新する。
     *
     * @param id       会員番号
     * @param newPoint 更新後のポイント
     * @return 更新できた件数（1なら成功、0なら対象なし）
     */
    int updatePoint(int id, int newPoint);
}
