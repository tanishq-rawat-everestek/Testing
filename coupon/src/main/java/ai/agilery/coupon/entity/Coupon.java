package ai.agilery.coupon.entity;

import ai.agilery.coupon.enumeration.CouponStatus;
import ai.agilery.coupon.enumeration.CouponType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a promotional coupon with discount rules and usage restrictions.
 * Supports both percentage-based and fixed-amount discounts with configurable limits.
 */
@Entity
@Table(name = "coupons")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    /**
     * Unique identifier for the coupon
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Unique coupon code that customers use to redeem the offer
     */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    /**
     * Human-readable description of the coupon offer
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * Type of discount: PERCENTAGE or FIXED_AMOUNT
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private CouponType type;

    /**
     * Discount value: percentage (0-100) or fixed amount based on type
     */
    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    /**
     * Minimum order value required to apply this coupon
     */
    @Column(name = "min_order_value", precision = 10, scale = 2)
    private BigDecimal minOrderValue;

    /**
     * Maximum discount amount cap (applicable for percentage discounts)
     */
    @Column(name = "max_discount_amount", precision = 10, scale = 2)
    private BigDecimal maxDiscountAmount;

    /**
     * Date when the coupon expires and can no longer be used
     */
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    /**
     * Total number of times this coupon can be redeemed across all customers
     */
    @Column(name = "total_redemption_limit")
    private Integer totalRedemptionLimit;

    /**
     * Maximum number of times a single customer can use this coupon
     */
    @Column(name = "per_customer_limit")
    private Integer perCustomerLimit;

    /**
     * Current count of successful redemptions
     */
    @Column(name = "redemption_count", nullable = false)
    @Builder.Default
    private Integer redemptionCount = 0;

    /**
     * Current status of the coupon: ACTIVE, INACTIVE, or EXPIRED
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CouponStatus status;

    /**
     * Version field for optimistic locking to prevent concurrent modification issues
     */
    @Version
    @Column(name = "version")
    private Long version;

    /**
     * Timestamp when the coupon was created
     */
    @CreationTimestamp
    @Column(name = "created_date_time", updatable = false)
    private LocalDateTime createdDateTime;

    /**
     * Timestamp when the coupon was last updated
     */
    @UpdateTimestamp
    @Column(name = "updated_date_time")
    private LocalDateTime updatedDateTime;
}
