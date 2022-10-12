package kamenev.delivery.tokens.dto;

public record TokenPair(
        String accessToken,
        String refreshToken
){
}
