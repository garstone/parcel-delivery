package kamenev.delivery.apigateway.client;

import java.net.http.HttpResponse;

public interface ITokenMsRestClient {

    HttpResponse<String> verifyAccessToken(String token) throws Exception;
}
