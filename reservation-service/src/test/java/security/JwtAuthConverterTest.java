package security;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtAuthConverterTest {

    @Test
    void utiliseLeNomUtilisateurDuJeton() {
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .claim("preferred_username", "amine")
                .build();

        assertEquals("amine", new JwtAuthConverter().convert(jwt).getName());
    }
}
