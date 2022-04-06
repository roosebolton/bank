//package nl.hva.miw.thepiratebank.service;
//
//import nl.hva.miw.thepiratebank.domain.Customer;
//import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
//import nl.hva.miw.thepiratebank.repository.RootRepository;
//import nl.hva.miw.thepiratebank.service.inputservice.ExistingUserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//class CustomerServiceUnitTest {
//
//    RootRepository rootRepository = Mockito.mock(RootRepository.class);
//    BcryptHashService bcryptHashService = Mockito.mock(BcryptHashService.class);
//    ExistingUserService existingUserService = Mockito.mock(ExistingUserService.class);
//    AccountService accountService = Mockito.mock(AccountService.class);
//
//    CustomerService customerService = new CustomerService(rootRepository,bcryptHashService,existingUserService,accountService);
//
//    Customer testCustomer;
//    CustomerDTO correct;
//    CustomerDTO incorrect;
//    CustomerDTO malformed;
//
//    @BeforeEach
//    void setUp() {
//        testCustomer = new Customer(1);
//        correct = createCorrectCustomerDTO();
//        incorrect = createBadCustomerDTO();
//        malformed = createMalformedJSONDTO();
//    }
//
//    //should just return a customer when it exists
//    @Test
//    void getCustomerById() {
//        Mockito.when(rootRepository.getCustomer(1)).thenReturn(testCustomer);
//        Customer expected = testCustomer;
//        Customer actual = customerService.getCustomerById(1);
//        assertThat(actual).isEqualTo(expected);
//    }
//
//    //rootrepository returns null, should not return a customer
//    @Test
//    void getCustomerByIdRepositoryReturnsNull() {
//        Mockito.when(rootRepository.getCustomer(1)).thenReturn(null);
//        Customer actual = customerService.getCustomerById(1);
//        assertThat(actual).isNull();
//    }
//
//    //should return a customer
//    @Test
//    void attemptCustomerRegistrationCorrectDTO() {
//        Mockito.when(existingUserService.isExistingUser(correct)).thenReturn("");
//        Mockito.when(rootRepository.getByUserName(correct.getEmailadres())).thenReturn(testCustomer);
//        Mockito.when(rootRepository.getCustomer(1)).thenReturn(testCustomer);
//        Customer actual = customerService.attemptCustomerRegistration(correct);
//        assertThat(actual).isEqualTo(testCustomer);
//    }
//
//    //should return null
//    @Test
//    void attemptCustomerRegistrationIncorrectDTO() {
//        Mockito.when(existingUserService.isExistingUser(incorrect)).thenReturn("");
//        Customer actual = customerService.attemptCustomerRegistration(incorrect);
//        assertThat(actual).isEqualTo(null);
//    }
//
//    //should also return null
//    @Test
//    void attemptCustomerRegistrationMalformedDTO() {
//        Mockito.when(existingUserService.isExistingUser(malformed)).thenReturn("");
//        Customer actual = customerService.attemptCustomerRegistration(malformed);
//        assertThat(actual).isEqualTo(null);
//
//    }
//
//    //should not return anything
//    @Test
//    void buildInputFieldErrorStringCorrectDTO() {
//        Mockito.when(existingUserService.isExistingUser(correct)).thenReturn("");
//        String actual = customerService.buildInputFieldErrorString(correct);
//        String expected = "";
//        assertThat(actual).isEqualTo(expected);
//    }
//
//    //should return an e-mailadress error
//    @Test
//    void buildInputFieldErrorStringIncorrectEmailAddressDTO() {
//        Mockito.when(existingUserService.isExistingUser(incorrect)).thenReturn("");
//        String actual = customerService.buildInputFieldErrorString(incorrect);
//        assertThat(actual).isNotEmpty().isEqualTo("Verplicht veld. Er is geen correct emailadres ingevuld\n");
//    }
//
//
//    //should return malformed json error
//    @Test
//    void buildInputFieldErrorStringMalformedJSON() {
//        String actual = customerService.buildInputFieldErrorString(malformed);
//        assertThat(actual).isEqualTo("JSON format incorrect, zie de documentatie voor het juiste format");
//
//    }
//
//    //should only return true when the error string == "Dit e-mailadres is al geregistreerd"
//    @Test
//    void isUserAlreadyRegistered() {
//        boolean actual1 = customerService.isUserAlreadyRegistered("Dit e-mailadres is al geregistreerd");
//        boolean actual2 = customerService.isUserAlreadyRegistered("this string should return false");
//        boolean actual3 = customerService.isUserAlreadyRegistered("");
//
//        assertThat(actual1).isTrue();
//        assertThat(actual2).isFalse();
//        assertThat(actual3).isFalse();
//    }
//
//
//    //////////////////////////////////////////////////////////////////////////////helper methods for constructing DTO's
//
//    public CustomerDTO createCorrectCustomerDTO() {
//        CustomerDTO customerDTO = new CustomerDTO();
//        customerDTO.setEmailadres("harry@harry.com");
//        customerDTO.setWachtwoord("password");
//        customerDTO.setVoornaam("Harry");
//        customerDTO.setTussenvoegsel("de");
//        customerDTO.setAchternaam("Wit");
//        customerDTO.setGeboortedatum("1980-02-14");
//        customerDTO.setBsnnummer("99999999");
//        customerDTO.setPostcode("1430XY");
//        customerDTO.setHuisnummertoevoeging("");
//        customerDTO.setHuisnummer("1");
//        customerDTO.setStraat("Oliemolen");
//        customerDTO.setWoonplaats("Hoorn");
//        customerDTO.setIbannummer("NL18RABO0123459876");
//        return customerDTO;
//    }
//
//    public CustomerDTO createBadCustomerDTO() {
//        CustomerDTO customerDTO = new CustomerDTO();
//        customerDTO.setEmailadres("harry@");
//        customerDTO.setWachtwoord("password");
//        customerDTO.setVoornaam("Harry");
//        customerDTO.setTussenvoegsel("de");
//        customerDTO.setAchternaam("Wit");
//        customerDTO.setGeboortedatum("1999-12-01");
//        customerDTO.setBsnnummer("99999999");
//        customerDTO.setPostcode("1430XY");
//        customerDTO.setHuisnummertoevoeging("");
//        customerDTO.setHuisnummer("1");
//        customerDTO.setStraat("Oliemolen");
//        customerDTO.setWoonplaats("Hoorn");
//        customerDTO.setIbannummer("NL18RABO0123459876");
//        return customerDTO;
//    }
//
//    public CustomerDTO createMalformedJSONDTO() {
//        CustomerDTO customerDTO = new CustomerDTO();
//        customerDTO.setEmailadres(null);
//        customerDTO.setWachtwoord("zszs");
//        customerDTO.setVoornaam("Tim");
//        customerDTO.setTussenvoegsel("de");
//        customerDTO.setAchternaam("Wit");
//        customerDTO.setGeboortedatum("1990-11-01");
//        customerDTO.setBsnnummer("99999999");
//        customerDTO.setPostcode("1430XY");
//        customerDTO.setHuisnummertoevoeging("");
//        customerDTO.setHuisnummer("1");
//        customerDTO.setStraat("x");
//        customerDTO.setWoonplaats("Hoorn");
//        customerDTO.setIbannummer("NL18RABO0123459876");
//        return customerDTO;
//    }
//
//
//
//
//}