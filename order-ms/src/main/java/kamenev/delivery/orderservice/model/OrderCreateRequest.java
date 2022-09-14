package kamenev.delivery.orderservice.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderCreateRequest(
        @NotNull UUID userId,
        @NotEmpty String phone,
        @NotEmpty String name,
        @NotEmpty String destination,
        @NotEmpty String pickupLocation,
        String comments
) {
}
