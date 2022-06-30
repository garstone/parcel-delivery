package kamenev.delivery.orderservice.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record OrderCreateRequest(
        @NotNull UUID userId,
        @NotEmpty String phone,
        @NotEmpty String name,
        @NotEmpty String destination,
        String comments
) {
}
