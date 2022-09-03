package kamenev.delivery.orderservice.model;

import jakarta.validation.constraints.NotNull;
import kamenev.delivery.orderservice.domain.Status;

import java.util.UUID;

public record ChangeStatusRequest(
        @NotNull UUID orderId,
        @NotNull Status status
        ) { }
