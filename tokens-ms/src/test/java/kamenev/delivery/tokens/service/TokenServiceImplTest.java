package kamenev.delivery.tokens.service;

import com.auth0.jwt.JWTVerifier;
import kamenev.delivery.common.security.UserDetails;
import kamenev.delivery.tokens.domain.Tokens;
import kamenev.delivery.tokens.model.CreateTokensRequest;
import kamenev.delivery.tokens.model.RefreshTokenResponse;
import kamenev.delivery.tokens.repository.TokensRepository;
import kamenev.delivery.tokens.security.Claims;
import kamenev.delivery.tokens.security.TokenType;
import kamenev.delivery.tokens.utils.Utils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@EnableAutoConfiguration(exclude= DataSourceAutoConfiguration.class)
@Import({TokenServiceImpl.class})
class TokenServiceImplTest {

    private static final String auth1 = "auth1";
    private static final String auth2 = "auth2";
    private static final UUID id = UUID.fromString("2b0d76ae-a68b-475e-9cbb-852a9cbd7c77");

    @Autowired
    TokenService service;
    @MockBean
    TokensRepository repository;
    @Captor
    ArgumentCaptor<Tokens> tokensArgumentCaptor;

    @Test
    void createTokens() {
        CreateTokensRequest req = new CreateTokensRequest(id, "email@email.com", Set.of(auth1, auth2));
        var tokenPair = service.createTokens(req);

        verify(repository).save(tokensArgumentCaptor.capture());
        assertEquals(id, tokensArgumentCaptor.getValue().getId());

        var verifier = getVerifier();
        {
            var decoded = verifier.verify(tokenPair.accessToken());
            var claims = decoded.getClaims();
            var claimsId = claims.get(Claims.ID).as(UUID.class);
            var email = decoded.getSubject();
            var authorities = Utils.parseAuthorities(claims.get(Claims.AUTHORITIES).asString());

            assertEquals(id, claimsId);
            assertEquals(req.email(), email);
            assertEquals(req.authorities(), authorities);
            assertEquals(TokenType.ACCESS.toString(), claims.get(Claims.TOKEN_TYPE).asString());
        }
        {
            var decoded = verifier.verify(tokenPair.refreshToken());
            var claims = decoded.getClaims();
            var claimsId = claims.get(Claims.ID).as(UUID.class);
            var email = decoded.getSubject();
            var authorities = Utils.parseAuthorities(claims.get(Claims.AUTHORITIES).asString());

            assertEquals(id, claimsId);
            assertEquals("email@email.com", email);
            assertEquals(req.authorities(), authorities);
            assertEquals(TokenType.REFRESH.toString(), claims.get(Claims.TOKEN_TYPE).asString());
        }
    }

    @Test
    void verifyRefreshToken() {
        CreateTokensRequest req = new CreateTokensRequest(id, "email@email.com", Set.of(auth1, auth2));
        var tokenPair = service.createTokens(req);

        when(repository.findById(id)).thenReturn(Optional.of(new Tokens(id, tokenPair.refreshToken())));

        RefreshTokenResponse response = service.verifyRefreshToken(tokenPair.refreshToken());
        verify(repository, times(2)).save(any());

        assertEquals(id, response.userDetails().getId());
        assertEquals(req.authorities(), response.userDetails().getAuthorities());
        assertEquals(req.email(), response.userDetails().getEmail());
        assertNotEquals(tokenPair.accessToken(), response.tokens().accessToken());
        assertNotEquals(tokenPair.refreshToken(), response.tokens().refreshToken());

        var verifier = getVerifier();
        {
            var decoded = verifier.verify(tokenPair.accessToken());
            var claims = decoded.getClaims();
            var claimsId = claims.get(Claims.ID).as(UUID.class);
            var email = decoded.getSubject();
            var authorities = Utils.parseAuthorities(claims.get(Claims.AUTHORITIES).asString());

            assertEquals(id, claimsId);
            assertEquals(req.email(), email);
            assertEquals(req.authorities(), authorities);
            assertEquals(TokenType.ACCESS.toString(), claims.get(Claims.TOKEN_TYPE).asString());
        }
        {
            var decoded = verifier.verify(tokenPair.refreshToken());
            var claims = decoded.getClaims();
            var claimsId = claims.get(Claims.ID).as(UUID.class);
            var email = decoded.getSubject();
            var authorities = Utils.parseAuthorities(claims.get(Claims.AUTHORITIES).asString());

            assertEquals(id, claimsId);
            assertEquals("email@email.com", email);
            assertEquals(req.authorities(), authorities);
            assertEquals(TokenType.REFRESH.toString(), claims.get(Claims.TOKEN_TYPE).asString());
        }
    }

    @Test
    void verifyAccessToken() {
        CreateTokensRequest req = new CreateTokensRequest(id, "email@email.com", Set.of(auth1, auth2));
        var tokenPair = service.createTokens(req);
        UserDetails userDetails = service.verifyAccessToken(tokenPair.accessToken());

        assertEquals(id, userDetails.getId());
        assertEquals("email@email.com", userDetails.getEmail());
        assertEquals(req.authorities(), userDetails.getAuthorities());
    }

    private JWTVerifier getVerifier() {
        return ((TokenServiceImpl)service).getJwtVerifier();
    }

}