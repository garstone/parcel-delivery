package kamenev.delivery.auth.exceptions.dto;

import org.springframework.http.HttpStatus;

public record AuthExceptionResponse(
        String message,
        HttpStatus status
) {
}
