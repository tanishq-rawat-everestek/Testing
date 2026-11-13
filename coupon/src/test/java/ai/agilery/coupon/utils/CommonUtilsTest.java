package ai.agilery.coupon.utils;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import ai.agilery.coupon.util.CommonUtils;

/**
 * Unit tests for {@link CommonUtils}.
 */
class CommonUtilsTest {

    private CommonUtils commonUtils;
    private Jwt jwt;
    private Authentication authentication;
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        commonUtils = new CommonUtils();
        jwt = Mockito.mock(Jwt.class);
        authentication = Mockito.mock(Authentication.class);
        securityContext = Mockito.mock(SecurityContext.class);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(jwt);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetUserIdFromUserIdClaim() {
        UUID expectedUuid = UUID.randomUUID();
        Mockito.when(jwt.getClaimAsString("userId")).thenReturn(expectedUuid.toString());

        UUID actualUuid = commonUtils.getUserId();

        Assertions.assertEquals(expectedUuid, actualUuid);
    }

    @Test
    void testGetUserIdFromSubjectWhenClaimMissing() {
        UUID expectedUuid = UUID.randomUUID();
        Mockito.when(jwt.getClaimAsString("userId")).thenReturn(null);
        Mockito.when(jwt.getSubject()).thenReturn(expectedUuid.toString());

        UUID actualUuid = commonUtils.getUserId();

        Assertions.assertEquals(expectedUuid, actualUuid);
    }

    @Test
    void testGetUserIdReturnsNullWhenInvalidOrMissing() {
        Mockito.when(jwt.getClaimAsString("userId")).thenReturn("invalid-uuid");
        Assertions.assertNull(commonUtils.getUserId());

        Mockito.when(jwt.getClaimAsString("userId")).thenReturn(null);
        Mockito.when(jwt.getSubject()).thenReturn(null);
        Assertions.assertNull(commonUtils.getUserId());
    }

    @Test
    void testGetAccountId() {
        Mockito.when(jwt.getClaimAsString("accountId")).thenReturn("12345");

        Long accountId = commonUtils.getAccountId();

        Assertions.assertEquals(12345L, accountId);
    }

    @Test
    void testGetAccountIdNullOrInvalid() {
        Mockito.when(jwt.getClaimAsString("accountId")).thenReturn(null);
        Assertions.assertNull(commonUtils.getAccountId());

        Mockito.when(jwt.getClaimAsString("accountId")).thenReturn("invalid");
        Assertions.assertNull(commonUtils.getAccountId());
    }

    @Test
    void testGetRoleName() {
        Mockito.when(jwt.getClaimAsString("role")).thenReturn("admin");

        String role = commonUtils.getRoleName();

        Assertions.assertEquals("admin", role);
    }

    @Test
    void testGetAuthorizationScope() {
        List<String> scopes = List.of("read", "write");
        Mockito.when(jwt.getClaim("scope")).thenReturn(scopes);

        List<String> actualScopes = commonUtils.getAuthorizationScope();

        Assertions.assertEquals(scopes, actualScopes);
    }
}
