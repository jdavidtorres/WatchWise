package co.com.jdti.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    @Test
    void generateTokenEncodesSubject() {
        JwtProperties props = new JwtProperties();
        props.setSecret("mysecretmysecretmysecretmysecret");
        props.setExpirationSeconds(60);
        JwtService service = new JwtService(props);

        String token = service.generateToken("user@example.com");
        String subject = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        assertThat(subject).isEqualTo("user@example.com");
    }
}
