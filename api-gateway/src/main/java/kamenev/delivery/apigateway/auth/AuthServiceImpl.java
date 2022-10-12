package kamenev.delivery.apigateway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import kamenev.delivery.apigateway.client.ITokenMsRestClient;
import kamenev.delivery.common.security.UserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final ITokenMsRestClient tokenMsRestClient;
    private final ObjectMapper mapper;

    public UserDetails authenticate(String jwt) {
        try {
            var response = tokenMsRestClient.verifyAccessToken(jwt);
            return mapper.readValue(response.body(), UserDetails.class);
        } catch (Exception e) {
            log.error(e.toString());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }
}
