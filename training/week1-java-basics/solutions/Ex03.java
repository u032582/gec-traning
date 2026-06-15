/**
 * 課題03: if分岐（模範解答）
 *
 * 仕様:
 *  - sign(int n) : n が正なら "正", 0 なら "ゼロ", 負なら "負" を返す
 *  - grade(int score) : 点数(0-100)から成績を返す
 *      90以上 -> "A" / 80以上 -> "B" / 70以上 -> "C" / 60以上 -> "D" / それ未満 -> "F"
 *  - isLeapYear(int year) : うるう年なら true
 *      条件: 4で割り切れる。ただし100で割り切れる年は除く。ただし400で割り切れる年は含む。
 *  - max3(int a, int b, int c) : 3つの中で最大の値を返す
 */
public class Ex03 {

    public String sign(int n) {
        if (n > 0) {
            return "正";
        } else if (n == 0) {
            return "ゼロ";
        } else {
            return "負";
        }
    }

    public String grade(int score) {
        if (score >= 90) {
            return "A";
        } else if (score >= 80) {
            return "B";
        } else if (score >= 70) {
            return "C";
        } else if (score >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

    public boolean isLeapYear(int year) {
        if (year % 400 == 0) {
            return true;
        }
        if (year % 100 == 0) {
            return false;
        }
        return year % 4 == 0;
    }

    public int max3(int a, int b, int c) {
        int max = a;
        if (b > max) {
            max = b;
        }
        if (c > max) {
            max = c;
        }
        return max;
    }
}
