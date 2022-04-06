package nl.hva.miw.thepiratebank.controller;

import nl.hva.miw.thepiratebank.service.AccountService;
import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(value = AccountController.class)
class AccountControllerTest {

    private MockMvc mockServer;

    @MockBean
    private AccountService accountService;
    @MockBean
    private AccessTokenService accessTokenService;

    @Autowired
    public AccountControllerTest(MockMvc mockServer) {
        this.mockServer = mockServer;
    }




}