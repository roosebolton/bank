package nl.hva.miw.thepiratebank.repository.wallethistory;


import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.domain.WalletHistory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class WalletHistoryDAO extends AbstractWalletHistoryDAO {

    private JdbcTemplate jdbcTemplate;

    public WalletHistoryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //ToDo remove this debug sout
    public void addCurrentValueToHistory(WalletHistory walletHistory) {
        String sql = "INSERT INTO wallethistory VALUES(?,?,?)";
        jdbcTemplate.update(sql, walletHistory.getCustomer().getUserId(), Timestamp.valueOf
                (LocalDateTime.now()), walletHistory.getCustomer().getWallet().getTotalValue());
    }


    //ToDo implement these batchUpdateMethods
    @Override
    public List<WalletHistory> getAll() {
        return null;
    }

    @Override
    public void create(WalletHistory walletHistory) {
    }

    @Override
    public WalletHistory get(Integer id) {
        String sql = "SELECT * FROM wallethistory WHERE customer_user_id = ? LIMIT 366";
        return jdbcTemplate.query(sql, new WalletHistoryResultSetExtractor(), id);
    }

    @Override
    public void update(WalletHistory walletHistory) {

    }

    @Override
    public void delete(Integer id) {

    }

    private class WalletHistoryResultSetExtractor implements ResultSetExtractor<WalletHistory> {

        @Override
        public WalletHistory extractData(ResultSet rs) throws SQLException, DataAccessException {
            WalletHistory walletHistory = new WalletHistory();
            Map<Timestamp, BigDecimal> map = new TreeMap<>();
            if (rs.next()) {
                map.put(rs.getTimestamp("timestamp"), rs.getBigDecimal("value"));
            }
            while (rs.next()) {
                map.put(rs.getTimestamp("timestamp"), rs.getBigDecimal("value"));
            }
            walletHistory.setWalletValueHistory(map);
            return walletHistory;
        }
    }
}
