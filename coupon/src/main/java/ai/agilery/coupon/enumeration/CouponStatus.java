package ai.agilery.coupon.enumeration;

/**
 * Enumeration representing the current status of a coupon.
 * <ul>
 *   <li>ACTIVE: Coupon is active and can be used</li>
 *   <li>INACTIVE: Coupon is temporarily disabled</li>
 *   <li>EXPIRED: Coupon has passed its expiry date</li>
 * </ul>
 */
public enum CouponStatus {
    /**
     * Coupon is active and available for redemption
     */
    ACTIVE,

    /**
     * Coupon is temporarily disabled by administrator
     */
    INACTIVE,

    /**
     * Coupon has expired and cannot be used
     */
    EXPIRED
}
