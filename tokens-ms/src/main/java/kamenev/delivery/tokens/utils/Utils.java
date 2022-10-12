package kamenev.delivery.tokens.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

public final class Utils {

    private Utils() {}

    public static Date convert(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Set<String> parseAuthorities(String authorities) {
        return Set.of(authorities.split(","));
    }

    public static String serializeAuthorities(Set<String> authorities) {
        return String.join(",", authorities);
    }

}
