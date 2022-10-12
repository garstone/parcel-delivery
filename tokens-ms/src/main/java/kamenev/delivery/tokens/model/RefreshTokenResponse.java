package kamenev.delivery.tokens.model;

import kamenev.delivery.common.security.UserDetails;
import kamenev.delivery.tokens.dto.TokenPair;

public record RefreshTokenResponse(
        UserDetails userDetails,
        TokenPair tokens
) {

}
