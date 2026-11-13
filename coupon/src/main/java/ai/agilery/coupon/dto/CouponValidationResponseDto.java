package ai.agilery.coupon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Response DTO for coupon validation results.
 * Contains validation status, discount calculations, and final payable amount.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing coupon validation results and discount calculation")
public class CouponValidationResponseDto {

    /**
     * Validation success indicator
     */
    @Schema(description = "Indicates if the coupon is valid and can be applied", example = "true")
    private Boolean valid;

    /**
     * Validation or error message
     */
    @Schema(description = "Message describing validation result or error", example = "Coupon applied successfully")
    private String message;

    /**
     * Coupon identifier
     */
    @Schema(description = "Unique identifier of the validated coupon", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID couponId;

    /**
     * Coupon code
     */
    @Schema(description = "Coupon code validated", example = "SAVE20")
    private String couponCode;

    /**
     * Original order amount
     */
    @Schema(description = "Original order amount before discount", example = "150.00")
    private BigDecimal orderAmount;

    /**
     * Calculated discount amount
     */
    @Schema(description = "Discount amount to be applied", example = "30.00")
    private BigDecimal discountAmount;

    /**
     * Final payable amount after discount
     */
    @Schema(description = "Final amount customer needs to pay", example = "120.00")
    private BigDecimal finalAmount;
}
