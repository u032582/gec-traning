public class Ex09 {

    public int safeDivide(int a, int b) {
        try{
            return a / b;
        }catch(ArithmeticException e){
            return 0;
        }
    }

    public int parseOrDefault(String s, int defaultValue) {
        try{
            return Integer.parseInt(s);
        }catch(NumberFormatException e){
            return defaultValue;
        }
    }

    public int withdraw(int balance, int amount) throws InsufficientBalanceException {
        if(balance < amount){
            throw new InsufficientBalanceException("残高不足");
        }
        return balance - amount;
    }
}

// 自作の検査例外（このファイル内に書く）
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}