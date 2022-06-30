package kamenev.delivery.auth.config;

import kamenev.delivery.common.security.Permissions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class GatewayConfig {

//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//
//                .build();
//    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http
                .headers()
                .contentSecurityPolicy("script-src 'self'")

                .and()
                .frameOptions()
                .mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN);

//                .and()
//                .s()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .csrf().disable()

                .authorizeExchange()
                .pathMatchers("/identity/**")
                .permitAll()

                .and()
                .authorizeExchange()
                .pathMatchers("/auth/**")
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
        ;

        return http.build();
    }

    @Bean
    public WebExceptionHandler exceptionHandler() {
        return (ServerWebExchange exchange, Throwable ex) -> {
//            if (ex instanceof JWTVerificationException) {
//                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//                exchange = exchange.mutate().response(getResponse("Internal server error", HttpStatus.FORBIDDEN)).build();
//                return Mono.just(exchange);
//            }
            return Mono.error(ex);
        };
    }

    private ServerHttpResponse getResponse(String text, HttpStatus status) {
        return (ServerHttpResponse) ServerResponse.status(status).body(text);
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .headers()
//                .contentSecurityPolicy("script-src 'self'")
//
//                .and()
//                .frameOptions()
//                .sameOrigin()
//
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http
//                .csrf().disable()
//
//                .authorizeRequests()
//
//                .antMatchers("/identity/**").permitAll()
//                .antMatchers("/auth/**").permitAll()
//
//                /*
//                order-ms
//                 */
//                .antMatchers("/order/api/orders/create").hasAuthority(Permissions.CREATE_ORDER.get())
//                .antMatchers("/order/api/orders/change-destination").hasAuthority(Permissions.CHANGE_ORDER_DESTINATION.get())
//                .antMatchers("/order/api/orders/cancel/**").hasAuthority(Permissions.CANCEL_ORDER.get())
//                .antMatchers("/order/api/orders/details/**").hasAuthority(Permissions.VIEW_ORDER.get())
//                .antMatchers("/order/api/orders/user/**").hasAuthority(Permissions.VIEW_ORDER.get())
//                .antMatchers("/order/api/orders/courier/**").hasAuthority(Permissions.VIEW_ORDER.get())
//                .antMatchers("/order/api/orders/change-status").hasAuthority(Permissions.CHANGE_ORDER_STATUS.get())
//                .antMatchers("/order/api/orders/all").hasAuthority(Permissions.VIEW_ALL_ORDERS.get())
//                .antMatchers("/order/api/orders/assign").hasAuthority(Permissions.ASSIGN_COURIER.get())
//                .antMatchers("/order/api/orders/track/**").hasAuthority(Permissions.TRACK_PARCEL.get())
//
//                .anyRequest().denyAll()
//        ;
//
////                .and()
////                .apply(securityConfigurerAdapter());
//
////        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
}
