package kamenev.delivery.auth.exceptions;

import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Order(-2)
public class AuthExceptionHandler extends AbstractErrorWebExceptionHandler {
//
//    @ExceptionHandler(value = RuntimeException.class)
//    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
//    public AuthExceptionResponse handleException(RuntimeException e) {
//        log.error("RuntimeException: ", e);
//        return new AuthExceptionResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(value = JWTVerificationException.class)
//    @ResponseStatus(code = HttpStatus.FORBIDDEN)
//    public AuthExceptionResponse handleException(JWTVerificationException e) {
//        log.info("JWT verification failed: ", e);
//        return new AuthExceptionResponse("Internal server error", HttpStatus.FORBIDDEN);
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(UserRoleDoesNotExistException.class)
//    public AuthExceptionResponse handleException(UserRoleDoesNotExistException ex) {
//        log.info("User role does not exist: ", ex);
//        return new AuthExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(AuthException.class)
//    public AuthExceptionResponse handleException(AuthException ex) {
//        log.info("Authentication exception: ", ex);
//        return new AuthExceptionResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(UnauthorizedException.class)
//    public AuthExceptionResponse handleException(UnauthorizedException ex) {
//        log.info("Unauthorized exception: ", ex);
//        return new AuthExceptionResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
//    }
//
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(
            ErrorAttributes errorAttributes) {

        return RouterFunctions.route(
                RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

        Map<String, Object> errorPropertiesMap = getErrorAttributes(request,
                ErrorAttributeOptions.defaults());

        return ServerResponse.status((HttpStatusCode) errorPropertiesMap.get("status"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }
}
