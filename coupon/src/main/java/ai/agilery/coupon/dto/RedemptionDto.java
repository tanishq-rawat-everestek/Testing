package ai.agilery.coupon.dto;

import ai.agilery.coupon.enumeration.RedemptionStatus;
import ai.agilery.coupon.enumeration.ValueOfEnum;
import ai.agilery.coupon.validation.OnCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Redemption entity.
 * Represents a coupon redemption transaction record.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Redemption data transfer object")
public class RedemptionDto {

    /**
     * Unique identifier for the redemption
     */
    @Null(groups = OnCreate.class, message = "ID must be null when creating a redemption")
    @Schema(description = "Unique identifier of the redemption", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID id;

    /**
     * Coupon identifier
     */
    @NotNull(groups = OnCreate.class, message = "Coupon ID is required")
    @Schema(description = "Reference to the coupon being redeemed", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID couponId;

    /**
     * Customer identifier
     */
    @NotNull(groups = OnCreate.class, message = "Customer ID is required")
    @Schema(description = "Reference to the customer redeeming the coupon", example = "550e8400-e29b-41d4-a716-446655440002")
    private UUID customerId;

    /**
     * Order identifier
     */
    @NotNull(groups = OnCreate.class, message = "Order ID is required")
    @Schema(description = "Reference to the order on which coupon is applied", example = "550e8400-e29b-41d4-a716-446655440003")
    private UUID orderId;

    /**
     * Original order amount
     */
    @NotNull(groups = OnCreate.class, message = "Order amount is required")
    @DecimalMin(value = "0.01", message = "Order amount must be greater than zero")
    @Schema(description = "Original order amount before discount", example = "150.00")
    private BigDecimal orderAmount;

    /**
     * Calculated discount amount
     */
    @Schema(description = "Discount amount applied", example = "30.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal discountAmount;

    /**
     * Final payable amount
     */
    @Schema(description = "Final amount after discount", example = "120.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal finalAmount;

    /**
     * Redemption status
     */
    @ValueOfEnum(enumClass = RedemptionStatus.class)
    @Schema(description = "Status of the redemption", example = "SUCCESS", allowableValues = {"SUCCESS", "FAILED"})
    private String status;

    /**
     * Failure reason if redemption failed
     */
    @Size(max = 500, message = "Failure reason must not exceed 500 characters")
    @Schema(description = "Error message if redemption failed", example = "Coupon has expired")
    private String failureReason;

    /**
     * Redemption timestamp
     */
    @Schema(description = "Timestamp when redemption occurred", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime redemptionDateTime;
}
