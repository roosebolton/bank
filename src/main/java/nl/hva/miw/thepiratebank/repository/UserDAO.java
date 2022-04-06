package nl.hva.miw.thepiratebank.repository;

import nl.hva.miw.thepiratebank.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAO extends UserAbstractDAO {

    private JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jbdcTemplate) {
        super();
        this.jdbcTemplate = jbdcTemplate;
    }

    @Override
    public List<User> getAll() {
        String sql = "Select * From user";
        List<User> result = new ArrayList<>();
        try {
            result = jdbcTemplate.query(sql, new UserRowMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            result.isEmpty();
        }
        return result;
    }

    @Override
    public void create(User user) {
        String sql = "Insert into user(username, password) values (?,?)";
        jdbcTemplate.update(sql, user.getUserName(), user.getPassword());
    }

    @Override
    public User get(Integer id) {
        User user;
        String sql ="SELECT * FROM user WHERE user_id = ?";
        try {
            user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
        } catch (DataAccessException dataAccessException) {
            user = null;
        }
        return user;
    }

    @Override
    public void update(User user) {
        String sql = "Update user Set username = ?, password = ? where user_id = ?;";
        jdbcTemplate.update(sql, user.getUserName(), user.getPassword(), user.getUserId());
    }

    // this method has no exception handling. Please use exception handling or check method when calling this method
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM user Where user_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public User getByUsername(String userName) {
        User user;
        String sql = "SELECT * FROM user WHERE username = ?";
        try {
            user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), userName);
        } catch (DataAccessException dataAccessException) {
            user = null;
        }
        return user;
    }


    public class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setUserName(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    }
}

