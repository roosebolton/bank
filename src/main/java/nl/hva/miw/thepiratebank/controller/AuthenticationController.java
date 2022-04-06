package nl.hva.miw.thepiratebank.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.service.LoginService;
import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import nl.hva.miw.thepiratebank.utilities.authorization.token.refreshToken.RefreshToken;
import nl.hva.miw.thepiratebank.utilities.authorization.token.RefreshTokenService;
import nl.hva.miw.thepiratebank.service.UserService;
import nl.hva.miw.thepiratebank.domain.transfer.LoginDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping(path = "users")
public class AuthenticationController extends BaseController{

    private final LoginService loginService;
    private final UserService userService;

    public AuthenticationController(RefreshTokenService refreshTokenService,  AccessTokenService accessTokenService, LoginService loginService, UserService userService) {
        super(refreshTokenService, accessTokenService);
        this.loginService = loginService;
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticationHandler(@RequestBody LoginDTO loginDTO) {
        User loggedUser = loginService.getUserValidLogin(loginDTO.getUsername(), loginDTO.getPassword());
        if (loggedUser == null) {
            JsonObject response = new JsonObject();
            response.addProperty("Error","Combinatie gebruikersnaam/wachtwoord niet gevonden.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).location(URI.create("/login/login.html")).body(response.toString());
        } else {
            RefreshToken refreshToken = refreshTokenService.getNewToken(loggedUser);
            JsonObject response = new JsonObject();
            response.addProperty("id", loggedUser.getUserId());
            response.addProperty("Refresh_Token", refreshToken.getRefreshTokenKey());
            response.addProperty("Authorization", accessTokenService.getNewToken(loggedUser));
            Gson gson = new Gson();
            return ResponseEntity.ok().body(gson.toJson(response));
        }
    }

    @GetMapping("/authenticate/refresh")
    public ResponseEntity<?> refreshHandler(@RequestHeader("Refresh_Token") String refreshTokenKey) {
        if (refreshTokenKey != null && refreshTokenService.isValid(refreshTokenKey)) {
            int userId = refreshTokenService.getToken(refreshTokenKey).getPayload().getUserId();
            JsonObject response = new JsonObject();
            response.addProperty("Authorization", accessTokenService.getNewToken(userService.getUserByUserId(userId)));
            Gson gson = new Gson();
            return ResponseEntity.ok().body(gson.toJson(response));
        } else
            return ResponseEntity.status(401).body("Geen valide refreshToken gevonden.");
    }


    @GetMapping("/authenticate/check")
    public ResponseEntity<?> authCheck() {
            return ResponseEntity.ok().body("authcheck");

    }

}
