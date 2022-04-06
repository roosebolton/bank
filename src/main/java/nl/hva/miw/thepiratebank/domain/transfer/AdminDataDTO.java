package nl.hva.miw.thepiratebank.domain.transfer;

import nl.hva.miw.thepiratebank.domain.Account;
import nl.hva.miw.thepiratebank.domain.Customer;
import java.math.BigDecimal;

public class AdminDataDTO {

    private BigDecimal transactionfee;
    private Customer customer;
    private Account account;

    public AdminDataDTO(BigDecimal transactionfee, Customer customer, Account account) {
        this.transactionfee = transactionfee;
        this.customer = customer;
        this.account = account;
    }

    public BigDecimal getTransactionfee() {
        return transactionfee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Account getAccount() {
        return account;
    }
}
