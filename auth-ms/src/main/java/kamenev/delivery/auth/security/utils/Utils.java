package kamenev.delivery.auth.security.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class Utils {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    public static Date convert(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

//    public static Optional<String> parseJwtToken(HttpServletRequest request) {
//        String headerAuth = request.getHeader(AUTHORIZATION);
//        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
//            return Optional.of(headerAuth.substring(7));
//        }
//        return Optional.empty();
//    }

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
