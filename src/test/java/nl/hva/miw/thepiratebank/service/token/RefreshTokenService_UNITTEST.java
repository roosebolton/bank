//ToDO fix unresolved references
//package nl.hva.miw.thepiratebank.service.Token;
//
//import nl.hva.miw.thepiratebank.domain.User;
//import nl.hva.miw.thepiratebank.service.Token.RefreshToken.RefreshToken;
//import nl.hva.miw.thepiratebank.service.Token.RefreshToken.RefreshTokenPayload;
//import nl.hva.miw.thepiratebank.service.persistence.RefreshTokenDatabase;
//import nl.hva.miw.thepiratebank.utilities.authorization.token.RefreshTokenService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.Assert;
//
//import java.util.Date;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class RefreshTokenService_UNITTEST {
//
//    private RefreshTokenService refreshTokenService;
//    private RefreshTokenDatabase refreshTokenDatabase = Mockito.mock(RefreshTokenDatabase.class);
//    private RefreshTokenPayload refreshTokenPayload;
//    private RefreshToken refreshToken;
//    private RefreshTokenPayload refreshTokenPayload2;
//    private RefreshToken refreshToken2;
//
//
//    @Autowired
//    public RefreshTokenService_UNITTEST() {
//        this.refreshTokenService = new RefreshTokenService(refreshTokenDatabase);
//    }
//
//    @BeforeEach
//    void setUp() {
//        refreshTokenPayload = new RefreshTokenPayload(1);
//        refreshToken = new RefreshToken("token-key",refreshTokenPayload);
//        Mockito.when(refreshTokenService.getToken("token-key")).thenReturn(refreshToken);
//        Mockito.when(refreshTokenService.getToken("andere-token-key")).thenReturn(null);
//    }
//
//    @Test
//    void isValid_True() {
//        boolean actual = refreshTokenService.isValid(refreshToken.getRefreshTokenKey());
//        boolean expected = true;
//        assertThat(actual).isNotNull().isEqualTo(expected);
//    }
//
//    @Test
//    void isValid_False_TOKEN_EXPIRED() {
//        refreshTokenPayload.setExpiryDate(new Date(System.currentTimeMillis()-100));
//        boolean actual = refreshTokenService.isValid(refreshToken.getRefreshTokenKey());
//        boolean expected = false;
//        assertThat(actual).isNotNull().isEqualTo(expected);
//    }
//
//    @Test
//    void isValid_False_KEY_NOT_FOUND() {
//        boolean actual = refreshTokenService.isValid("andere-token-key");
//        boolean expected = false;
//        assertThat(actual).isNotNull().isEqualTo(expected);
//    }
//
//    @Test
//    void getToken_True() {
//        Mockito.when(refreshTokenDatabase.findRefreshTokenByKey("token-key")).thenReturn(refreshToken);
//        RefreshToken actual = refreshTokenService.getToken("token-key");
//        RefreshToken refreshTokenexpected = new RefreshToken(new RefreshTokenPayload(1));
//        refreshTokenexpected.setRefreshTokenKey("token-key");
//        RefreshToken expected =refreshTokenexpected;
//        assertThat(actual).isNotNull().isEqualTo(expected);
//    }
//
//    @Test
//    void getToken_false() {
//        Mockito.when(refreshTokenDatabase.findRefreshTokenByKey("andere-token")).thenReturn(refreshToken2);
//        RefreshToken actual = refreshTokenService.getToken("token-key");
//
//        RefreshToken expected =refreshToken2;
//        assertThat(actual).isNotNull().isEqualTo(expected);
//    }
//
//    @Test
//    void getTokenPayload() {
//
//    }
//
//    @Test
//    void createToken() {
//        //hier alleen checken of hij wat krijgt
//        User user = new User("admin@admin.com","1234");
//        Assert.notNull(refreshTokenService.createToken(user),"null");
//    }
//
//    @Test
//    void blockToken() {
//
//
//    }
//}
//
////    public void blockToken(String key) {
////        refreshTokenDatabase.findRefreshTokenByKey(key).getPayload().setActive(false);
////    }
//
