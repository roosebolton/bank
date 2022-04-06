package nl.hva.miw.thepiratebank.domain.transfer;

import nl.hva.miw.thepiratebank.domain.Account;
import nl.hva.miw.thepiratebank.service.AdminService;

import java.math.BigDecimal;

public class AdminAccountBalanceDTO {


    private String userName;
    private BigDecimal balance;

    public AdminAccountBalanceDTO() {
        this.userName = "";
        this.balance = BigDecimal.ZERO;
    }

    public String getUserName() {
        return userName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
