package nl.hva.miw.thepiratebank.repository.order;

import nl.hva.miw.thepiratebank.domain.Order;
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
public class OrderDAO extends OrderAbstractDAO {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final JdbcTemplate jdbcTemplate;

    public OrderDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Order> findAll() {
        String SQL = "Select * from `order`";
        List<Order> orders = new ArrayList<>();
        try {
            orders = jdbcTemplate.query(SQL, new OrderRowmapper());
        } catch (DataAccessException exception) {
            log.debug(exception.getMessage());
        }
        return orders;
    }

    @Override
    public void save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> insertOrderStatement(order,connection),keyHolder);
            order.setOrderId(keyHolder.getKey().intValue());
        } catch (DataAccessException exception){
            log.debug(exception.getMessage());
        }
    }

    @Override
    public Optional<Order> find(Integer id) {
        String SQL = "SELECT * FROM `order` WHERE orderId = ?";
        List<Order> orders = jdbcTemplate.query(SQL, new OrderRowmapper(), id);
        if (orders.size() != 1) {
            return Optional.empty();
        } else {
            return Optional.of(orders.get(0));
        }
    }

    @Override
    public void update(Order order) {
        String SQL = "UPDATE `order` SET buy = ?, userId = ?, assetName = ?, amount = ?, limitPrice = ?, timeOrderPlaced = ? WHERE orderId = ?";
        try {
            jdbcTemplate.update(SQL,order.isBuy(),
                    order.getUser().getUserId(),
                    order.getAsset().getName(),
                    order.getAmount(),
                    order.getLimitPrice(),
                    order.getTimeOrderPlaced(),
                    order.getOrderId());
        } catch (DataAccessException exception) {
            log.debug(exception.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            String SQL = "DELETE FROM `order` WHERE orderId = ?";
            jdbcTemplate.update(SQL,id);
        } catch (DataAccessException exception) {
            log.debug(exception.getMessage());
        }
    }

    public List<Order> findAllOrdersByUserId(int userid) {
        String SQL = "Select * from `order` WHERE userId=?";
        List<Order> orders = new ArrayList<>();
        try {
            orders = jdbcTemplate.query(SQL, new OrderRowmapper(),userid);
        } catch (DataAccessException exception) {
            log.debug(exception.getMessage());
        }
        return orders;
    }




    private PreparedStatement insertOrderStatement(Order order, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO `order`(buy,userId,assetName,amount,limitPrice,timeOrderPlaced) values (?,?,?,?,?,?);"
                , Statement.RETURN_GENERATED_KEYS);
        ps.setBoolean(1,order.isBuy());
        ps.setInt(2,order.getUser().getUserId());
        ps.setString(3,order.getAsset().getName());
        ps.setBigDecimal(4,order.getAmount());
        ps.setBigDecimal(5,order.getLimitPrice());
        ps.setTimestamp(6,order.getTimeOrderPlaced());
        return ps;
    }

    private static class OrderRowmapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
           return new Order(
                    rs.getInt("orderId"),
                    rs.getBoolean("buy"),
                    rs.getBigDecimal("amount"),
                    rs.getBigDecimal("limitPrice"),
                    rs.getTimestamp("timeOrderPlaced"));
        }
    }

}
