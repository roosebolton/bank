package nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken.repository;

import nl.hva.miw.thepiratebank.repository.GenericDAOWithOptional;
import nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken.RefreshToken;

import java.util.Optional;

public abstract class RefreshTokenAbstractDAO implements GenericDAOWithOptional<RefreshToken,String> {

    /** abtract class if more methods are needed **/

    public abstract void blockToken(String token);

    public abstract Optional<RefreshToken> findActiveByUserid(int userId);


}
