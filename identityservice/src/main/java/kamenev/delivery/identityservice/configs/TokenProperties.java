package kamenev.delivery.identityservice.configs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.security.auth.jwt")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenProperties {

    private String base64secret;
    private int accessTokenTtlHours;
    private int refreshTokenTtlHours;

}
