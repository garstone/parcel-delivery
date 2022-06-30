package kamenev.delivery.auth.config;

//@Configuration
////@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final TokenService tokenService;
//    private final AuthTokenFilter authTokenFilter;
//
////    @Bean
////    public AuthenticationProvider authenticationProvider() {
////        return new DaoAuthenticationProvider();
////    }
//
////    @Bean
////    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
////        return authConfig.getAuthenticationManager();
////    }
////
////    @Bean
////    public DaoAuthenticationProvider authenticationProvider() {
////        var authProvider = new DaoAuthenticationProvider();
////        authProvider.setUserDetailsService(userDetailsService);
////        return authProvider;
////    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.headers().contentSecurityPolicy("script-src 'self'")
//
//                .and().frameOptions().sameOrigin()
//
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.csrf().disable()
//
//                .authorizeRequests()
//
//                .antMatchers("/identity/**").permitAll().antMatchers("/auth/**").permitAll()
//
//                /*
//                order-ms
//                 */.antMatchers("/order/api/orders/create").hasAuthority(Permissions.CREATE_ORDER.get()).antMatchers("/order/api/orders/change-destination").hasAuthority(Permissions.CHANGE_ORDER_DESTINATION.get()).antMatchers("/order/api/orders/cancel/**").hasAuthority(Permissions.CANCEL_ORDER.get()).antMatchers("/order/api/orders/details/**").hasAuthority(Permissions.VIEW_ORDER.get()).antMatchers("/order/api/orders/user/**").hasAuthority(Permissions.VIEW_ORDER.get()).antMatchers("/order/api/orders/courier/**").hasAuthority(Permissions.VIEW_ORDER.get()).antMatchers("/order/api/orders/change-status").hasAuthority(Permissions.CHANGE_ORDER_STATUS.get()).antMatchers("/order/api/orders/all").hasAuthority(Permissions.VIEW_ALL_ORDERS.get()).antMatchers("/order/api/orders/assign").hasAuthority(Permissions.ASSIGN_COURIER.get()).antMatchers("/order/api/orders/track/**").hasAuthority(Permissions.TRACK_PARCEL.get())
//
//                .anyRequest().denyAll();
//
////                .and()
////                .apply(securityConfigurerAdapter());
//
////        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
////    private JwtConfigurer securityConfigurerAdapter() {
////        return new JwtConfigurer(tokenService);
////    }
//
//    /*
//     * AuthTokenFilter doesn't work for these endpoints
//     */
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**").antMatchers("/auth/**").antMatchers("/identity/api/authorization/token/refresh");
//    }
//
//}
