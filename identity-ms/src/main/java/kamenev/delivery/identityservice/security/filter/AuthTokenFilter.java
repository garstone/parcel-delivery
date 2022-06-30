package kamenev.delivery.identityservice.security.filter;

import kamenev.delivery.identityservice.exceptions.exception.AuthException;
import kamenev.delivery.identityservice.security.UserDetailsImpl;
import kamenev.delivery.identityservice.security.service.TokenService;
import kamenev.delivery.identityservice.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final TokenService tokenService;

    String AUTHORIZATION = "Authorization";
    String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String headerAuth = request.getHeader(AUTHORIZATION);
        String token = null;
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
            token = headerAuth.substring(7);
        }
        if (token != null) {
            if (!tokenService.tokenIsValid(token)) {
                throw new AuthException(HttpStatus.UNAUTHORIZED, "Token is expired");
            }
            UserDetailsImpl userDetails = tokenService.verify(token);
            request.
        }

        filterChain.doFilter(request, response);
    }
}
