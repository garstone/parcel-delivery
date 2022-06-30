package kamenev.delivery.identityservice.service;

import kamenev.delivery.identityservice.model.SigninViaEmailRequest;
import kamenev.delivery.identityservice.model.SignupRequest;
import kamenev.delivery.identityservice.model.TokenPair;

public interface IAuthorizationService {
    void createUser(SignupRequest request);

    TokenPair signin(SigninViaEmailRequest request);

    void createCourier(SignupRequest request);

    TokenPair tokenRefresh(String refreshToken);
}
