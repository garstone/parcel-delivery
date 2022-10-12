package kamenev.delivery.apigateway.config;

import kamenev.delivery.common.security.Permissions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;

@Configuration
@EnableWebFluxSecurity
public class GatewayConfig {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http
                .headers()
                .contentSecurityPolicy("script-src 'self'")

                .and()
                .frameOptions()
                .mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN);

        http
                .authorizeExchange()
                .pathMatchers("/identity/**")
                .permitAll()

                .and()
                .authorizeExchange()
                .pathMatchers("/auth/token/refresh/**")
                .permitAll()

                /*
                order-ms
                 */
                .and()
                .authorizeExchange()
                .pathMatchers("/order/api/orders/create").hasAuthority(Permissions.CREATE_ORDER.get())
                .and()
                .authorizeExchange()
                .pathMatchers("/order/api/orders/change-destination").hasAuthority(Permissions.CHANGE_ORDER_DESTINATION.get())
                .and()
                .authorizeExchange()
                .pathMatchers("/order/api/orders/cancel/**").hasAuthority(Permissions.CANCEL_ORDER.get())
                .and()
                .authorizeExchange()
                .pathMatchers("/order/api/orders/details/**").hasAuthority(Permissions.VIEW_ORDER.get())
                .and()
                .authorizeExchange()
                .pathMatchers("/order/api/orders/courier/**").hasAuthority(Permissions.VIEW_ORDER.get())
                .and()
                .authorizeExchange()
                .pathMatchers("/order/api/orders/change-status").hasAuthority(Permissions.CHANGE_ORDER_STATUS.get())
                .and()
                .authorizeExchange()
                .pathMatchers("/order/api/orders/all").hasAuthority(Permissions.VIEW_ALL_ORDERS.get())
                .and()
                .authorizeExchange()
                .pathMatchers("/order/api/orders/assign").hasAuthority(Permissions.ASSIGN_COURIER.get())
                .and()
                .authorizeExchange()
                .pathMatchers("/order/api/orders/track/**").hasAuthority(Permissions.TRACK_PARCEL.get())

                .and()
                .authorizeExchange().anyExchange().denyAll();

        return http.build();
    }

}
