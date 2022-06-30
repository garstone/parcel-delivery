package kamenev.delivery.orderservice.errors;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String message,
        HttpStatus status
) { }
