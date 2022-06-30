package kamenev.delivery.auth.exceptions.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends RuntimeException {

    HttpStatus httpStatus;
    String message;

    public AuthException(HttpStatus status, String message) {
        this.httpStatus = status;
        this.message = message;
    }

}
