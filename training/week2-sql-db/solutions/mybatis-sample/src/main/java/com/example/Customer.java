package com.example;

/**
 * 顧客1件を表すドメインクラス（DBの customers テーブルの1行に対応する入れ物）。
 *
 * MyBatis は、SELECT の結果の各列を、同じ名前のフィールドに自動で詰めてくれる。
 *   customers.id         -> id
 *   customers.name       -> name
 *   customers.email      -> email
 *   customers.prefecture -> prefecture
 * （registered_at は今回このクラスに持たせていないので使われないだけ。エラーにはならない。）
 *
 * このクラスは完成済み。書き換える必要はない。
 */
public class Customer {

    private int id;
    private String name;
    private String email;
    private String prefecture;

    // MyBatis は、引数なしコンストラクタでオブジェクトを作り、setter で値を詰める。
    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getPrefecture() {
        return prefecture;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }

    // 画面表示用。println したときに中身が分かるようにしている。
    @Override
    public String toString() {
        return "Customer{id=" + id
                + ", name='" + name + '\''
                + ", email='" + email + '\''
                + ", prefecture='" + prefecture + '\''
                + '}';
    }
}
