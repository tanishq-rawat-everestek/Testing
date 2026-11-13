package ai.agilery.coupon.service;

import ai.agilery.coupon.dto.CouponDto;
import ai.agilery.coupon.dto.CouponValidationRequestDto;
import ai.agilery.coupon.dto.CouponValidationResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Coupon business operations.
 * Defines methods for CRUD operations, validation, and discount calculation.
 */
public interface CouponService {

    /**
     * Create a new coupon.
     *
     * @param couponDto the coupon data
     * @return created coupon DTO
     */
    CouponDto create(CouponDto couponDto);

    /**
     * Update an existing coupon.
     *
     * @param id the coupon identifier
     * @param couponDto the updated coupon data
     * @return updated coupon DTO
     */
    CouponDto update(UUID id, CouponDto couponDto);

    /**
     * Partially update an existing coupon.
     *
     * @param id the coupon identifier
     * @param couponDto the partial coupon data
     * @return updated coupon DTO
     */
    CouponDto patch(UUID id, CouponDto couponDto);

    /**
     * Retrieve a coupon by its identifier.
     *
     * @param id the coupon identifier
     * @return coupon DTO
     */
    CouponDto getById(UUID id);

    /**
     * Retrieve a coupon by its code.
     *
     * @param code the coupon code
     * @return coupon DTO
     */
    CouponDto getByCode(String code);

    /**
     * Retrieve all coupons.
     *
     * @return list of all coupon DTOs
     */
    List<CouponDto> getAll();

    /**
     * Delete a coupon by its identifier.
     *
     * @param id the coupon identifier
     */
    void delete(UUID id);

    /**
     * Validate a coupon and calculate discount for an order.
     *
     * @param request the validation request containing coupon code and order details
     * @return validation response with discount calculation
     */
    CouponValidationResponseDto validateAndCalculateDiscount(CouponValidationRequestDto request);
}
