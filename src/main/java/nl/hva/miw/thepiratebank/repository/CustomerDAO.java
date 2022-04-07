package nl.hva.miw.thepiratebank.repository;


import nl.hva.miw.thepiratebank.domain.*;
import nl.hva.miw.thepiratebank.domain.customerattributes.Address;
import nl.hva.miw.thepiratebank.domain.customerattributes.IdentifyingInformation;
import nl.hva.miw.thepiratebank.domain.customerattributes.PersonalDetails;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class CustomerDAO extends CustomerAbstractDAO {

    private JdbcTemplate jdbcTemplate;
    private UserDAO userDAO;

    public CustomerDAO(JdbcTemplate jbdcTemplate, UserDAO userDAO) {
        super();
        this.jdbcTemplate = jbdcTemplate;
        this.userDAO = userDAO;
    }

    @Override
    public List<Customer> getAll() {
        String sql = "Select * From customer";
        List<Customer> result = new ArrayList<>();
        try {
            result = jdbcTemplate.query(sql, new CustomerRowMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            result.isEmpty();
        }
        return result;
    }

    //ToDo use different method to create a customer that is not dependent on a different DAO, see dictaat domainmapping

    @Override
    public void create(Customer customer) {
    String sql = "INSERT INTO customer VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
    User user = userDAO.getByUsername(customer.getUserName());
    jdbcTemplate.update(sql, user.getUserId(), customer.getPersonalDetails().getFirstName(),customer.getPersonalDetails().getInFix(),
            customer.getPersonalDetails().getLastName(), customer.getIdentifyingInformation().getDateOfBirth(),
            customer.getIdentifyingInformation().getBsnNumber(), customer.getAddress().getPostalCode(),
            customer.getAddress().getHouseNumber(), customer.getAddress().getHouseNumberAddition(),
            customer.getAddress().getStreet(), customer.getAddress().getCity(),
            customer.getIdentifyingInformation().getIbanNumber());
    }

    @Override
    public Customer get(Integer id) {
        String sql ="SELECT * FROM customer WHERE user_id = ?";
            Customer customer;
            try {
                customer = jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), id);
            } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
                customer = null;
            }
            return customer;
        }


    @Override
    public void update(Customer customer) {
        String sql = "Update customer Set first_name=?, infix =?, last_name=?,date_of_birth=?,bsn_number=?," +
                "postal_code=?, house_number=?,house_number_addition=?,street=?,city=?,iban_number=? where user_id=?";
        jdbcTemplate.update(sql, customer.getUserId(), customer.getPersonalDetails().getFirstName(),customer.getPersonalDetails().getInFix(),
                customer.getPersonalDetails().getLastName(), customer.getIdentifyingInformation().getDateOfBirth(),
                customer.getIdentifyingInformation().getBsnNumber(), customer.getAddress().getPostalCode(),
                customer.getAddress().getHouseNumber(), customer.getAddress().getHouseNumberAddition(),
                customer.getAddress().getStreet(), customer.getAddress().getCity(),
                customer.getIdentifyingInformation().getIbanNumber());
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM customer Where user_id=?";
        jdbcTemplate.update(sql, id);
    }


public class CustomerRowMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Address address = new Address (rs.getString("street"), rs.getString("house_number"),
                rs.getString("house_number_addition"), rs.getString("postal_code"),
                rs.getString("city"));
        PersonalDetails personalDetails = new PersonalDetails(rs.getString("first_name"),
                rs.getString("infix"), rs.getString("last_name") );
        IdentifyingInformation identifyingInformation = new IdentifyingInformation(rs.getInt("bsn_number"),
                rs.getString("iban_number"), LocalDate.parse(rs.getDate("date_of_birth").toString()));
        Customer customer = new Customer (personalDetails, address, identifyingInformation);
        customer.setUserId(rs.getInt("user_id"));
        return customer;
    }
}

/**  Association methods   */
        public Optional<Customer> findBuyerOf(Integer transactionId) {
        int buyerId =
                jdbcTemplate.queryForObject("SELECT buyer from transaction WHERE transactionId = ?", Integer.class, transactionId);
        List<Customer> transactions= jdbcTemplate.query("SELECT * from customer where user_id = ?", new CustomerRowMapper(),buyerId);
            if (transactions.size() != 1) {
                return Optional.empty();
            } else {
                return Optional.of(transactions.get(0));
            }
        }

        public Optional<Customer> findSellerOf(Integer transactionId) {
        int sellerId =
                jdbcTemplate.queryForObject("SELECT seller FROM transaction WHERE transactionId = ?", Integer.class, transactionId);
        List<Customer> transactions= jdbcTemplate.query("SELECT * FROM customer WHERE user_id = ?", new CustomerRowMapper(),sellerId);
            if (transactions.size() != 1) {
                return Optional.empty();
            } else {
                return Optional.of(transactions.get(0));
            }
        }

    public Optional<Customer> findCustomerOfOrder(Integer orderId) {
        int customerId =
                jdbcTemplate.queryForObject("SELECT userId FROM `order` WHERE orderId = ?", Integer.class, orderId);
        List<Customer> transactions= jdbcTemplate.query("SELECT * FROM customer WHERE user_id = ?", new CustomerRowMapper(),customerId);
        if (transactions.size() != 1) {
            return Optional.empty();
        } else {
            return Optional.of(transactions.get(0));
        }
    }


}
