package nl.hva.miw.thepiratebank.repository.transaction;

import nl.hva.miw.thepiratebank.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionDAO extends TransactionAbstractDAO {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final JdbcTemplate jdbcTemplate;

    public TransactionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transaction> findAll() {
        String SQL = "Select * from transaction";
        List<Transaction> transactions = new ArrayList<>();
        try {
            transactions = jdbcTemplate.query(SQL,new TransactionRowMapper());
        } catch (DataAccessException exception) {
            log.debug(exception.getMessage());
        }
        return transactions;
    }


    @Override
    public void save(Transaction transaction) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> insertTransactionStatement(transaction,connection),keyHolder);
            transaction.setTransactionId(keyHolder.getKey().intValue());
        } catch (DataAccessException exception) {
            log.debug(exception.getMessage());
        }
    }

    @Override
    public Optional<Transaction> find(Integer id) {
        String SQL = "SELECT * FROM transaction WHERE transactionId = ?";
        List<Transaction> transactions = jdbcTemplate.query(SQL, new TransactionRowMapper(),id);
        if (transactions.size() != 1){
            return Optional.empty();
        } else {
            return Optional.of(transactions.get(0));
        }
    }


    @Override
    public List<Transaction> findBuyTransactionsByUserId(Integer id) {
        String SQL = "SELECT * FROM transaction WHERE buyer = ?";
        List<Transaction> transactions = new ArrayList<>();
        try {
            transactions = jdbcTemplate.query(SQL,new TransactionRowMapper(),id);
        } catch (DataAccessException exception) {
            log.debug(exception.getMessage());
        }
        return transactions;
    }

    @Override
    public List<Transaction> findSellTransactionsByUserId(Integer id) {
        String SQL = "SELECT * FROM transaction WHERE seller = ?";
        List<Transaction> transactions = new ArrayList<>();
        try {
            transactions = jdbcTemplate.query(SQL,new TransactionRowMapper(),id);
        } catch (DataAccessException exception) {
            log.debug(exception.getMessage());
        }
        return transactions;
    }

    @Override
    public void update(Transaction transaction) {
        String SQL = "UPDATE transaction SET buyer = ?, seller = ?, assetName = ?, assetAmount = ?, monetaryValue = ?, transactionCost = ?, transactionDate = ? WHERE transactionId = ?; ";
        try {
        jdbcTemplate.update(SQL,transaction.getBuyer().getUserId(),
                 transaction.getSeller().getUserId(),
                 transaction.getAsset().getName(),
                 transaction.getAssetAmount(),
                 transaction.getValue(),
                 transaction.getTransactionCost(),
                 transaction.getTransactionDate(),
                 transaction.getTransactionId());
        } catch (DataAccessException exception) {
            log.debug(exception.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        try {
        String SQL = "DELETE FROM transaction WHERE transactionId = ?";
        jdbcTemplate.update(SQL, id);
        } catch (DataAccessException exception) {
            log.debug(exception.getMessage());
        }
    }

    private PreparedStatement insertTransactionStatement(Transaction transaction, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO transaction(buyer,seller,assetName,assetAmount, monetaryValue, transactionCost,transactionDate) values (?,?,?,?,?,?,?);"
                , Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, transaction.getBuyer().getUserId());
        ps.setInt(2, transaction.getSeller().getUserId());
        ps.setString(3, transaction.getAsset().getName());
        ps.setBigDecimal(4, transaction.getAssetAmount());
        ps.setBigDecimal(5, transaction.getValue());
        ps.setBigDecimal(6, transaction.getTransactionCost());
        ps.setTimestamp(7, transaction.getTransactionDate());
        return ps;
    }

    private static class TransactionRowMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction.TransactionBuilder()
                    .transactionId(rs.getInt("transactionId"))
                    .assetAmount(rs.getBigDecimal("assetAmount"))
                    .value(rs.getBigDecimal("monetaryValue"))
                    .transactionCost(rs.getBigDecimal("transactionCost"))
                    .transactionDate(rs.getTimestamp("transactionDate"))
                    .build();
            return transaction;
        }
    }




}
