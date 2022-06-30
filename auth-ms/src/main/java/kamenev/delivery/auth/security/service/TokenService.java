package kamenev.delivery.auth.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import kamenev.delivery.auth.config.TokenProperties;
import kamenev.delivery.auth.exceptions.exception.AuthException;
import kamenev.delivery.auth.security.enums.Claims;
import kamenev.delivery.auth.security.enums.TokenType;
import kamenev.delivery.common.security.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final TokenProperties tokenProperties;

    private Algorithm algorithm() {
        return Algorithm.HMAC512(tokenProperties.getBase64secret());
    }

    public boolean tokenIsValid(String token) {
        var verifier = JWT.require(algorithm()).build();
        var decoded = verifier.verify(token);
        if (!TokenType.REFRESH.toString().equals(decoded.getClaim(Claims.TOKEN_TYPE).asString())) {
            return false;
        }
        return decoded.getExpiresAt().before(new Date());
    }

    public UserDetails verify(String token) {
        var verifier = JWT.require(algorithm()).build();
        var decoded = verifier.verify(token);

        if (!TokenType.REFRESH.toString().equals(decoded.getClaim(Claims.TOKEN_TYPE).asString())) {
            throw new AuthException(HttpStatus.NOT_ACCEPTABLE, "Wrong token type");
        }
        if (decoded.getExpiresAt().after(new Date())) {
            throw new AuthException(HttpStatus.SEE_OTHER, "Token is expired");
        }

        Set<? extends GrantedAuthority> authorities =
                Arrays.stream(decoded.getClaim(Claims.AUTHORITIES).asString().split(","))
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        String email = decoded.getSubject();
        UUID id = UUID.fromString(decoded.getClaim(Claims.ID).asString());

        return UserDetails.builder()
                .id(id)
                .email(email)
                .authorities(authorities)
                .build();
    }

    public static Optional<String> parseJwtToken(ServerHttpRequest request) {
        var authHeaders = request.getHeaders().get(AUTHORIZATION);
        if (!Objects.requireNonNull(authHeaders).isEmpty()) {
            var headerAuth = authHeaders.get(0);
            if (headerAuth.startsWith(BEARER)) {
                return Optional.of(headerAuth.substring(7));
            }
        }
        return Optional.empty();
    }

}
