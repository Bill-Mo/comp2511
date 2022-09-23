package banking;

public class BankAccount {
    protected int balance;
    /**
     * @invariant balance >= 0
     * @param balance
     */
    public BankAccount(int balance) {
        this.balance = balance;
    }
    /**
     * @precondition amount > 0
     * @param amount of money wanted to be deposited
     */
    public void deposit(int amount) {
        balance += amount;
    }
    /**
     * @precondition amount > 0
     * @param amount of money wanted to be withdrawn
     */
    public void withdraw(int amount) {
        balance -= amount;
    }

    public int getBalance() {
        return balance;
    }
}