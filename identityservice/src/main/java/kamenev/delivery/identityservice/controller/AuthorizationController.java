package kamenev.delivery.identityservice.controller;

import kamenev.delivery.identityservice.model.SigninViaEmailRequest;
import kamenev.delivery.identityservice.model.SignupRequest;
import kamenev.delivery.identityservice.model.TokenPair;
import kamenev.delivery.identityservice.service.IAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authorization")
@Validated
public class AuthorizationController {

    private final IAuthorizationService authorizationService;

    @PutMapping("/user/signup")
    public ResponseEntity<Void> createUser(@Valid @RequestBody SignupRequest request) {
        authorizationService.createUser(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/signin")
    public ResponseEntity<TokenPair> signinUser(@Valid @RequestBody SigninViaEmailRequest request) {
        TokenPair tokenPair = authorizationService.signin(request);
        return ResponseEntity.status(HttpStatus.OK).body(tokenPair);
    }

    @PostMapping("/admin/signin")
    public ResponseEntity<TokenPair> signinAdmin(@Valid @RequestBody SigninViaEmailRequest request) {
        TokenPair tokenPair = authorizationService.signin(request);
        return ResponseEntity.status(HttpStatus.OK).body(tokenPair);
    }

    @PutMapping("/courier/signup")
    public ResponseEntity<Void> createCourier(@Valid @RequestBody SignupRequest request) {
        authorizationService.createCourier(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/courier/signin")
    public ResponseEntity<TokenPair> signinCourier(@Valid @RequestBody SigninViaEmailRequest request) {
        TokenPair tokenPair = authorizationService.signin(request);
        return ResponseEntity.status(HttpStatus.OK).body(tokenPair);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenPair> tokenRefresh(@NotEmpty @RequestBody String refreshToken) {
        TokenPair tokenPair = authorizationService.tokenRefresh(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(tokenPair);
    }

}
