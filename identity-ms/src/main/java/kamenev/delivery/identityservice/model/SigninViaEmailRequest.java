package kamenev.delivery.identityservice.model;

import javax.validation.constraints.NotEmpty;

public record SigninViaEmailRequest(
        @NotEmpty String email,
        @NotEmpty String password
) {
}
