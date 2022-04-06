package nl.hva.miw.thepiratebank.repository;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AssetDAO implements GenericDAO<Asset, String> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;

    public AssetDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Asset> getAll(){
        String sql = "Select * From Asset;";
        List<Asset> assets = new ArrayList<>();
        try{
            assets = jdbcTemplate.query(sql, new assetRowmapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException){
            assets.isEmpty();
        }
        return assets;
    }

    @Override
    public void create(Asset asset) {
        String sql = "Insert into asset Values(?, ?);";

        try {
            jdbcTemplate.update(sql, asset.getName(), asset.getAbbreviation());
        }catch (DataAccessException e) {
            log.debug(e.toString());
        }
    }


    @Override
    public Asset get(String name) {
        String sql = "Select * From asset Where name = ?;";
        Asset asset;
        try{
            asset = jdbcTemplate.queryForObject(sql, new assetRowmapper(), name);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException){
            asset = null;
        }
        return asset;
    }

    @Override
    public void update(Asset asset) {
        String sql = "Update asset Set abbreviation = ? Where name = ?;";
        jdbcTemplate.update(sql, asset.getAbbreviation(), asset.getName());
    }

    @Override
    public void delete(String name) {
        String sql = "Delete From asset Where name = ?;";
        jdbcTemplate.update(sql, name);
    }


    public class assetRowmapper implements RowMapper<Asset>{
        @Override
        public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {
            Asset asset = new Asset();
            asset.setName(rs.getString("name"));
            asset.setAbbreviation(rs.getString("abbreviation"));
            return asset;
        }
    }

    public Optional<Asset> findAssetOfTransaction(Integer transactionId) {
        String assetName =
                jdbcTemplate.queryForObject("SELECT assetName FROM transaction WHERE transactionId = ?", String.class, transactionId);
        List<Asset> asset= jdbcTemplate.query("SELECT * FROM asset WHERE name = ?", new assetRowmapper(),assetName);
        if (asset.size() != 1) {
            return Optional.empty();
        } else {
            return Optional.of(asset.get(0));
        }
    }

    public Optional<Asset> findAssetOfOrder(Integer orderId) {
        String assetName =
                jdbcTemplate.queryForObject("SELECT assetName FROM `order` WHERE orderId = ?", String.class, orderId);
        List<Asset> asset= jdbcTemplate.query("SELECT * FROM asset WHERE name = ?", new assetRowmapper(),assetName);
        if (asset.size() != 1) {
            return Optional.empty();
        } else {
            return Optional.of(asset.get(0));
        }
    }
}
