package nl.hva.miw.thepiratebank.utilities.authorization.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Intercepts pathvariable userID -> checks if userid in pathvariable is same as in jwt token.
 * Prevents users from accessing other URI
 * *
 */
@Component
public class UserIdInterceptor implements HandlerInterceptor{

    private final AccessTokenService accessTokenService;
    private final static String ATTRIBUTE_VARIABLE_PATH_USERID = "userid";

    public UserIdInterceptor(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables != null && pathVariables.containsKey(ATTRIBUTE_VARIABLE_PATH_USERID)) {
            String pathVariableValue = pathVariables.get(ATTRIBUTE_VARIABLE_PATH_USERID);

            // is door filter heen gekomen dus heeft al een authorisatie header
            String[] authorizationHeader = request.getHeader("AUTHORIZATION").split(" ");
            DecodedJWT userJWT = accessTokenService.getTokenPayload(authorizationHeader[1]);
            int userIDJWt = userJWT.getClaim("id").asInt();

            if (userIDJWt == Integer.parseInt(pathVariableValue)) {
                return true;
            }
            // als er in de url een pathvariable is met id
            //verkeerde login
            return redirectToLogin(response);
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }


    public boolean redirectToLogin(HttpServletResponse  response) throws IOException {
        response.sendRedirect("/login/");
        return false;
    }

}
