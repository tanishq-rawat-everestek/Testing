package ai.agilery.coupon.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SecurityConfig}.
 */
class SecurityConfigTest {

    @Test
    void testSecurityConfigCreation() {
        SecurityConfig config = new SecurityConfig();
        Assertions.assertNotNull(config);
    }
}
