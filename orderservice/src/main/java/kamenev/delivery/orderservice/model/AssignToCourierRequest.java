package kamenev.delivery.orderservice.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record AssignToCourierRequest(
        @NotNull UUID orderId,
        @NotNull UUID courierId,
        @NotEmpty String name
        ) {
}
