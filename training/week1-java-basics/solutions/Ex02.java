/**
 * 課題02: 四則演算と型変換（模範解答）
 *
 * 仕様:
 *  - add(int a, int b)        : a + b を返す
 *  - subtract(int a, int b)   : a - b を返す
 *  - multiply(int a, int b)   : a * b を返す
 *  - divideAsDouble(int a, int b) : a ÷ b を小数(double)で返す（例: 7,2 -> 3.5）
 *  - average(int a, int b, int c) : 3つの平均を double で返す
 *  - toInt(double value)      : 小数を切り捨てて int にして返す（例: 3.9 -> 3）
 */
public class Ex02 {

    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public double divideAsDouble(int a, int b) {
        // int 同士の割り算は小数が切り捨てられるので、片方を double にしてから割る
        return (double) a / b;
    }

    public double average(int a, int b, int c) {
        return (a + b + c) / 3.0;
    }

    public int toInt(double value) {
        return (int) value;
    }
}
