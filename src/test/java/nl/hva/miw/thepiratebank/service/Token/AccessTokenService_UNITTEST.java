package nl.hva.miw.thepiratebank.service.Token;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import nl.hva.miw.thepiratebank.utilities.authorization.token.accessToken.JWTCreator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccessTokenService_UNITTEST {
    private AccessTokenService accessTokenService;
    private JWTVerifier jwtVerifier;
    private JWTCreator jwtCreator;


    public AccessTokenService_UNITTEST() {
        this.accessTokenService = new AccessTokenService(jwtCreator);
    }

    @Test
    void isValidTestValid() {
        String JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUaGVQaXJhdGVCYW5rIiwiaWQiOjMsImV4cCI6MjI0NjY3NjMzNSwiaWF0IjoxNjQ2Njc2MzM1fQ.8ZrE9MLmr7mCHyMn9iI-0c__8-312A4AM95T8yvaPLY";
        boolean expected = true;
        boolean actual = accessTokenService.isValid(JWT);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void isValidTestInvalidModifiedToken() {
        //modified token in string
        String JWT = "eeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUaGVQaXJhdGVCYW5rIiwiaWQiOjYsImV4cCI6MjI0NjY3NjMzNSwiaWF0IjoxNjQ2Njc2MzM1fQ.ia7O5KqhwHlRP0RfSuEi80xICSZWjRJXrnf8Y3CWUE4";
        boolean expected = false;
        boolean actual = accessTokenService.isValid(JWT);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void isValidTestInvalidWrongIssuerToken() {
        //changed issuer
        String JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUaGVQaXJhdGVCYW5rVmVyYW5kZXJkIiwiaWQiOjMsImV4cCI6MjI0NjY3NjMzNSwiaWF0IjoxNjQ2Njc2MzM1fQ.oieUHXY5fqMj7G1DM2OCIZIFKG4MGwdLC3gwZFLYGBs";
        boolean expected = false;
        boolean actual = accessTokenService.isValid(JWT);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void getTokenPayload_ModfiedToken_Null() {
        String JWT = "eeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUaGVQaXJhdGVCYW5rIiwiaWQiOjYsImV4cCI6MjI0NjY3NjMzNSwiaWF0IjoxNjQ2Njc2MzM1fQ.ia7O5KqhwHlRP0RfSuEi80xICSZWjRJXrnf8Y3CWUE4";
        DecodedJWT actual = accessTokenService.getTokenPayload(JWT);
        DecodedJWT expected = null;
        assertThat(actual).isEqualTo(expected);
    }
}