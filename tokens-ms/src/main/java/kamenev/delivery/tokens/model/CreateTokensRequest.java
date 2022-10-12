package kamenev.delivery.tokens.model;

import java.util.Set;
import java.util.UUID;

public record CreateTokensRequest(
        UUID id,
        String email,
        Set<String> authorities
) {
}
