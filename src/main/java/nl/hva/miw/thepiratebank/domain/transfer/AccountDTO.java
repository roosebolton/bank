package nl.hva.miw.thepiratebank.domain.transfer;

import nl.hva.miw.thepiratebank.domain.Account;

import java.math.BigDecimal;

public class AccountDTO {
    private int userId;
    private BigDecimal balance;

    public AccountDTO(Account account) {
        this.userId = account.getCustomer().getUserId();
        this.balance = account.getBalance();
    }

    public AccountDTO(){
        userId = 0;
        balance = BigDecimal.ZERO;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
