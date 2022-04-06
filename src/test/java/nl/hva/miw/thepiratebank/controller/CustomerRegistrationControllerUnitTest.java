package nl.hva.miw.thepiratebank.controller;

import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.domain.customerattributes.Address;
import nl.hva.miw.thepiratebank.domain.customerattributes.IdentifyingInformation;
import nl.hva.miw.thepiratebank.domain.customerattributes.PersonalDetails;
import nl.hva.miw.thepiratebank.service.CustomerService;
import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import nl.hva.miw.thepiratebank.service.UserService;
import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(value = CustomerRegistrationController.class)
class CustomerRegistrationControllerUnitTest {

    private MockMvc mockServer;

    @Autowired
    private CustomerRegistrationController customerRegistrationController;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private UserService userService;

    @MockBean
    private AccessTokenService accessTokenService;

    @MockBean
    CustomerDTO customerDTO;

    @MockBean
    private Customer testCustomer;

    private MockHttpServletRequestBuilder httpPostRequest;


    @BeforeAll
    void setup() {
        mockServer = MockMvcBuilders.standaloneSetup(customerRegistrationController).build();
        PersonalDetails personalDetails = new PersonalDetails("Harry", "de", "Wit");
        Address address = new Address("Oliemolen", "1", "", "1430XY", "Hoorn");
        IdentifyingInformation identifyingInformation = new IdentifyingInformation(99999999,
                "NL88RABO0549857483", LocalDate.parse("1980-02-14"));
        testCustomer = new Customer("harry@harry.com", "password", personalDetails, address, identifyingInformation);
        testCustomer.setUserId(1);
        httpPostRequest = post("/user/register").content("{\n" + "\"emailadres\": \"harry@harry.com\",\n" +
                "\"wachtwoord\": \"password\",\n" + "\"voornaam\": \"Harry\",\n" + "\"tussenvoegsel\": \"de\",\n" +
                "\"achternaam\": \"Wit\",\n" + "\"geboortedatum\": \"1980-02-14\",\n" + "\"bsnnummer\": \"99999999\",\n" +
                "\"postcode\": \"1430XY\",\n" + "\"huisnummer\": \"1\",\n" + "\"huisnummertoevoeging\": \"\",\n" +
                "\"straat\": \"Oliemolen\",\n" + "\"woonplaats\": \"Hoorn\",\n" + "\"ibannummer\": \"NL88RABO0549857483\"\n" +
                "}");

        httpPostRequest.contentType(MediaType.APPLICATION_JSON);
    }



    @Test
    void CorrectRequestShould201() {
        setup();
        //indien de request klopt geeft de service altijd een Customer object terug
        Mockito.when(customerService.attemptCustomerRegistration(any(CustomerDTO.class))).thenReturn(testCustomer);
        try {
            ResultActions response = mockServer.perform(httpPostRequest);
            response.andExpect(status().isCreated()).andDo(print());
            String responsebody = response.andReturn().getResponse().getContentAsString();
            String locationheader = response.andReturn().getResponse().getHeader("Location");
            assertThat(responsebody).isEqualTo("Klant geregistreerd");
            assertThat(locationheader).isEqualTo("user/1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void BadRequestShould400() {
        //indien er iets niet klopt aan de request geeft de service altijd een null object terug
        Mockito.when(customerService.attemptCustomerRegistration(any(CustomerDTO.class))).thenReturn(null);
        try {
            mockServer.perform(httpPostRequest).andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void clientSendsEmptyRequestShould400() {
        httpPostRequest = post("/user/register").content("");
        try {
            mockServer.perform(httpPostRequest).andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void clientSendsMalformedRequestShould400() {
        httpPostRequest = post("/user/register").content("\"{\\n\" + \"\\\"emailadres\\\": \\\"harry@harry.com\\\",\\n\" +\n" +
                "\"\\\"wachtwoor\": \\\"password\\\",voornaam\\\": \\\"Harry\\\",\\n\" + \"\\\"tussenvoegsel\\\": \\\"de\\\",\\n\" +\n" +
                "\"\\\"achternaams\\\": \\\"Wit\\\",\\n\" + \"\\\"geboortedatum\\\": 1980-02-14\\\",\\n\" + \"\\\"bsnnummer\\\": \\\"99999999\\\",\\n\" +\n" +
                "\"\\\"postcode\\\": \\\"1430XY\\\",\\n\" + \"\\\"huisnummer\\\": \\\"1\\\",\\n\" + \"\\\"huisnummertoevoeging\\\": \\\"\\\",\\n\" +\n" +
                "\"\\\"straat\\\": \\\"Oliemolen\\\",\\n\" + \"\\\"woonplaats\\\": \\\"Hoorn\\\",\\n\" + \"\\\"ibannummer\\\": \\\"NL88RABO0549857483\\\"\\n\" +\n" +
                "\"}\"");
        httpPostRequest.contentType(MediaType.APPLICATION_JSON);
        try {
            mockServer.perform(httpPostRequest).andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void clientSendsEmptyJSONShould400() {
        post("/user/register").content("{\n" + "\"emailadres\": \"\",\n" +
                "\"wachtwoord\": \"\",\n" + "\"voornaam\": \"\",\n" + "\"tussenvoegsel\": \"\",\n" +
                "\"achternaam\": \"\",\n" + "\"geboortedatum\": \"\",\n" + "\"bsnnummer\": \"\",\n" +
                "\"postcode\": \"1430XY\",\n" + "\"huisnummer\": \"1\",\n" + "\"huisnummertoevoeging\": \"\",\n" +
                "\"straat\": \"\",\n" + "\"woonplaats\": \"Hoorn\",\n" + "\"\": \"NL88RABO0549857483\"\n" +
                "}");
        try {
            mockServer.perform(httpPostRequest).andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
