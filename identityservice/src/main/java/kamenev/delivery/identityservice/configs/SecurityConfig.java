package kamenev.delivery.identityservice.configs;

import kamenev.delivery.identityservice.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // вместо @Override protected void configure(HttpSecurity http) throws Exception {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authz) -> authz
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(withDefaults());

        http
                .headers()
                .contentSecurityPolicy("script-src 'self'")

                .and()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .csrf().disable()

                .authorizeRequests()
                .antMatchers("**/signup").not().fullyAuthenticated()
                .antMatchers("**/signin").not().fullyAuthenticated()
                .antMatchers("/api/authorization/token/refresh").permitAll();


        return http.build();

    }

    /*
     * JwtTokenFilter doesn't work for these endpoints
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                        .antMatchers(HttpMethod.OPTIONS, "/**")
//                        .antMatchers("/actuator/health")
//                        .antMatchers("/swagger-resources/**")
//                        .antMatchers("/swagger-ui/**")
//                        .antMatchers("/v2/api-docs/**")
//                        .antMatchers("/v3/api-docs/**")
//                        .antMatchers("/webjars/**")
//                        .antMatchers("/favicon.ico")
//                        .antMatchers("/error")
                        .antMatchers("**/signup")
                        .antMatchers("**/signin")
                        .antMatchers("/api/authorization/token/refresh");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
