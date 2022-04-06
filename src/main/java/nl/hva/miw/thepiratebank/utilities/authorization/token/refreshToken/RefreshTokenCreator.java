package nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken;

import nl.hva.miw.thepiratebank.domain.User;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCreator {
    public  RefreshToken createToken (User user) {
        return new RefreshToken( new RefreshTokenPayload(user.getUserId()));
    }
}

