package ai.agilery.coupon;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * This test case just checks if application starts successfully or now.
 */
@SpringBootTest
@ActiveProfiles("test")
class CouponServiceApplicationTests {

    @Test
    void contextLoads() {
        // Just load all the beans and check if there is any spring dependency injection
        // issue.
    }

}
