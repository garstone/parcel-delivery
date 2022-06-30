package kamenev.delivery.identityservice.model;


import java.io.Serializable;

public record TokenPair(
        String refreshToken,
        String accessToken
) implements Serializable {
}
