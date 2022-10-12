package kamenev.delivery.tokens.controller;

import kamenev.delivery.common.security.UserDetails;
import kamenev.delivery.tokens.dto.TokenPair;
import kamenev.delivery.tokens.model.CreateTokensRequest;
import kamenev.delivery.tokens.model.RefreshTokenResponse;
import kamenev.delivery.tokens.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class TokensController {

    private final TokenService tokenService;

    @PostMapping(value = "/token/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenPair> createTokens(@RequestBody CreateTokensRequest request) {
        TokenPair tokenPair = tokenService.createTokens(request);
        return ResponseEntity.ok(tokenPair);
    }

    @GetMapping(value = "/token/refresh/{token}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefreshTokenResponse> verifyRefreshToken(@PathVariable String token) {
        if (token.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        RefreshTokenResponse refreshTokenResponse = tokenService.verifyRefreshToken(token);
        return ResponseEntity.ok(refreshTokenResponse);
    }

    @GetMapping(value = "/token/access/{token}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetails> verifyAccessToken(@PathVariable String token) {
        if (token.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        UserDetails userDetails = tokenService.verifyAccessToken(token);
        return ResponseEntity.ok(userDetails);
    }

}
