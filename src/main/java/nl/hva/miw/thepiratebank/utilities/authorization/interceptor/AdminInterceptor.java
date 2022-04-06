
package nl.hva.miw.thepiratebank.utilities.authorization.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Intercepts pathvariable userID -> checks if userid in pathvariable is same as in jwt token.
 * Prevents users from accessing other URI
 * *
 */

@Component
public class AdminInterceptor implements HandlerInterceptor{

    private final AccessTokenService accessTokenService;
    private final static String ATTRIBUTE_VARIABLE_PATH_ADMIN = "admin";

    public AdminInterceptor(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");

        if(request.getRequestURI().contains(ATTRIBUTE_VARIABLE_PATH_ADMIN) && header != null ) {
            //is al door authorisatie filter heen, dus heeft header
            String[] authorizationHeader = header.split(" ");
            DecodedJWT userJWT = accessTokenService.getTokenPayload(authorizationHeader[1]);
           if (!userJWT.getClaim("admin").asBoolean()) {
               return redirectToHomepage(response);}
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }


    public boolean redirectToHomepage(HttpServletResponse  response) throws IOException {
        response.sendRedirect("/index.html");
        return false;
    }

}

