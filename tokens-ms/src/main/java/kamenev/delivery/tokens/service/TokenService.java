package kamenev.delivery.tokens.service;

import kamenev.delivery.common.security.UserDetails;
import kamenev.delivery.tokens.dto.TokenPair;
import kamenev.delivery.tokens.model.CreateTokensRequest;
import kamenev.delivery.tokens.model.RefreshTokenResponse;

public interface TokenService {

    TokenPair createTokens(CreateTokensRequest request);

    RefreshTokenResponse verifyRefreshToken(String token);

    UserDetails verifyAccessToken(String token);
}
