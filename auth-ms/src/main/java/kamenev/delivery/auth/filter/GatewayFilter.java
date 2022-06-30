package kamenev.delivery.auth.filter;

import kamenev.delivery.auth.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GatewayFilter implements GlobalFilter, Ordered {

    private final TokenService tokenService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var tokenOpt = TokenService.parseJwtToken(request);
        if (!tokenOpt.isEmpty()) {
            var token = tokenOpt.get();
            var userDetails  = tokenService.verify(token);

            exchange.getRequest().mutate()
                    .header("id", userDetails.getId().toString())
                    .header("email", userDetails.getEmail())
                    .header("permissions", userDetails.stringifyAuthorities())
                            .build();

//            Auth auth = new Auth(
//                    userDetails,
//                    token,
//                    userDetails.getAuthorities()
//            );
//            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
