package kamenev.delivery.identityservice.model;

import kamenev.delivery.identityservice.validator.PhoneNumberConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public record SignupRequest(
        @NotEmpty String name,
        @NotEmpty String surname,
        @NotEmpty @PhoneNumberConstraint String phoneNumber,
        @NotEmpty @Email String email,
        @NotEmpty String password
) {
}
