package banking;

public class LoggedBankAccount extends BankAccount {
    private int account;
    private int password;
    public LoggedBankAccount(int balance, int account, int password) {
        super(balance);
        this.account = account;
        this.password = password;
    }

    public boolean login(int account, int password) {
        if (account == this.account && password == this.password) {
            return true;
        }
        return false;
    }

    /**
     * @precondition amount > 0
     * @param amount of money wanted to be deposited
     */
    public void deposit(int account, int password, int amount) {
        if (login(account, password)) {
            balance += amount;
        }
    }
    /**
     * @precondition amount > 0
     * @param amount of money wanted to be withdrawn
     */
    public void withdraw(int account, int password, int amount) {
        if (login(account, password)) {
            balance -= amount;
        }
    }
}
