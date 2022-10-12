package kamenev.delivery.apigateway.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
public class TokenMsRestClient implements ITokenMsRestClient {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.of(2, ChronoUnit.SECONDS))
            .build();
    @Value("${app.tokens-ms.url}")
    private String authMsUrl;
    @Value("${app.tokens-ms.refresh-token-url}")
    private String refreshTokenUrl;
    @Value("${app.tokens-ms.access-token-url}")
    private String accessTokenUrl;

    @Override
    public HttpResponse<String> verifyAccessToken(String token) throws Exception {
        var request = HttpRequest.newBuilder(new URI(authMsUrl + accessTokenUrl))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(token))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }


}
