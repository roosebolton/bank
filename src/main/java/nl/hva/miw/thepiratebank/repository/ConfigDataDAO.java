package nl.hva.miw.thepiratebank.repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public class ConfigDataDAO {
    private JdbcTemplate jdbcTemplate;

    public ConfigDataDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Gets the transactionFee as present in the database
     * @return transactionFee as present in the database
     */
    public BigDecimal getTransActionFee(){
        String sql = "SELECT transaction_fee FROM configdata LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql,BigDecimal.class);
        }
        catch (EmptyResultDataAccessException emptyResultDataAccessException){
            return null;
        }
    }


    /**
     * Gets the bankStartingCapital as present in the database
     * @return bankStartingCapital as present in the database
     */
    public BigDecimal getBankStartingCapital(){
        String sql = "SELECT bank_starting_capital FROM configdata LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql,BigDecimal.class);
        }
        catch (EmptyResultDataAccessException emptyResultDataAccessException){
            return null;
        }
    }

    /**
     * Gets the bankId as present in the database
     * @return bankId as present in the database
     */
    public Integer getBankId(){
        String sql = "SELECT bank_id FROM configdata LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql,Integer.class);
        }
        catch (EmptyResultDataAccessException emptyResultDataAccessException){
            return null;
        }
    }

    /**
     * Sets the new transactionFee in the database
     * @param newTransactionFee
     */
    public void setTransactionFee(BigDecimal newTransactionFee){
        String sql = "UPDATE configdata SET transaction_fee = ? LIMIT 1";
        jdbcTemplate.update(sql,newTransactionFee);
    }

    /**
     * Sets the new bankStatringCapital in the database
     * @param newStartingCapital
     */
    public void setBankStartingCapital(BigDecimal newStartingCapital){
        String sql = "UPDATE configdata SET bank_starting_capital = ? LIMIT 1";
        jdbcTemplate.update(sql,newStartingCapital);
    }

    /**
     * Sets the new bankStatringCapital in the database
     * @param newBankId
     */
    public void setBankId (int newBankId){
        String sql = "UPDATE configdata SET bank_id = ? LIMIT 1";
        jdbcTemplate.update(sql,newBankId);
    }


}
