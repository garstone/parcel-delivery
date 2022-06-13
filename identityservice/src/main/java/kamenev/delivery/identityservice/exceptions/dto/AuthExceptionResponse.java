package kamenev.delivery.identityservice.exceptions.dto;

import org.springframework.http.HttpStatus;

public record AuthExceptionResponse(
        String message,
        HttpStatus status
) {
}
