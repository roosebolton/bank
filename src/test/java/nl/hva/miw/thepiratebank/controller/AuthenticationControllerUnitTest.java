package nl.hva.miw.thepiratebank.controller;

import nl.hva.miw.thepiratebank.domain.User;
//import nl.hva.miw.thepiratebank.utilities.authorization.AuthTokenFilter;
import nl.hva.miw.thepiratebank.service.LoginService;
import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken.RefreshToken;
import nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken.RefreshTokenPayload;
import nl.hva.miw.thepiratebank.utilities.authorization.token.RefreshTokenService;
import nl.hva.miw.thepiratebank.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;


@WebMvcTest(value = AuthenticationController.class)
class AuthenticationControllerUnitTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MockMvc mockServer;

    @Autowired
    private AuthenticationController authenticationController;

    @MockBean
    private AccessTokenService accessTokenService;
    @MockBean
    private RefreshTokenService refreshTokenService;
    @MockBean
    private LoginService loginService;
    @MockBean
    private UserService userService;

    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        mockServer = MockMvcBuilders
                .standaloneSetup(authenticationController)
                .build();

        User testUser = new User("admin@admin.com", "1234");
        testUser.setUserId(1);
        refreshToken = new RefreshToken(new RefreshTokenPayload(1));

        Mockito.when(loginService.getUserValidLogin("admin@admin.com", "1234")).thenReturn(testUser);
        Mockito.when(refreshTokenService.getNewToken(testUser)).thenReturn(refreshToken);
        Mockito.when(accessTokenService.getNewToken(testUser)).thenReturn("TESTAUTHORIZATIONHEADER");

    }

    @Test
    void authenticationHandlerTest_valid_OkResponse() {
        MockHttpServletRequestBuilder request;
        request = MockMvcRequestBuilders.post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)//
                .content("{\n" +
                        "    \"username\":\"admin@admin.com\",\n" +
                        "    \"password\":\"1234\"\n" +
                        "}");

        try {
            ResultActions response = mockServer.perform(request);
            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.header().exists("Refresh_Token"))
                    .andExpect(MockMvcResultMatchers.header().exists("AUTHORIZATION"))
                    .andDo(MockMvcResultHandlers.print());

            MvcResult result = response.andReturn();
            String contents = result.getResponse().getContentAsString();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }



    @Test
    void authenticationHandlerTest_Invalid_401Response() {
        MockHttpServletRequestBuilder request;
        request = MockMvcRequestBuilders.post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)//
                .content("{\n" +
                        "    \"username\":\"admin3@admin.com\",\n" +
                        "    \"password\":\"1234\"\n" +
                        "}");
        try {
            ResultActions response = mockServer.perform(request);
            response.andExpect(MockMvcResultMatchers.status().is(401))
                    .andDo(MockMvcResultHandlers.print());
            MvcResult result = response.andReturn();
            String contents = result.getResponse().getContentAsString();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    @Test
    void refreshTokenHandlerTest_Valid_OKresponse() {
        User testUser = new User("admin@admin.com", "1234");
        testUser.setUserId(1);
        RefreshTokenPayload refreshTokenPayload = new RefreshTokenPayload(1);
        refreshTokenPayload.setExpiryDate(new Date(	1678220701));
        refreshToken = new RefreshToken(refreshTokenPayload);
        refreshToken.setRefreshTokenKey("9c2c777b-c569-494b-a162-c2f780f161aa");

        Mockito.when(refreshTokenService.getToken("9c2c777b-c569-494b-a162-c2f780f161aa")).thenReturn(refreshToken);
        Mockito.when(refreshTokenService.isValid("9c2c777b-c569-494b-a162-c2f780f161aa")).thenReturn(true);
        Mockito.when(userService.getUserByUserId(1)).thenReturn(testUser);

        Mockito.when(userService.getUserByUserName("admin@admin.com")).thenReturn(testUser);
        Mockito.when(accessTokenService.getNewToken(testUser)).thenReturn("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUaGVQaXJhdGVCYW5rIiwiaWQiOjMsImV4cCI6MjI0NjY3NjMzNSwiaWF0IjoxNjQ2Njc2MzM1fQ.8ZrE9MLmr7mCHyMn9iI-0c__8-312A4AM95T8yvaPLY");

        MockHttpServletRequestBuilder request;
        request = MockMvcRequestBuilders.get("/users/authenticate/refresh");
        request.contentType(MediaType.APPLICATION_JSON)
                .header("Refresh_Token", "9c2c777b-c569-494b-a162-c2f780f161aa").content("");

        try {
            ResultActions response = mockServer.perform(request);
            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.header().exists("AUTHORIZATION"))
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    @Test
    void refreshTokenHandlerTest_invalid_401response() {
        User testUser = new User("admin@admin.com", "1234");
        testUser.setUserId(1);
        RefreshTokenPayload refreshTokenPayload = new RefreshTokenPayload(1);
        refreshTokenPayload.setExpiryDate(new Date(	1678220701));
        refreshToken = new RefreshToken(refreshTokenPayload);
        refreshToken.setRefreshTokenKey("9c2c777b-c569-494b-a162-c2f780f161aa");

        Mockito.when(refreshTokenService.getToken("9c2c777b-c569-494b-a162-c2f780f161aa")).thenReturn(null);
        Mockito.when(refreshTokenService.isValid("9c2c777b-c569-494b-a162-c2f780f161aa")).thenReturn(false);

        Mockito.when(userService.getUserByUserName("admin@admin.com")).thenReturn(testUser);
        Mockito.when(accessTokenService.getNewToken(testUser)).thenReturn("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUaGVQaXJhdGVCYW5rIiwiaWQiOjMsImV4cCI6MjI0NjY3NjMzNSwiaWF0IjoxNjQ2Njc2MzM1fQ.8ZrE9MLmr7mCHyMn9iI-0c__8-312A4AM95T8yvaPLY");

        MockHttpServletRequestBuilder request;
        request = MockMvcRequestBuilders.get("/users/authenticate/refresh");
        request.contentType(MediaType.APPLICATION_JSON)
                .header("Refresh_Token", "9c2c777b-c569-494b-a162-c2f780f161aa").content("");

        try {
            ResultActions response = mockServer.perform(request);
            response.andExpect(MockMvcResultMatchers.status().is(401))
                    .andDo(MockMvcResultHandlers.print());
            MvcResult result = response.andReturn();
            result.getResponse().getHeader("Authorization");
            String contents = result.getResponse().getContentAsString();

        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

}