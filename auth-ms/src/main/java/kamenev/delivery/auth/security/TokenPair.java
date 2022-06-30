package kamenev.delivery.auth.security;


import java.io.Serializable;

public record TokenPair(
        String refreshToken,
        String accessToken
) implements Serializable {
}
