package kamenev.delivery.orderservice.model;

import kamenev.delivery.orderservice.domain.Status;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record ChangeStatusRequest(
        @NotNull UUID orderId,
        @NotNull Status status
        ) { }
