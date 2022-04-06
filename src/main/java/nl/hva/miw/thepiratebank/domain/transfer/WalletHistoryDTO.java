package nl.hva.miw.thepiratebank.domain.transfer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

public class WalletHistoryDTO {
    int user_id;
    Map<Timestamp, BigDecimal> walletHistory;

    public WalletHistoryDTO(int user_id, Map<Timestamp, BigDecimal> walletHistory) {
        this.user_id = user_id;
        this.walletHistory = walletHistory;
    }

    public WalletHistoryDTO() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Map<Timestamp, BigDecimal> getWalletHistory() {
        return walletHistory;
    }

    public void setWalletHistory(Map<Timestamp, BigDecimal> walletHistory) {
        this.walletHistory = walletHistory;
    }

    @Override
    public String toString() {
        return "WalletHistoryDTO{" +
                "user_id=" + user_id +
                ", walletHistory=" + walletHistory +
                '}';
    }
}
