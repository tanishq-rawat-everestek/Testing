package ai.agilery.coupon.utils;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import ai.agilery.coupon.util.CommonUtils;
import ai.agilery.coupon.util.RbacExpression;

/**
 * Unit tests for {@link RbacExpression}.
 */
class RbacExpressionTest {

    private CommonUtils commonUtils;
    private Environment environment;
    private RbacExpression rbacExpression;

    @BeforeEach
    void setUp() {
        commonUtils = Mockito.mock(CommonUtils.class);
        environment = Mockito.mock(Environment.class);
        rbacExpression = new RbacExpression(commonUtils, environment);
    }

    @Test
    void testTestProfileBypassesPermissionCheck() {
        Mockito.when(environment.acceptsProfiles(Mockito.any(Profiles.class))).thenReturn(true);

        Assertions.assertTrue(rbacExpression.hasPermission("example.create"));
        Assertions.assertTrue(rbacExpression.hasPermission("example.delete"));
    }

    @Test
    void testInternalScopeGrantsAllPermissions() {
        Mockito.when(environment.acceptsProfiles(Mockito.any(Profiles.class))).thenReturn(false);
        Mockito.when(commonUtils.getAuthorizationScope()).thenReturn(List.of("internal"));

        Assertions.assertTrue(rbacExpression.hasPermission("example.create"));
        Assertions.assertTrue(rbacExpression.hasPermission("any.resource.action"));
    }

    @Test
    void testPermissionAllowedByDefault() {
        Mockito.when(environment.acceptsProfiles(Mockito.any(Profiles.class))).thenReturn(false);
        Mockito.when(commonUtils.getAuthorizationScope()).thenReturn(Collections.emptyList());

        Assertions.assertTrue(rbacExpression.hasPermission("example.create"));
        Assertions.assertTrue(rbacExpression.hasPermission("example.view"));
    }

    @Test
    void testWildcardPermissionMatching() {
        Assertions.assertTrue(rbacExpression.hasPermission("example.*", "example.create"));
        Assertions.assertTrue(rbacExpression.hasPermission("example.*", "example.view"));
        Assertions.assertFalse(rbacExpression.hasPermission("example.*", "other.create"));

        Assertions.assertTrue(rbacExpression.hasPermission("*.create", "example.create"));
        Assertions.assertFalse(rbacExpression.hasPermission("*.create", "example.view"));

        Assertions.assertTrue(rbacExpression.hasPermission("*.*", "example.delete"));
    }

    @Test
    void testPermissionToRegex() {
        Pattern pattern = rbacExpression.permissionToRegex("example.*");
        Assertions.assertTrue(pattern.matcher("example.read").matches());
        Assertions.assertFalse(pattern.matcher("item.read").matches());
    }
}
