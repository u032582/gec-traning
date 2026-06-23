public class Ex04 {

    public int sumTo(int n) {
        int sum = 0;
        for(int i = 1; i <= n; i++){
            sum = sum + i;
        }
            return sum;
    }

    public long factorial(int n) {
        int result = 1;
        for(int i = 1; i <= n; i++){
            result = result * i;
        }
        return result;
    }

    public int countDigits(int n) {
        if(n == 0){
            return 1;
        }
        int count = 0;
        while(n > 0){
            n = n / 10;
            count++;
        }
        return count;
    }

    public String fizzbuzz(int n) {
        String result = "";
        for(int i = 1; i <= n; i++){
            if(i > 1){
                result += "\n";
            }
            if(i % 15 == 0){
                result += "FizzBuzz";
            }else if(i % 3 == 0){
                result += "Fizz";
            }else if(i % 5 == 0){
                result += "Buzz";
            }else{
                result += i + "";
            }
        } 
        return result;
    }
}