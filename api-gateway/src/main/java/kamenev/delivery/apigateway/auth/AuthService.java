package kamenev.delivery.apigateway.auth;

import kamenev.delivery.common.security.UserDetails;

public interface AuthService {

    UserDetails authenticate(String jwt);

}
