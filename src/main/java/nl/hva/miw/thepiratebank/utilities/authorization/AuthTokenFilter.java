package nl.hva.miw.thepiratebank.utilities.authorization;

import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private final AccessTokenService validator;

    public AuthTokenFilter(AccessTokenService validator) {
        this.validator = validator;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            FilterChain filterChain)  throws ServletException, IOException {
        String header = httpServletRequest.getHeader("Authorization");

        if (header != null) {
            String[] content = httpServletRequest.getHeader("Authorization").split(" ");
            if (content[1]!= null &&content[0].equals("Bearer") && validator.isValid(content[1])) {
                // geef door in de filter chain, richting controller method
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                // Stop het doorgeven en geef een response naar de client

                httpServletResponse.setHeader("Location","/index.html");
              httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"The token is not valid.");
            }
        } else {
            httpServletResponse.setHeader("Location","/index.html");
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing.");

        }
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        var pathMatcher = new AntPathMatcher();
        var excludes = List.of(
                "js/**","css/**","images/**",
                "/",
                "market/**",
                "user/register",
                "users/authenticate",
                "users/authenticate/refresh",
                "login/**",
                "coins/**",
                "/trade",
                "index.html",
                "header.html",
                "register/register.html",
                "trading/tradepage.html",
                "wallet/wallet.html",
                "about/about.html",
                "orderbook/orderbook.html",
                "admin/admin.html");
        return excludes.stream().anyMatch(path -> pathMatcher.match("/" + path, request.getServletPath()));
    }
}
