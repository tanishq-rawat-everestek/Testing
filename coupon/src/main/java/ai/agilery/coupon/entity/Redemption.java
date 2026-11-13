package ai.agilery.coupon.entity;

import ai.agilery.coupon.enumeration.RedemptionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a coupon redemption transaction.
 * Records each attempt to apply a coupon to an order, including discount calculations.
 */
@Entity
@Table(name = "redemptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Redemption {

    /**
     * Unique identifier for the redemption record
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Reference to the coupon being redeemed
     */
    @Column(name = "coupon_id", nullable = false)
    private UUID couponId;

    /**
     * Reference to the customer redeeming the coupon
     */
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    /**
     * Reference to the order on which the coupon is applied
     */
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    /**
     * Original order amount before discount
     */
    @Column(name = "order_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderAmount;

    /**
     * Calculated discount amount applied from the coupon
     */
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    /**
     * Final payable amount after discount (orderAmount - discountAmount)
     */
    @Column(name = "final_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalAmount;

    /**
     * Status of the redemption: SUCCESS or FAILED
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RedemptionStatus status;

    /**
     * Error message if redemption failed
     */
    @Column(name = "failure_reason", length = 500)
    private String failureReason;

    /**
     * Timestamp when the redemption occurred
     */
    @CreationTimestamp
    @Column(name = "redemption_date_time", updatable = false)
    private LocalDateTime redemptionDateTime;
}
