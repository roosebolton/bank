package nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken.repository;

import nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken.RefreshToken;
import nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken.RefreshTokenPayload;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class RefreshRefreshTokenDAOJDBCImpl extends RefreshTokenAbstractDAO {

    private final JdbcTemplate jdbcTemplate;

    public RefreshRefreshTokenDAOJDBCImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<RefreshToken> findAll() {
        String SQL = "SELECT * from refreshtoken";
        return jdbcTemplate.query(SQL, new RefreshTokenRowMapper());
    }

    @Override
    public void save(RefreshToken refreshToken) {
        String SQL = "INSERT INTO refreshtoken values(?,?,?,?)";
        jdbcTemplate.update(SQL,
                refreshToken.getRefreshTokenKey(),
                refreshToken.getPayload().getUserId(),
                refreshToken.getPayload().getExpiryDate(),
                refreshToken.getPayload().isActive());
    }

    @Override
    public Optional<RefreshToken> find(String id) {
        String SQL = "SELECT * from refreshtoken " +
                "WHERE refreshTokenKey = ? AND active = true";
        List<RefreshToken> refreshToken =
                jdbcTemplate.query(SQL, new RefreshTokenRowMapper(),id);
        if(refreshToken.size() == 1) {
            return Optional.of(refreshToken.get(0));
        }
        return Optional.empty();
    }

    @Override
    public void update(RefreshToken refreshToken) {
        String SQL = "Update refreshtoken SET user_id = ?, expirydate  = ?, active = ? WHERE refreshTokenKey = ?";
        jdbcTemplate.update(SQL,
                refreshToken.getPayload().getUserId(),
                refreshToken.getPayload().getExpiryDate(),
                refreshToken.getPayload().isActive(),
                refreshToken.getRefreshTokenKey()
                );
    }

    @Override
    public void delete(String id) {
        String SQL = "DELETE From refreshtoken WHERE refreshTokenId = ?";
        jdbcTemplate.update(SQL, id);
    }


    @Override
    public void blockToken(String token) {
        String SQL = "Update refreshtoken SET active = ? WHERE refreshTokenKey = ?";
        jdbcTemplate.update(SQL,
                false,
                token
        );
    }

    @Override
    public Optional<RefreshToken> findActiveByUserid(int userId) {
        String SQL = "SELECT * from refreshtoken WHERE user_id= ? AND active = true";
        List<RefreshToken> refreshToken =
                jdbcTemplate.query( SQL, new RefreshTokenRowMapper(),userId);
        if(refreshToken.size() == 1) {
            return Optional.of(refreshToken.get(0));
        }
        return Optional.empty();
    }


    private static class RefreshTokenRowMapper implements RowMapper<RefreshToken> {
        @Override
        public RefreshToken mapRow(ResultSet rs, int rowNum) throws SQLException {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setRefreshTokenKey(rs.getString("refreshTokenKey"));
            RefreshTokenPayload refreshTokenPayload = new RefreshTokenPayload();
            refreshTokenPayload.setUserId(rs.getInt("user_id"));
            refreshTokenPayload.setExpiryDate(rs.getDate("expiryDate"));
            refreshTokenPayload.setActive(rs.getBoolean("active"));
            refreshToken.setRefreshTokenPayload(refreshTokenPayload);
            return refreshToken;
        }
    }
}
