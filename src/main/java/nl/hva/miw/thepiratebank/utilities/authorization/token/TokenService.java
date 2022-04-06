package nl.hva.miw.thepiratebank.utilities.authorization.token;

import nl.hva.miw.thepiratebank.domain.User;

/**
 * Interface for validating and reading payload of tokens
 *
 * @param <T>Type token
 * @param <P> Payload object of token
 */
public interface TokenService <T,P> {

   boolean isValid(String token);

    P getTokenPayload (String key);

    T getNewToken(User user);

}
