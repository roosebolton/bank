package nl.hva.miw.thepiratebank.controller;

import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import nl.hva.miw.thepiratebank.utilities.authorization.token.RefreshTokenService;
import nl.hva.miw.thepiratebank.utilities.exceptions.AuthorizationException;
import nl.hva.miw.thepiratebank.utilities.exceptions.ConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public abstract class BaseController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected RefreshTokenService refreshTokenService;
    protected AccessTokenService accessTokenService;


    public BaseController(RefreshTokenService refreshTokenService, AccessTokenService accessTokenService) {
        this.refreshTokenService = refreshTokenService;
        this.accessTokenService = accessTokenService;
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
        public String handleAuthorizationException(AuthorizationException ex) {
        return ex.getMessage();

    }

    @ExceptionHandler(ConflictException.class)
    public String handleNotFoundException(String message) {
        throw new ResponseStatusException(HttpStatus.CONFLICT,message);

    }


}
