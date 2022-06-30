package kamenev.delivery.identityservice.model;

import kamenev.delivery.identityservice.validator.PhoneNumberConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public record UserDetails(
        @NotEmpty String name,
        @NotEmpty String surname,
        @NotEmpty @PhoneNumberConstraint String phoneNumber,
        @NotEmpty @Email String email
) implements Serializable {
}
