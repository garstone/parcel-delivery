package kamenev.delivery.identityservice.service;

import kamenev.delivery.identityservice.domain.Role;
import kamenev.delivery.identityservice.domain.User;
import kamenev.delivery.identityservice.exceptions.exception.UserAlreadyExistsException;
import kamenev.delivery.identityservice.mapper.IdentityMapper;
import kamenev.delivery.identityservice.model.SigninViaEmailRequest;
import kamenev.delivery.identityservice.model.SignupRequest;
import kamenev.delivery.identityservice.model.TokenPair;
import kamenev.delivery.identityservice.repository.PermissionRepository;
import kamenev.delivery.identityservice.repository.RoleRepository;
import kamenev.delivery.identityservice.repository.UserRepository;
import kamenev.delivery.identityservice.security.enums.Roles;
import kamenev.delivery.identityservice.security.service.IJwtStoreService;
import kamenev.delivery.identityservice.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorizationService implements IAuthorizationService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final IdentityMapper mapper;
    private final IJwtStoreService jwtStore;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    public void createUser(SignupRequest request) {
        createUser(request, Roles.ROLE_USER);
    }

    private void createUser(SignupRequest request, Roles role) {
        throwIfUserExists(request.email(), request.phoneNumber());

        String encoded = encoder.encode(request.password());
        User user = mapper.fromSignupRequest(request, encoded);
        Role userRole = roleRepository.getByName(role.toString());
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
    }

    @Override
    public TokenPair signin(SigninViaEmailRequest request) {
        throwIfUserExists(request.email(), null);
        var authToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var authentication = authenticationManager.authenticate(authToken);
        return tokenService.createToken(authentication);
    }

    @Override
    public void createCourier(SignupRequest request) {
        throwIfUserExists(request.email(), request.phoneNumber());
        createUser(request, Roles.ROLE_COURIER);
    }

    @Override
    public TokenPair tokenRefresh(String refreshToken) {
        return tokenService.refreshToken(refreshToken);
    }

    private void throwIfUserExists(String email, String phoneNumber) {
        if (email != null && userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(String.format("User with email '%s' exists already", request.email()));
        }
        if (phoneNumber != null && userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new UserAlreadyExistsException(String.format("User with contact number '%s' exists already", request.phoneNumber()));
        }
    }

}
