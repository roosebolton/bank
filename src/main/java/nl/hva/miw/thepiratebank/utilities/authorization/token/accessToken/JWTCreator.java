package nl.hva.miw.thepiratebank.utilities.authorization.token.accessToken;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.service.AdminService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JWTCreator {
    private final static String SIGNATURE_SECRET = "c34caa3ac31d43d5b2e98d30d4cf9b82";
    private final static String ISSUER_TOKEN="ThePirateBank";
    private final static long JWT_EXPIRY_TIME_MIN = 30;

    private final AdminService adminService;

    public JWTCreator(AdminService adminService) {
        this.adminService = adminService;
    }


    public  String createToken (User user) {
        LocalDateTime current_time = LocalDateTime.now();
        LocalDateTime time_expired = current_time.plusMinutes(JWT_EXPIRY_TIME_MIN);
        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(SIGNATURE_SECRET);
            token = JWT.create()
                    .withIssuer(ISSUER_TOKEN)
                    .withIssuedAt(Date.from(current_time.atZone((ZoneId.systemDefault())).toInstant()))
                    .withExpiresAt(Date.from(time_expired.atZone((ZoneId.systemDefault())).toInstant()))
                    .withClaim("id",user.getUserId())
                    .withClaim("admin", adminService.isAdmin())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            exception.printStackTrace();
        }
        return token;
    }

    public static String getSignatureSecret() {
        return SIGNATURE_SECRET;
    }
    public static String getIssuerToken() {
        return ISSUER_TOKEN;
    }



}
