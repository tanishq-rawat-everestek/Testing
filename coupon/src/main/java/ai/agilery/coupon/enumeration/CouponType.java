package ai.agilery.coupon.enumeration;

/**
 * Enumeration representing the type of discount offered by a coupon.
 * <ul>
 *   <li>PERCENTAGE: Discount is calculated as a percentage of the order amount</li>
 *   <li>FIXED_AMOUNT: Discount is a fixed monetary amount</li>
 * </ul>
 */
public enum CouponType {
    /**
     * Percentage-based discount (e.g., 10% off)
     */
    PERCENTAGE,

    /**
     * Fixed amount discount (e.g., $20 off)
     */
    FIXED_AMOUNT
}
