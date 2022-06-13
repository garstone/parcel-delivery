package kamenev.delivery.identityservice.exceptions.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserAlreadyExistsException extends AuthException {

    public UserAlreadyExistsException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }

}
