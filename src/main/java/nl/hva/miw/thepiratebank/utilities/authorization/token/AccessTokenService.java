package nl.hva.miw.thepiratebank.utilities.authorization.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.utilities.authorization.token.accessToken.JWTCreator;
import nl.hva.miw.thepiratebank.utilities.exceptions.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class AccessTokenService implements TokenService<String, DecodedJWT> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final JWTVerifier jwtVerifier;
    private final JWTCreator jwtCreator;

    public AccessTokenService(JWTCreator jwtCreator) {
        this.jwtVerifier = buildJWTVerifier();
        this.jwtCreator = jwtCreator;
    }

    public JWTVerifier buildJWTVerifier() {
        Algorithm algorithm = Algorithm.HMAC256(JWTCreator.getSignatureSecret());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(JWTCreator.getIssuerToken())
                .build();
        return verifier;
    }

    public boolean isValid(String token) {
        try {
            DecodedJWT jwt =jwtVerifier.verify(token);
           return true;
        } catch (JWTVerificationException exception) {
           log.debug(exception.getMessage());
        }
        return false;
    }

    @Override
    public DecodedJWT getTokenPayload(String key) {
        try {
            DecodedJWT jwt = jwtVerifier.verify(key);
            return jwt;
        } catch (JWTVerificationException exception) {
            log.debug(exception.getMessage());
        } return null;
    }

    public String getUserIdFromToken (String token) {
        try {
            return jwtVerifier.verify(token).getClaims().get("id").toString();
        } catch (JWTVerificationException exception) {
           log.debug(exception.getMessage());
        } return null;
    }

    public boolean validateUserIdInToken(String token, Integer userId) {
        try {
            return (userId.toString().equals(jwtVerifier.verify(token).getClaims().get("id").toString()));
        } catch (JWTVerificationException exception) {
            throw new AuthorizationException("Invalid Token");
        }
    }

    public String getNewToken(User user) {
        return jwtCreator.createToken(user);
    }

    public Optional<Integer> getTokenFromRequest (HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String[] strippedToken = authHeader.split(" ");
            int userId = Integer.parseInt(getUserIdFromToken(strippedToken[1]));
            return Optional.ofNullable(userId);
        } else return Optional.ofNullable(null);
    }

}
