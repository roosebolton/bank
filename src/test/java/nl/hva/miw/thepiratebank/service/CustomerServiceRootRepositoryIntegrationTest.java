package nl.hva.miw.thepiratebank.service;

import nl.hva.miw.thepiratebank.controller.AssetConsumerController;
import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.repository.CustomerDAO;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.repository.UserDAO;
import nl.hva.miw.thepiratebank.service.inputservice.*;
import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceRootRepositoryIntegrationTest {

    /*
    test de integratie van de customerservice met de rootrepository
    Mockt de relevante DAO's, instantieert de Customer service en de rootrepository
     */

    //Necessary to prevent postconstruct annotation from running and filling the database do not delete
    @MockBean
    AssetConsumerController assetConsumerController;

    @MockBean
    private UserDAO userDAO;

    @MockBean
    private CustomerDAO customerDAO;

    @MockBean
    private ExistingUserService existingUserService;


    private RootRepository rootRepository;
    private CustomerService customerService;
    private CustomerDTO correctDTO;
    private CustomerDTO correctDTOTwo;
    private CustomerDTO dtoBadEmailAddress;
    private CustomerDTO malformed;
    private Customer testCustomer;


    @Autowired
    public CustomerServiceRootRepositoryIntegrationTest(UserDAO userDAO, CustomerDAO customerDAO, RootRepository rootRepository,
                                                        CustomerService customerService, BcryptHashService bcryptHashService,
                                                        ExistingUserService existingUserService) {
        this.customerService = customerService;
        this.userDAO = userDAO;
        this.customerDAO = customerDAO;
        this.rootRepository = rootRepository;
        this.existingUserService = existingUserService;
    }


    @BeforeAll
    void setup() {
        correctDTO = createCorrectCustomerDTO();
        correctDTOTwo = createCorrectCustomerDTOTwo();
        dtoBadEmailAddress = createBadCustomerDTO();
        malformed = createMalformedJSONDTO();
        testCustomer = new Customer(0);
    }

    //should return a customer
    @Test
    void attemptCustomerRegistrationCorrectDTO() {
        Mockito.when(customerDAO.get(0)).thenReturn(testCustomer);
        Mockito.when(userDAO.getByUsername(correctDTO.getEmailadres())).thenReturn(testCustomer);
        Mockito.when(existingUserService.isExistingUser(correctDTO)).thenReturn("");
        Customer actual = customerService.attemptCustomerRegistration(correctDTO);
        assertThat(actual).isEqualTo(testCustomer);
    }

    //should return null
    @Test
    void attemptCustomerRegistrationRepositoryReturnsNull() {
        Mockito.when(customerDAO.get(0)).thenReturn(null);
        Mockito.when(userDAO.getByUsername(correctDTO.getEmailadres())).thenReturn(testCustomer);
        Mockito.when(existingUserService.isExistingUser(correctDTO)).thenReturn("");
        Customer actual = customerService.attemptCustomerRegistration(correctDTO);
        assertThat(actual).isEqualTo(null);
    }


    //should return null
    @Test
    void attemptCustomerRegistrationBadEmailAddressDTO() {
        Mockito.when(existingUserService.isExistingUser(dtoBadEmailAddress)).thenReturn("");
        Customer customer = customerService.attemptCustomerRegistration(dtoBadEmailAddress);
        assertThat(customer).isNull();
    }

    //should return null
    @Test
    void attemptCustomerRegistrationMalformedJSON() {
        Mockito.when(existingUserService.isExistingUser(malformed)).thenReturn("");
        Customer customer = customerService.attemptCustomerRegistration(malformed);
        assertThat(customer).isNull();
    }


   /////////////////////////////////////////////////////////////////////////// //helper methods to build DTO's

    public CustomerDTO createCorrectCustomerDTO() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmailadres("harry@harry.com");
        customerDTO.setWachtwoord("password");
        customerDTO.setVoornaam("Harry");
        customerDTO.setTussenvoegsel("de");
        customerDTO.setAchternaam("Wit");
        customerDTO.setGeboortedatum("1980-02-14");
        customerDTO.setBsnnummer("99999999");
        customerDTO.setPostcode("1430XY");
        customerDTO.setHuisnummertoevoeging("");
        customerDTO.setHuisnummer("1");
        customerDTO.setStraat("Oliemolen");
        customerDTO.setWoonplaats("Hoorn");
        customerDTO.setIbannummer("NL18RABO0123459876");
        return customerDTO;
    }

    public CustomerDTO createCorrectCustomerDTOTwo() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmailadres("barry@barry.com");
        customerDTO.setWachtwoord("password");
        customerDTO.setVoornaam("Harry");
        customerDTO.setTussenvoegsel("de");
        customerDTO.setAchternaam("Wit");
        customerDTO.setGeboortedatum("1980-02-14");
        customerDTO.setBsnnummer("99999999");
        customerDTO.setPostcode("1430XY");
        customerDTO.setHuisnummertoevoeging("");
        customerDTO.setHuisnummer("1");
        customerDTO.setStraat("Oliemolen");
        customerDTO.setWoonplaats("Hoorn");
        customerDTO.setIbannummer("NL18RABO0123459876");
        return customerDTO;
    }


    public CustomerDTO createBadCustomerDTO() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmailadres("harry@");
        customerDTO.setWachtwoord("password");
        customerDTO.setVoornaam("Harry");
        customerDTO.setTussenvoegsel("de");
        customerDTO.setAchternaam("Wit");
        customerDTO.setGeboortedatum("1999-12-01");
        customerDTO.setBsnnummer("99999999");
        customerDTO.setPostcode("1430XY");
        customerDTO.setHuisnummertoevoeging("");
        customerDTO.setHuisnummer("1");
        customerDTO.setStraat("Oliemolen");
        customerDTO.setWoonplaats("Hoorn");
        customerDTO.setIbannummer("NL18RABO0123459876");
        return customerDTO;
    }

    public CustomerDTO createMalformedJSONDTO() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmailadres(null);
        customerDTO.setWachtwoord("zszs");
        customerDTO.setVoornaam("Tim");
        customerDTO.setTussenvoegsel("de");
        customerDTO.setAchternaam("Wit");
        customerDTO.setGeboortedatum("1990-11-01");
        customerDTO.setBsnnummer("99999999");
        customerDTO.setPostcode("1430XY");
        customerDTO.setHuisnummertoevoeging("");
        customerDTO.setHuisnummer("1");
        customerDTO.setStraat("x");
        customerDTO.setWoonplaats("Hoorn");
        customerDTO.setIbannummer("NL18RABO0123459876");
        return customerDTO;
    }
}


