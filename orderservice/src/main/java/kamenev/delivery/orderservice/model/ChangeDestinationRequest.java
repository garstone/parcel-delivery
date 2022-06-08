package kamenev.delivery.orderservice.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record ChangeDestinationRequest(
        @NotNull UUID id,
        @NotEmpty String destination
) {
}
