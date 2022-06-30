package kamenev.delivery.auth.exceptions.exception;

import org.springframework.http.HttpStatus;

public class UserRoleDoesNotExistException extends AuthException {

    public UserRoleDoesNotExistException() {
        super(HttpStatus.NOT_FOUND, "User with such role does not exist");
    }
}
