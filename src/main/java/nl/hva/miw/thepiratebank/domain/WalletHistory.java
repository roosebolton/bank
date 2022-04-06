package nl.hva.miw.thepiratebank.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

public class WalletHistory {
    private Customer customer;
    private Map<Timestamp, BigDecimal> walletValueHistory;

    public WalletHistory(Customer customer, Map<Timestamp, BigDecimal> walletValueHistory) {
        this.customer = customer;
        this.walletValueHistory = walletValueHistory;
    }

    public WalletHistory() {
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Map<Timestamp, BigDecimal> getWalletValueHistory() {
        return walletValueHistory;
    }

    public void setWalletValueHistory(Map<Timestamp, BigDecimal> walletValueHistory) {
        this.walletValueHistory = walletValueHistory;
    }

    @Override
    public String toString() {
        return customer + " " + walletValueHistory;
    }

}
