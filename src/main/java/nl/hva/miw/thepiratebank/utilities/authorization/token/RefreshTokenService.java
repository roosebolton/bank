package nl.hva.miw.thepiratebank.utilities.authorization.token;

import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken.RefreshToken;
import nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken.RefreshTokenCreator;
import nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken.repository.RefreshRefreshTokenDAOJDBCImpl;
import nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken.RefreshTokenPayload;
import nl.hva.miw.thepiratebank.utilities.exceptions.AuthorizationException;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;


@Service
public class RefreshTokenService implements TokenService<RefreshToken, RefreshTokenPayload> {
    public final static long REFRESHTOKEN_STANDARD_EXPIRY_DATE_MS = 604800000;
    private final RefreshRefreshTokenDAOJDBCImpl refreshTokenDAOJDBC;
    private final RefreshTokenCreator refreshTokenCreator;

    public RefreshTokenService(RefreshRefreshTokenDAOJDBCImpl refreshTokenDAOJDBC, RefreshTokenCreator refreshTokenCreator) {
        this.refreshTokenDAOJDBC = refreshTokenDAOJDBC;
        this.refreshTokenCreator = refreshTokenCreator;
    }

    /** Validates refresh token based on following factors
     * presence of token in database, token expiry_date, and token set to active
     * @param token
     * @return boolean
     */
    @Override
    public boolean isValid(String token) {
       RefreshToken refreshToken =  refreshTokenDAOJDBC.find(token)
               .orElseThrow(() ->new AuthorizationException("Invalid token"));
       if ( new Date(System.currentTimeMillis())
                        .after(refreshToken.getPayload().getExpiryDate()) ||
                !refreshToken.getPayload().isActive()) {
           return false;
       } else return true;

    }

    public RefreshToken getToken(String key) throws AuthorizationException {
        return refreshTokenDAOJDBC.find(key).orElseThrow(() ->new AuthorizationException("Invalid token"));
    }

    @Override
    public RefreshTokenPayload getTokenPayload(String token) {
        return refreshTokenDAOJDBC.find(token).map(e -> e.getPayload()).orElse(null);
    }

    @Override
    public RefreshToken getNewToken(User user) {
        // deactivate previous token of user if present
        blockToken(refreshTokenDAOJDBC.findActiveByUserid(user.getUserId()).map(e -> e.getRefreshTokenKey()).orElse(null));
        // save token in database
        RefreshToken generatedtoken = refreshTokenCreator.createToken(user);
        refreshTokenDAOJDBC.save(generatedtoken);
        // return generated token
        return generatedtoken;
    }

    public void blockToken(String key) {
        if (key != null) {
            refreshTokenDAOJDBC.blockToken(key);
        }
    }
}
