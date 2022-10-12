package kamenev.delivery.tokens.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kamenev.delivery.common.security.UserDetails;
import kamenev.delivery.tokens.domain.Tokens;
import kamenev.delivery.tokens.dto.TokenPair;
import kamenev.delivery.tokens.mapper.Mapper;
import kamenev.delivery.tokens.model.CreateTokensRequest;
import kamenev.delivery.tokens.model.RefreshTokenResponse;
import kamenev.delivery.tokens.repository.TokensRepository;
import kamenev.delivery.tokens.security.Claims;
import kamenev.delivery.tokens.security.TokenType;
import kamenev.delivery.tokens.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    public static final String REFRESH_TOKEN_URL = "/auth/token/refresh/{token}";
    @Value("${app.security.auth.jwt.base64-secret}")
    private String base64secret;
    @Value("${app.security.auth.jwt.access-token-ttl-minutes}")
    private int accessTokenTtlMinutes;
    @Value("${app.security.auth.jwt.refresh-token-ttl-hours}")
    private int refreshTokenTtlMinutes;

    private final TokensRepository repository;
    private static final Mapper mapper = Mapper.INSTANCE;

    private String generateNewTokenPair(UserDetails userDetails, int ttlMinutes, TokenType tokenType) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(ttlMinutes);

        String authorities = Utils.serializeAuthorities(userDetails.getAuthorities());

        return JWT.create()
                .withSubject(userDetails.getEmail())
                .withClaim(Claims.ID, userDetails.getId().toString())
                .withClaim(Claims.TOKEN_TYPE, tokenType.toString())
                .withClaim(Claims.AUTHORITIES, authorities)
                .withIssuedAt(Utils.convert(now))
                .withExpiresAt(Utils.convert(expiresAt))
                .sign(algorithm());
    }

    private TokenPair generateNewTokenPair(UserDetails userDetails) {
        return new TokenPair(
                generateNewTokenPair(userDetails, accessTokenTtlMinutes, TokenType.ACCESS),
                generateNewTokenPair(userDetails, refreshTokenTtlMinutes, TokenType.REFRESH)
        );
    }

    private Algorithm algorithm() {
        return Algorithm.HMAC256(base64secret);
    }

    @Override
    public TokenPair createTokens(CreateTokensRequest request) {
        UserDetails userDetails = mapper.fromCreateTokensRequest(request);
        var tokenPair = generateNewTokenPair(userDetails);
        var entity = new Tokens(request.id(), tokenPair.refreshToken());
        repository.save(entity);
        return tokenPair;
    }

    @Override
    @Transactional
    public RefreshTokenResponse verifyRefreshToken(String token) {
        var decoded = decodeJWT(token, TokenType.REFRESH);

        if (decoded.getExpiresAt().before(Date.from(Instant.now()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, REFRESH_TOKEN_URL);
        }

        var id = decoded.getClaim(Claims.ID).as(UUID.class);
        var opt = repository.findById(id);
        if (opt.isEmpty() || !token.equals(opt.get().getRefreshToken())) {
            log.warn("Somebody tries to update refresh token");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No such token in database");
        }

        var userDetails = getUserDetails(decoded);
        var tokenPair = generateNewTokenPair(userDetails);
        repository.save(new Tokens(id, tokenPair.refreshToken()));
        return new RefreshTokenResponse(userDetails, tokenPair);
    }

    private static UserDetails getUserDetails(DecodedJWT decoded) {
        var authorities = Arrays.stream(decoded.getClaim(Claims.AUTHORITIES).asString().split(","))
                .collect(Collectors.toSet());
        String email = decoded.getSubject();
        UUID userId = decoded.getClaim(Claims.ID).as(UUID.class);

        return new UserDetails(userId, email, null, authorities);
    }

    private DecodedJWT decodeJWT(String token, TokenType type) {
        var verifier = getJwtVerifier();
        var decoded = verifier.verify(token);

        if (!decoded.getClaim(Claims.TOKEN_TYPE).asString().equals(type.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong token");
        }

        return decoded;
    }

    @Override
    public UserDetails verifyAccessToken(String token) {
        DecodedJWT decoded = decodeJWT(token, TokenType.ACCESS);

        if (decoded.getExpiresAt().before(Date.from(Instant.now()))) {
            throw new ResponseStatusException(HttpStatus.CONTINUE, REFRESH_TOKEN_URL);
        }

        return getUserDetails(decoded);
    }

    JWTVerifier getJwtVerifier() {
        return JWT.require(algorithm()).build();
    }

}
