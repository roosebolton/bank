package nl.hva.miw.thepiratebank.repository;

import nl.hva.miw.thepiratebank.controller.AssetConsumerController;
import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.domain.customerattributes.Address;
import nl.hva.miw.thepiratebank.domain.customerattributes.IdentifyingInformation;
import nl.hva.miw.thepiratebank.domain.customerattributes.PersonalDetails;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;



@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerDAOTest {
    CustomerDAO customerDAO;
    List<Customer> customerListActual;
    Customer customer;
    @MockBean
    UserDAO userDAO;

    //Necessary to prevent postconstruct annotation from running do not delete
    @MockBean
    private AssetConsumerController assetConsumerController;

    @Autowired
    public CustomerDAOTest(CustomerDAO customerDAO, UserDAO userDAO) {
        this.customerDAO = customerDAO;
        this.userDAO = userDAO;
    }

    @BeforeAll
    void setup() {
        customerListActual = customerDAO.getAll();
        Address address = new Address();
        IdentifyingInformation identifyingInformation = new IdentifyingInformation(12345678,"9898", LocalDate.EPOCH);
        PersonalDetails personalDetails = new PersonalDetails();
        customer = new Customer(personalDetails, address, identifyingInformation);
        customer.setUserId(2);
        customer.getPersonalDetails().setFirstName("Tjardy");
        customer.getPersonalDetails().setLastName("van Keimpema");
        customer.setUserName("tjardy@tjardy.com");
    }


    @Test
    void getAll() {
        int size = customerDAO.getAll().size();
        assertThat(size).isEqualTo(1);
    }

    @Test
    void create() {
        Mockito.when(userDAO.getByUsername(customer.getUserName())).thenReturn(customer);
        customerDAO.create(customer);
        assertThat(customerDAO.get(2)).isNotNull().isEqualTo(customer);

        //deleting after create to prevent test interaction
        customerDAO.delete(2);
        assertThat(customerDAO.getAll()).size().isEqualTo(1);
    }

    @Test
    void get() {
        Mockito.when(userDAO.getByUsername(customer.getUserName())).thenReturn(customer);
        customerDAO.create(customer);
        assertThat(customerDAO.get(customer.getUserId())).isEqualTo(customer);

        //deleting after create to prevent test interaction
        customerDAO.delete(2);
    }

    @Test
    void update() {
        Mockito.when(userDAO.getByUsername(customer.getUserName())).thenReturn(customer);
        customerDAO.create(customer);
        assertThat(customerDAO.get(customer.getUserId())).isEqualTo(customer);
        customer.setUserName("Harry");
        customerDAO.update(customer);
        assertThat(customerDAO.get(2)).isEqualTo(customer);

        //deleting after create to prevent test interaction
        customerDAO.delete(2);
        assertThat(customerDAO.getAll()).size().isEqualTo(1);
    }

    @Test
    void delete() {
        Mockito.when(userDAO.getByUsername(customer.getUserName())).thenReturn(customer);
        customerDAO.create(customer);
        assertThat(customerDAO.getAll()).size().isEqualTo(2);
        customerDAO.delete(2);
        assertThat(customerDAO.getAll()).size().isEqualTo(1);
    }
}