package ai.agilery.coupon.enumeration;

/**
 * Enumeration representing the outcome of a coupon redemption attempt.
 * <ul>
 *   <li>SUCCESS: Coupon was successfully applied to the order</li>
 *   <li>FAILED: Coupon redemption failed due to validation errors</li>
 * </ul>
 */
public enum RedemptionStatus {
    /**
     * Redemption was successful
     */
    SUCCESS,

    /**
     * Redemption failed validation
     */
    FAILED
}
