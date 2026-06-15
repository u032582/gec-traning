/**
 * 課題04: for文・while文（模範解答）
 *
 * 仕様:
 *  - sumTo(int n)        : 1 から n までの合計を返す（n が0以下なら0）
 *  - factorial(int n)    : n の階乗（n! = 1*2*...*n）を返す。0! は 1
 *  - countDigits(int n)  : 整数 n の桁数を返す（n は0以上。0 は 1桁）
 *  - fizzbuzz(int n)     : 1 から n を1行ずつ連結した文字列を返す（改行 "\n" 区切り）
 *      3の倍数 -> "Fizz" / 5の倍数 -> "Buzz" / 両方 -> "FizzBuzz" / それ以外 -> 数字
 *      例: fizzbuzz(5) -> "1\n2\nFizz\n4\nBuzz"
 */
public class Ex04 {

    public int sumTo(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    public long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public int countDigits(int n) {
        if (n == 0) {
            return 1;
        }
        int count = 0;
        while (n > 0) {
            n /= 10;
            count++;
        }
        return count;
    }

    public String fizzbuzz(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            if (i > 1) {
                sb.append("\n");
            }
            if (i % 15 == 0) {
                sb.append("FizzBuzz");
            } else if (i % 3 == 0) {
                sb.append("Fizz");
            } else if (i % 5 == 0) {
                sb.append("Buzz");
            } else {
                sb.append(i);
            }
        }
        return sb.toString();
    }
}
