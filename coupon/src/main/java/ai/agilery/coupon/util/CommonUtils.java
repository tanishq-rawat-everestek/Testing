package ai.agilery.coupon.util;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Utility class for common reusable methods.
 * <p>
 * Provides methods for extracting user details from the security context and other shared logic.
 * </p>
 */
@Component
public final class CommonUtils {

    private static final Logger log = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * Retrieves the current {@link Jwt} from the Spring Security context.
     *
     * <p>
     * This method extracts the authenticated principal from the
     * {@link SecurityContextHolder} and casts it to {@link Jwt}.
     * It assumes that the application is configured as an OAuth2 Resource Server
     * where the principal is a JWT.
     * </p>
     *
     * @return the current authenticated {@link Jwt}
     * @throws ClassCastException if the principal is not a Jwt
     * @throws NullPointerException if authentication is missing
     */
    private Jwt getJwt() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Retrieves the current authenticated user's ID from the JWT token.
     *
     * @return the user ID as {@link UUID} from JWT claims, or null if missing/invalid
     */
    public UUID getUserId() {
        try {
            String userId = getJwt().getClaimAsString("userId");
            if (userId == null) {
                userId = getJwt().getSubject();
            }
            return userId != null ? UUID.fromString(userId) : null;
        } catch (Exception e) {
            log.warn("Failed to retrieve user ID from JWT token", e);
            return null;
        }
    }

    /**
     * Retrieves the current authenticated user's account ID from the JWT token.
     *
     * @return the user account ID from JWT claims, or null if not found
     */
    public Long getAccountId() {
        try {
            String accountId = getJwt().getClaimAsString("accountId");
            return accountId != null ? Long.valueOf(accountId) : null;
        } catch (Exception e) {
            log.warn("Failed to retrieve account ID from JWT token", e);
            return null;
        }
    }

    /**
     * Retrieves the current authenticated user's role name from the JWT token.
     *
     * @return the role name string from JWT claims, or null if unauthenticated
     */
    public String getRoleName() {
        try {
            return getJwt().getClaimAsString("role");
        } catch (Exception e) {
            log.warn("Failed to retrieve role name from JWT token", e);
            return null;
        }
    }

    /**
     * Retrieves the authorization scopes assigned to the currently authenticated
     * user from the JWT claims.
     *
     * @return list of authorization scopes, or null if none exist
     */
    public List<String> getAuthorizationScope() {
        try {
            return getJwt().getClaim("scope");
        } catch (Exception e) {
            log.warn("Failed to retrieve authorization scopes from JWT token", e);
            return null;
        }
    }
}
