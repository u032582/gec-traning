/**
 * 課題01: 変数と出力（模範解答）
 *
 * 仕様:
 *  - greeting(String name) : "こんにちは、" + name + "さん！" を返す
 *  - introduce(String name, int age) : name と age を使って自己紹介文を返す
 *      形式: name + "（" + age + "歳）です。"
 */
public class Ex01 {

    public String greeting(String name) {
        return "こんにちは、" + name + "さん！";
    }

    public String introduce(String name, int age) {
        return name + "（" + age + "歳）です。";
    }
}
