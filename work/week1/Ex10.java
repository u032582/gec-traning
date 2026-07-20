public class Ex10 {
    // ファイル名(Ex10.java)に合わせるための入れ物。中身は空でよい。
}

class BankAccount {
    private String owner;
    private int balance;

    public BankAccount(String owner, int initialBalance) {
        this.owner = owner;
        this.balance = initialBalance;
    }

    public String getOwner() {
        return owner;
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int amount) {
        if(amount <= 0){
            return;
        }
            balance = balance + amount;
        
    }

    public boolean withdraw(int amount) {
        if(amount <= 0 || balance < amount){
            return false;
        }
            balance = balance - amount;
            return true;
    }
}