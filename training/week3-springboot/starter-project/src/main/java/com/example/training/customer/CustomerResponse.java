package com.example.training.customer;

/**
 * APIの「返事（レスポンス）」として外に返す形を表すクラス（DTO）。
 *
 * <p>用語メモ: DTO（Data Transfer Object）… データを運ぶためだけの入れ物クラス。
 * 「APIが外に見せてよい項目」だけを並べる。エンティティ（Customer）をそのまま返さず、
 * わざわざDTOに詰め替えるのは、「DBの内部構造」と「外に見せる形」を分けておくため。
 * こうしておくと、片方を変えてももう片方に影響しにくい（=変更に強い）。
 *
 * <p>このDTOは Java の record（レコード）で書いている。
 * record … 「値を持つだけの簡単なクラス」を短く書くための仕組み（Java 16以降）。
 * getter などが自動で用意される。
 */
public record CustomerResponse(
        Long id,
        String name,
        String email
) {
    /**
     * エンティティ（Customer）から、レスポンス用DTOを作る変換メソッド。
     * 「DBの形 → 外に見せる形」への詰め替えをここに集約しておく。
     */
    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail()
        );
    }
}
