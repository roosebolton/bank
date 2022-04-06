package nl.hva.miw.thepiratebank.repository.assetrate;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.AssetRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Repository
public class AssetRateDAO extends AssetRateAbstractDAO{

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private JdbcTemplate jdbcTemplate;

    public AssetRateDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<AssetRate> getAll() {
        String sql = "Select * From asset_rates";
        List<AssetRate> assetRates = new ArrayList<>();
        try{
            assetRates = jdbcTemplate.query(sql, new assetRateRowmapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException){
            assetRates.isEmpty();
        }
        return assetRates;
    }

    @Override
    public void create(AssetRate assetRate) {
        String sql = "Insert Into asset_rates(asset_name, timestamp, value) values(?, ?, ?);";
        try {
            jdbcTemplate.update(sql, assetRate.getAsset().getName(), assetRate.getTimestamp(), assetRate.getValue());
        } catch (DataAccessException exception) {
            log.debug(exception.getMessage());
        }
    }

    @Override
    public AssetRate get(String id) {
        String sql = "Select * from asset_rates where asset_name = ? ORDER BY timestamp DESC LIMIT 1;";
        return jdbcTemplate.queryForObject(sql, new assetRateRowmapper(),id);
    }


    @Override
    public AssetRate getAssetRateByTimestamp(String asset_name,Timestamp timestamp) {
        String sql = "Select * from asset_rates where asset_name = ? AND timestamp = ?;";
       return jdbcTemplate.queryForObject(sql, new assetRateRowmapper(),asset_name, timestamp);
    }

    @Override
    public List<AssetRate> getFullHistory (String asset_name) {
        String sql = "SELECT * FROM asset_rates WHERE asset_name = ? ";
        List<AssetRate>  assetRates = null;
        try {
            assetRates= jdbcTemplate.query(sql, new assetRateRowmapperWithoutAssetname(),asset_name);
        } catch (Exception exception) {
            log.debug(exception.getMessage());
        }
        return assetRates;
    }


    @Override
    public List<AssetRate> getClosest(String asset_name,Timestamp timestamp) {
        String sql = "SELECT * FROM asset_rates WHERE asset_name = ? " +
                "AND timestamp BETWEEN ? AND ? ORDER BY timestamp DESC";
        List<AssetRate>  assetRates = null;
        try {
            assetRates= jdbcTemplate.query(sql, new assetRateRowmapper(),asset_name, timestamp.toLocalDateTime().minusDays(1),timestamp);
        } catch (Exception exception) {
            log.debug(exception.getMessage());
        }
        return assetRates;
    }

    @Override
    public void update(AssetRate assetRate) {
        String sql = "Update asset_rates set value = ? where asset_name = ? AND timestamp = ? ";
        jdbcTemplate.update(sql, assetRate.getValue(),assetRate.getAsset().getName(), assetRate.getTimestamp());
    }

    @Override
    public void delete(String asset_name) {
        String sql = "Delete From asset_rates where asset_name = ?";
        jdbcTemplate.update(sql, asset_name);
    }

    @Override
    public void deleteAssetRateByTimestamp(String asset_name, Timestamp timestamp){
        String sql = "Delete From asset_rates where asset_name = ? And timestamp = ?";
        jdbcTemplate.update(sql, asset_name, timestamp);
    }


    public class assetRateRowmapper implements RowMapper<AssetRate> {
        @Override
        public AssetRate mapRow(ResultSet rs, int rowNum) throws SQLException {
            Timestamp timeStamp = rs.getTimestamp("timestamp");
            BigDecimal value = rs.getBigDecimal("value");
            Asset associatedAssetName = new Asset();
            associatedAssetName.setName(rs.getString("asset_name"));
            AssetRate assetRate = new AssetRate(associatedAssetName, timeStamp, value);
            return assetRate;
        }
    }

    public class assetRateRowmapperWithoutAssetname implements RowMapper<AssetRate> {
        @Override
        public AssetRate mapRow(ResultSet rs, int rowNum) throws SQLException {
            AssetRate assetRate = new AssetRate();
            assetRate.setTimestamp(rs.getTimestamp("timestamp"));
            assetRate.setValue(rs.getBigDecimal("value"));
            return assetRate;
        }
    }
}

