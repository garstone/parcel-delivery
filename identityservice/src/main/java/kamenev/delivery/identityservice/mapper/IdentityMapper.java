package kamenev.delivery.identityservice.mapper;

import kamenev.delivery.identityservice.domain.User;
import kamenev.delivery.identityservice.model.SignupRequest;
import org.mapstruct.Mapper;

@Mapper
public interface IdentityMapper {

    default User fromSignupRequest(SignupRequest request, String encodedPass) {
        return User.builder()
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .password(encodedPass)
                .build();
    }
}
