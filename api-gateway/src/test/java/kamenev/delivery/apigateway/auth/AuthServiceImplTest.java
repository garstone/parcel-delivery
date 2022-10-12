package kamenev.delivery.apigateway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import kamenev.delivery.apigateway.client.TokenMsRestClient;
import kamenev.delivery.apigateway.client.ITokenMsRestClient;
import kamenev.delivery.common.security.UserDetails;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.http.HttpResponse;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    private final ITokenMsRestClient ITokenMsRestClient = Mockito.mock(TokenMsRestClient.class);
    private ObjectMapper mapper = new ObjectMapper();
    private final AuthService service = new AuthServiceImpl(ITokenMsRestClient, mapper);
    private final Set<String> authorities = Set.of("permission1, permission2");
    private final UserDetails userDetails1 = new UserDetails(
            UUID.randomUUID(),
            "abc@gmail.com",
            "+71231231212",
            authorities
    );

    @Test
    void authenticate_Ok() throws Exception {
        var jwt = "jwt";
        HttpResponse response = mock(HttpResponse.class);
        when(response.body()).thenReturn(mapper.writeValueAsString(userDetails1));
        when(response.statusCode()).thenReturn(200);
        when(ITokenMsRestClient.verifyAccessToken(jwt)).thenReturn(response);

        UserDetails actual = service.authenticate(jwt);

        assertEquals(userDetails1.getId(), actual.getId());
        assertEquals(userDetails1.getEmail(), actual.getEmail());
        assertEquals(userDetails1.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(userDetails1.getAuthorities(), actual.getAuthorities());
    }

}