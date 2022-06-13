package kamenev.delivery.identityservice.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import kamenev.delivery.identityservice.exceptions.dto.AuthExceptionResponse;
import kamenev.delivery.identityservice.exceptions.exception.AuthException;
import kamenev.delivery.identityservice.exceptions.exception.UserAlreadyExistsException;
import kamenev.delivery.identityservice.exceptions.exception.UserNotFoundException;
import kamenev.delivery.identityservice.exceptions.exception.UserRoleDoesNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class AuthExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public AuthExceptionResponse handleException(RuntimeException e) {
        log.error("RuntimeException: ", e);
        return new AuthExceptionResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = JWTVerificationException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public AuthExceptionResponse handleException(JWTVerificationException e) {
        log.info("JWT verification failed: ", e);
        return new AuthExceptionResponse("Internal server error", HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public AuthExceptionResponse handleException(BadCredentialsException ex) {
        log.info("Username or password is wrong: ", ex);
        return new AuthExceptionResponse("Username or password is wrong", HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public AuthExceptionResponse handleException(UserAlreadyExistsException ex) {
        log.info("User already exists: ", ex);
        return new AuthExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public AuthExceptionResponse handleException(UserNotFoundException ex) {
        log.info("User not found: ", ex);
        return new AuthExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserRoleDoesNotExistException.class)
    public AuthExceptionResponse handleException(UserRoleDoesNotExistException ex) {
        log.info("User role does not exist: ", ex);
        return new AuthExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public AuthExceptionResponse handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.info("DataIntegrityViolationException: ", ex);
        return new AuthExceptionResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AuthException.class)
    public AuthExceptionResponse handleException(AuthException ex) {
        log.info("Authentication exception: ", ex);
        return new AuthExceptionResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
