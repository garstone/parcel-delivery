package kamenev.delivery.identityservice.exceptions.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotFoundException extends AuthException {

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User with this email or phone number not found");
    }

}
