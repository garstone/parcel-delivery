package kamenev.delivery.orderservice.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignToCourierRequest(
        @NotNull UUID orderId,
        @NotNull UUID courierId,
        @NotEmpty String name
        ) {
}
