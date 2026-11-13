package ai.agilery.coupon.service;

import ai.agilery.coupon.dto.CouponValidationRequestDto;
import ai.agilery.coupon.dto.RedemptionDto;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Redemption business operations.
 * Manages coupon redemption recording and history tracking.
 */
public interface RedemptionService {

    /**
     * Record a coupon redemption (successful or failed).
     *
     * @param request the validation request containing redemption details
     * @return recorded redemption DTO
     */
    RedemptionDto recordRedemption(CouponValidationRequestDto request);

    /**
     * Retrieve a redemption by its identifier.
     *
     * @param id the redemption identifier
     * @return redemption DTO
     */
    RedemptionDto getById(UUID id);

    /**
     * Retrieve all redemptions for a specific customer.
     *
     * @param customerId the customer identifier
     * @return list of redemption DTOs for the customer
     */
    List<RedemptionDto> getByCustomerId(UUID customerId);

    /**
     * Retrieve all redemptions for a specific coupon.
     *
     * @param couponId the coupon identifier
     * @return list of redemption DTOs for the coupon
     */
    List<RedemptionDto> getByCouponId(UUID couponId);

    /**
     * Retrieve all redemptions for a specific order.
     *
     * @param orderId the order identifier
     * @return list of redemption DTOs for the order
     */
    List<RedemptionDto> getByOrderId(UUID orderId);

    /**
     * Retrieve all redemptions.
     *
     * @return list of all redemption DTOs
     */
    List<RedemptionDto> getAll();
}
