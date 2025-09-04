package co.com.jdti.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    /** Secret key used for signing JWT tokens. */
    private String secret;
    /** Expiration time in seconds. */
    private long expirationSeconds = 3600;
}
