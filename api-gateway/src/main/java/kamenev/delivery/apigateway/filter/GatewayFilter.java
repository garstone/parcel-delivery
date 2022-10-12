package kamenev.delivery.apigateway.filter;

import kamenev.delivery.apigateway.auth.AuthService;
import kamenev.delivery.apigateway.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GatewayFilter implements GlobalFilter, Ordered {

    private final AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var jwt = Utils.parseJwtToken(request);
        ServerHttpRequest.Builder mutate = exchange.getRequest().mutate();
        if (jwt.isPresent()) {
            var userDetails = authService.authenticate(jwt.get());
            mutate
                    .header("id", userDetails.getId().toString())
                    .header("email", userDetails.getEmail())
                    .header("permissions", String.join(",", userDetails.getAuthorities()))
                    ;
        }

        mutate
                .header("request_id", String.valueOf(UUID.randomUUID()));

        mutate.build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
