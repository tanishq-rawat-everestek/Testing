package ai.agilery.coupon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for validating a coupon against an order.
 * Contains the coupon code, order details, and customer information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to validate and apply a coupon to an order")
public class CouponValidationRequestDto {

    /**
     * Coupon code to validate
     */
    @NotBlank(message = "Coupon code is required")
    @Schema(description = "Coupon code to be validated", example = "SAVE20")
    private String couponCode;

    /**
     * Customer identifier
     */
    @NotNull(message = "Customer ID is required")
    @Schema(description = "Customer attempting to use the coupon", example = "550e8400-e29b-41d4-a716-446655440002")
    private UUID customerId;

    /**
     * Order identifier
     */
    @NotNull(message = "Order ID is required")
    @Schema(description = "Order on which coupon will be applied", example = "550e8400-e29b-41d4-a716-446655440003")
    private UUID orderId;

    /**
     * Original order amount
     */
    @NotNull(message = "Order amount is required")
    @DecimalMin(value = "0.01", message = "Order amount must be greater than zero")
    @Schema(description = "Total order amount before discount", example = "150.00")
    private BigDecimal orderAmount;
}
