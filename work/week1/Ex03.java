public class Ex03{
    public String sign(int n){
        if(n > 0){
            return "正";
        }else if(n == 0){
            return "ゼロ";
        }else{
            return "負";
        }
    }
    public String grade(int score){
        if(score >= 90){
            return "A";
        }else if(score >= 80){
            return "B";
        }else if(score >= 70){
            return "C";
        }else if(score >= 60){
            return "D";
        }else{
            return "F";
        }
    }
    public boolean isLeapYear(int year){
        if(year % 400 == 0){
            return true;
        }else if(year % 100 ==0){
            return false;
        }else if(year % 4 == 0){
            return true;
        }else{
            return false;
        }
    }
    public int max3(int a, int b, int c){
        if(a > b && a > c){
            return a;
        }else if(b > a && b > c){
            return b;
        }else{
            return c;
        }
    }
}