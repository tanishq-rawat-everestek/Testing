package ai.agilery.coupon.util;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * RBAC (Role-Based Access Control) expression component.
 * <p>
 * This class is responsible for evaluating whether the currently authenticated
 * user has permission to perform a specific action based on their role and
 * configured permissions.
 * </p>
 *
 * <p>
 * Permission format follows a dot-separated structure:
 *
 * <pre>
 * resource.action
 * </pre>
 *
 * Wildcards are supported:
 * <ul>
 * <li><b>example.*</b> - All actions on example</li>
 * <li><b>*.create</b> - Create action on all resources</li>
 * <li><b>*.*</b> - Full access (admin)</li>
 * </ul>
 * </p>
 */
@Component("rbac")
@RequiredArgsConstructor
public class RbacExpression {

    /**
     * A utility class for fetching user context and common helpers.
     */
    private final CommonUtils commonUtils;

    /**
     * Spring environment to check active profiles.
     */
    private final Environment environment;

    /**
     * Checks whether the currently authenticated user has the given permission.
     * <p>
     * If the active profile is {@code test}, permission checks are bypassed.
     * If the request contains the {@code internal} scope, all permissions are granted.
     * </p>
     *
     * @param permission the permission required to access a resource (e.g.
     *                   {@code example.create})
     * @return {@code true} if the user has the required permission, {@code false}
     *         otherwise
     */
    public boolean hasPermission(String permission) {

        if (environment != null && environment.acceptsProfiles(Profiles.of("test"))) {
            return true;
        }

        List<String> authenticationScopes = commonUtils.getAuthorizationScope();

        // If the request is an internal call (e.g. from another service) and has the
        // "internal" scope, we can assume it has all permissions since it's a trusted
        // internal call in a B2B context.
        if (authenticationScopes != null && authenticationScopes.contains("internal")) {
            return true;
        }

        // Read role from user payload / context
        // String roleName = commonUtils.getRoleName();

        // permissions = call your permission service to get the permissions for the role

        // Check if the required permission is in the user's permissions
        // if (permissions == null || !permissions.stream().anyMatch(p -> "*.*".equals(p) || hasPermission(p, permission))) {
        //     return false;
        // }

        return true;
    }

    /**
     * Evaluates whether a granted permission matches a required permission.
     * <p>
     * The granted permission may contain wildcards ({@code *}) which are converted
     * into a regular expression for matching.
     * </p>
     *
     * @param grantedPermission  the permission assigned to the role (e.g.
     *                           {@code example.*}, {@code *.*})
     * @param requiredPermission the permission being checked (e.g.
     *                           {@code example.create})
     * @return {@code true} if the granted permission matches the required
     *         permission, {@code false} otherwise
     */
    public boolean hasPermission(String grantedPermission, String requiredPermission) {
        Pattern pattern = this.permissionToRegex(grantedPermission);
        return pattern.matcher(requiredPermission).matches();
    }

    /**
     * Converts a permission pattern containing wildcards into a compiled regular
     * expression.
     *
     * <p>
     * This method transforms permission strings such as:
     * <ul>
     * <li>{@code example.view}</li>
     * <li>{@code example.*}</li>
     * <li>{@code *.create}</li>
     * </ul>
     * into equivalent regular expressions that can be used for runtime permission
     * matching.
     * </p>
     *
     * @param permission the permission pattern containing optional wildcards
     * @return compiled {@link Pattern} representing the equivalent regular
     *         expression
     * @throws IllegalArgumentException if permission is null
     */
    public Pattern permissionToRegex(String permission) {
        String regex = permission.replace(".", "\\.") // escape dot
                .replace("*", ".*"); // wildcard match

        return Pattern.compile("^" + regex + "$");
    }
}
