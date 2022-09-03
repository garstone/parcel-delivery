package kamenev.delivery.orderservice.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChangeDestinationRequest(
        @NotNull UUID id,
        @NotEmpty String destination
) {
}
