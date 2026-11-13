package ai.agilery.coupon.dto;

import ai.agilery.coupon.enumeration.CouponStatus;
import ai.agilery.coupon.enumeration.CouponType;
import ai.agilery.coupon.enumeration.ValueOfEnum;
import ai.agilery.coupon.validation.NullOrNotEmpty;
import ai.agilery.coupon.validation.OnCreate;
import ai.agilery.coupon.validation.OnPatch;
import ai.agilery.coupon.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Coupon entity.
 * Used for creating, updating, and retrieving coupon information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Coupon data transfer object")
public class CouponDto {

    /**
     * Unique identifier for the coupon
     */
    @Null(groups = OnCreate.class, message = "ID must be null when creating a coupon")
    @NotNull(groups = OnUpdate.class, message = "ID is required when updating a coupon")
    @Null(groups = OnPatch.class, message = "ID must be null in the request body for patch operations")
    @Schema(description = "Unique identifier of the coupon", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    /**
     * Unique coupon code
     */
    @NotBlank(groups = OnCreate.class, message = "Coupon code is required")
    @NullOrNotEmpty(groups = {OnUpdate.class, OnPatch.class})
    @Size(max = 50, message = "Coupon code must not exceed 50 characters")
    @Schema(description = "Unique coupon code used for redemption", example = "SAVE20")
    private String code;

    /**
     * Description of the coupon offer
     */
    @NullOrNotEmpty(groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Schema(description = "Human-readable description of the coupon", example = "Get 20% off on orders above $100")
    private String description;

    /**
     * Type of discount: PERCENTAGE or FIXED_AMOUNT
     */
    @NotNull(groups = OnCreate.class, message = "Coupon type is required")
    @ValueOfEnum(enumClass = CouponType.class)
    @Schema(description = "Type of discount", example = "PERCENTAGE", allowableValues = {"PERCENTAGE", "FIXED_AMOUNT"})
    private String type;

    /**
     * Discount value (percentage or fixed amount)
     */
    @NotNull(groups = OnCreate.class, message = "Discount value is required")
    @DecimalMin(value = "0.01", message = "Discount value must be greater than zero")
    @Schema(description = "Discount value: percentage (0-100) or fixed amount", example = "20.00")
    private BigDecimal discountValue;

    /**
     * Minimum order value to apply coupon
     */
    @DecimalMin(value = "0.00", message = "Minimum order value cannot be negative")
    @Schema(description = "Minimum order value required to use the coupon", example = "100.00")
    private BigDecimal minOrderValue;

    /**
     * Maximum discount amount (cap for percentage discounts)
     */
    @DecimalMin(value = "0.00", message = "Maximum discount amount cannot be negative")
    @Schema(description = "Maximum discount cap for percentage-based coupons", example = "50.00")
    private BigDecimal maxDiscountAmount;

    /**
     * Expiry date of the coupon
     */
    @NotNull(groups = OnCreate.class, message = "Expiry date is required")
    @Schema(description = "Date when the coupon expires", example = "2026-12-31")
    private LocalDate expiryDate;

    /**
     * Total redemption limit across all customers
     */
    @Min(value = 1, message = "Total redemption limit must be at least 1")
    @Schema(description = "Total number of times the coupon can be redeemed", example = "1000")
    private Integer totalRedemptionLimit;

    /**
     * Per-customer usage limit
     */
    @Min(value = 1, message = "Per customer limit must be at least 1")
    @Schema(description = "Maximum times a single customer can use the coupon", example = "1")
    private Integer perCustomerLimit;

    /**
     * Current redemption count
     */
    @Schema(description = "Current count of successful redemptions", example = "45", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer redemptionCount;

    /**
     * Current status of the coupon
     */
    @NotNull(groups = OnCreate.class, message = "Status is required")
    @ValueOfEnum(enumClass = CouponStatus.class)
    @Schema(description = "Current status of the coupon", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "EXPIRED"})
    private String status;

    /**
     * Timestamp when the coupon was created
     */
    @Schema(description = "Creation timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdDateTime;

    /**
     * Timestamp when the coupon was last updated
     */
    @Schema(description = "Last update timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedDateTime;
}
