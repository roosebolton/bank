package nl.hva.miw.thepiratebank.controller;//
//package nl.hva.miw.thepiratebank.controller;
//
//import nl.hva.miw.thepiratebank.controller.AssetConsumerController;
//import nl.hva.miw.thepiratebank.utilities.authorization.AuthTokenFilter;
//import nl.hva.miw.thepiratebank.service.LoginService;
//import nl.hva.miw.thepiratebank.service.Token.AccessTokenService;
//import nl.hva.miw.thepiratebank.service.Token.RefreshTokenService;
//import nl.hva.miw.thepiratebank.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(AuthenticationController.class)
//@ExtendWith(SpringExtension.class)
//class AuthenticationController_Integration {
//
//
//    @Autowired
//    private AuthenticationController authenticationController;
//    @Autowired
//    private AccessTokenService accessTokenService;
//    @Autowired
//    private RefreshTokenService refreshTokenService;
//    @Autowired
//    private LoginService loginService;
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private MockMvc mvc;
//
//    public AuthenticationController_Integration() {
//        this.authenticationController = new AuthenticationController(accessTokenService,refreshTokenService,loginService,userService);
//    }
//
//    //    public AuthenticationController_Integration() {
////        authenticationController = new AuthenticationController(AccessTokenService)
////    }
//
//    //    @MockBean
////    private AccessTokenService accessTokenService;
////    @MockBean
////    private RefreshTokenService refreshTokenService;
////    @MockBean
////    private LoginService loginService;
////    @MockBean
////    private UserService userService;
//
//    @BeforeEach
//    void setUp () {
//        mvc = MockMvcBuilders
//                .standaloneSetup(authenticationController)
//                .build();
//
//    }
//
//    @Test
//    void authenticationHandler_Success() {
//        try {
//            mvc.perform( MockMvcRequestBuilders
//                            .post("/users/authenticate").
//                            contentType(MediaType.APPLICATION_JSON)
//                            .content("{\n" +
//                            "    \"username\":\"admin@admin.com\",\n" +
//                            "    \"password\":\"1234\"\n" +
//                            "}")
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andDo(print())
//                    .andExpect(status().isOk())
//                    .andExpect(MockMvcResultMatchers.header().exists("Authorization"))
//                    .andExpect(MockMvcResultMatchers.header().exists("Refresh_Token"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void authenticationHandler_Failure() {
//        try {
//            mvc.perform( MockMvcRequestBuilders
//                            .post("/users/authenticate").
//                            contentType(MediaType.APPLICATION_JSON)
//                            .content("{\n" +
//                                    "    \"username\":\"admin3@admin.com\",\n" +
//                                    "    \"password\":\"1234\"\n" +
//                                    "}")
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andDo(print())
//                    .andExpect(status().is(401));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    void refreshHandler() {
//        try {
//            mvc.perform( MockMvcRequestBuilders
//                            .post("/users/authenticate/refresh").
//                            contentType(MediaType.APPLICATION_JSON)
//                            .content("{\n" +
//                                    "    \"username\":\"admin@admin.com\",\n" +
//                                    "    \"password\":\"1234\"\n" +
//                                    "}")
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andDo(print())
//                    .andExpect(status().is(401))
//                    .andExpect(MockMvcResultMatchers.header().exists("Authorization"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
//
