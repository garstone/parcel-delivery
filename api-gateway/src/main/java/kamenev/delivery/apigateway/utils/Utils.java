package kamenev.delivery.apigateway.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Objects;
import java.util.Optional;

public class Utils {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private Utils() {}

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
