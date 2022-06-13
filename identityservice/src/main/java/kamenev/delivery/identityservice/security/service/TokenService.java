package kamenev.delivery.identityservice.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import kamenev.delivery.identityservice.configs.TokenProperties;
import kamenev.delivery.identityservice.model.TokenPair;
import kamenev.delivery.identityservice.security.UserDetailsImpl;
import kamenev.delivery.identityservice.security.enums.Claims;
import kamenev.delivery.identityservice.security.enums.TokenType;
import kamenev.delivery.identityservice.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProperties tokenProperties;
    private final IJwtStoreService jwtStore;

    private String createToken(UserDetailsImpl userDetails, int ttlHours, TokenType tokenType) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusHours(ttlHours);
        String authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return JWT.create()
                .withSubject(userDetails.getEmail())
                .withClaim(Claims.ID, userDetails.getId().toString())
                .withClaim(Claims.TOKEN_TYPE, tokenType.toString())
                .withClaim(Claims.AUTHORITIES, authorities)
                .withIssuedAt(Utils.convert(now))
                .withExpiresAt(Utils.convert(expiresAt))
                .sign(algorithm());
    }

    private Algorithm algorithm() {
        return Algorithm.HMAC512(tokenProperties.getBase64secret());
    }

    public TokenPair createToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String refreshToken = createToken(userDetails, tokenProperties.getRefreshTokenTtlHours(), TokenType.REFRESH);
        String accessToken = createToken(userDetails, tokenProperties.getAccessTokenTtlHours(), TokenType.ACCESS);

        var tokenPair = new TokenPair(
                refreshToken,
                accessToken
        );

        jwtStore.add(userDetails.getId(), tokenPair);
        return tokenPair;
    }

    public boolean tokenIsValid(String token) {
        var verifier = JWT.require(algorithm()).build();
        var decoded = verifier.verify(token);
        if (!TokenType.REFRESH.toString().equals(decoded.getClaim(Claims.TOKEN_TYPE).asString())) {
            return false;
        }
        return decoded.getExpiresAt().before(new Date());
    }

    public UserDetailsImpl verify(String token) {
        var verifier = JWT.require(algorithm()).build();
        var decoded = verifier.verify(token);

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(decoded.getClaim(Claims.AUTHORITIES)
                .asString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        String email = decoded.getSubject();
        UUID id = UUID.fromString(decoded.getClaim(Claims.ID).asString());

        return new UserDetailsImpl(id, email, null, null, authorities);
    }

    public TokenPair refreshToken(String refreshToken) {

    }
}
