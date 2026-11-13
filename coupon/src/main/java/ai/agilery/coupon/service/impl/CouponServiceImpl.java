package ai.agilery.coupon.service.impl;

import ai.agilery.coupon.dto.CouponDto;
import ai.agilery.coupon.dto.CouponValidationRequestDto;
import ai.agilery.coupon.dto.CouponValidationResponseDto;
import ai.agilery.coupon.entity.Coupon;
import ai.agilery.coupon.enumeration.CouponStatus;
import ai.agilery.coupon.enumeration.CouponType;
import ai.agilery.coupon.enumeration.RedemptionStatus;
import ai.agilery.coupon.exceptions.ProcessingException;
import ai.agilery.coupon.exceptions.ResourceNotFoundException;
import ai.agilery.coupon.mapper.CouponMapper;
import ai.agilery.coupon.repository.CouponRepository;
import ai.agilery.coupon.repository.RedemptionRepository;
import ai.agilery.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of CouponService for managing coupon lifecycle and validation.
 * Handles CRUD operations, discount calculations, and coupon validation logic.
 */
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private static final Logger log = LoggerFactory.getLogger(CouponServiceImpl.class);

    private final CouponRepository couponRepository;
    private final RedemptionRepository redemptionRepository;
    private final CouponMapper couponMapper;

    @Override
    @Transactional
    public CouponDto create(CouponDto couponDto) {
        log.info("Creating coupon with code: {}", couponDto.getCode());

        Coupon coupon = couponMapper.toEntity(couponDto);
        coupon.setRedemptionCount(0);

        Coupon savedCoupon = couponRepository.save(coupon);
        log.info("Coupon created successfully with ID: {}", savedCoupon.getId());

        return couponMapper.toDto(savedCoupon);
    }

    @Override
    @Transactional
    public CouponDto update(UUID id, CouponDto couponDto) {
        log.info("Updating coupon with ID: {}", id);

        Coupon existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with ID: " + id));

        Coupon updatedCoupon = couponMapper.toEntity(couponDto);
        updatedCoupon.setId(existingCoupon.getId());
        updatedCoupon.setRedemptionCount(existingCoupon.getRedemptionCount());
        updatedCoupon.setCreatedDateTime(existingCoupon.getCreatedDateTime());

        Coupon savedCoupon = couponRepository.save(updatedCoupon);
        log.info("Coupon updated successfully with ID: {}", savedCoupon.getId());

        return couponMapper.toDto(savedCoupon);
    }

    @Override
    @Transactional
    public CouponDto patch(UUID id, CouponDto couponDto) {
        log.info("Patching coupon with ID: {}", id);

        Coupon existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with ID: " + id));

        if (couponDto.getCode() != null) {
            existingCoupon.setCode(couponDto.getCode());
        }
        if (couponDto.getDescription() != null) {
            existingCoupon.setDescription(couponDto.getDescription());
        }
        if (couponDto.getType() != null) {
            existingCoupon.setType(CouponType.valueOf(couponDto.getType()));
        }
        if (couponDto.getDiscountValue() != null) {
            existingCoupon.setDiscountValue(couponDto.getDiscountValue());
        }
        if (couponDto.getMinOrderValue() != null) {
            existingCoupon.setMinOrderValue(couponDto.getMinOrderValue());
        }
        if (couponDto.getMaxDiscountAmount() != null) {
            existingCoupon.setMaxDiscountAmount(couponDto.getMaxDiscountAmount());
        }
        if (couponDto.getExpiryDate() != null) {
            existingCoupon.setExpiryDate(couponDto.getExpiryDate());
        }
        if (couponDto.getTotalRedemptionLimit() != null) {
            existingCoupon.setTotalRedemptionLimit(couponDto.getTotalRedemptionLimit());
        }
        if (couponDto.getPerCustomerLimit() != null) {
            existingCoupon.setPerCustomerLimit(couponDto.getPerCustomerLimit());
        }
        if (couponDto.getStatus() != null) {
            existingCoupon.setStatus(CouponStatus.valueOf(couponDto.getStatus()));
        }

        Coupon savedCoupon = couponRepository.save(existingCoupon);
        log.info("Coupon patched successfully with ID: {}", savedCoupon.getId());

        return couponMapper.toDto(savedCoupon);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponDto getById(UUID id) {
        log.info("Retrieving coupon with ID: {}", id);

        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with ID: " + id));

        return couponMapper.toDto(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponDto getByCode(String code) {
        log.info("Retrieving coupon with code: {}", code);

        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with code: " + code));

        return couponMapper.toDto(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponDto> getAll() {
        log.info("Retrieving all coupons");

        return couponRepository.findAll().stream()
                .map(couponMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        log.info("Deleting coupon with ID: {}", id);

        if (!couponRepository.existsById(id)) {
            throw new ResourceNotFoundException("Coupon not found with ID: " + id);
        }

        couponRepository.deleteById(id);
        log.info("Coupon deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponValidationResponseDto validateAndCalculateDiscount(CouponValidationRequestDto request) {
        log.info("Validating coupon: {} for customer: {} on order: {}",
                request.getCouponCode(), request.getCustomerId(), request.getOrderId());

        Coupon coupon = couponRepository.findByCode(request.getCouponCode())
                .orElse(null);

        if (coupon == null) {
            return buildFailureResponse(request, "Coupon code is invalid");
        }

        if (coupon.getStatus() != CouponStatus.ACTIVE) {
            return buildFailureResponse(request, "Coupon is not active");
        }

        if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
            return buildFailureResponse(request, "Coupon has expired");
        }

        if (coupon.getMinOrderValue() != null &&
                request.getOrderAmount().compareTo(coupon.getMinOrderValue()) < 0) {
            return buildFailureResponse(request,
                    "Order amount must be at least " + coupon.getMinOrderValue());
        }

        if (coupon.getTotalRedemptionLimit() != null &&
                coupon.getRedemptionCount() >= coupon.getTotalRedemptionLimit()) {
            return buildFailureResponse(request, "Coupon redemption limit has been reached");
        }

        if (coupon.getPerCustomerLimit() != null) {
            long customerUsageCount = redemptionRepository.countByCustomerIdAndCouponIdAndStatus(
                    request.getCustomerId(), coupon.getId(), RedemptionStatus.SUCCESS);
            if (customerUsageCount >= coupon.getPerCustomerLimit()) {
                return buildFailureResponse(request, "You have reached the usage limit for this coupon");
            }
        }

        BigDecimal discountAmount = calculateDiscount(coupon, request.getOrderAmount());
        BigDecimal finalAmount = request.getOrderAmount().subtract(discountAmount);

        log.info("Coupon validation successful. Discount: {}, Final Amount: {}", discountAmount, finalAmount);

        return CouponValidationResponseDto.builder()
                .valid(true)
                .message("Coupon applied successfully")
                .couponId(coupon.getId())
                .couponCode(coupon.getCode())
                .orderAmount(request.getOrderAmount())
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .build();
    }

    /**
     * Calculate discount amount based on coupon type and rules.
     *
     * @param coupon the coupon entity
     * @param orderAmount the order amount
     * @return calculated discount amount
     */
    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal orderAmount) {
        BigDecimal discount;

        if (coupon.getType() == CouponType.PERCENTAGE) {
            discount = orderAmount.multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            if (coupon.getMaxDiscountAmount() != null &&
                    discount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
                discount = coupon.getMaxDiscountAmount();
            }
        } else {
            discount = coupon.getDiscountValue();
        }

        if (discount.compareTo(orderAmount) > 0) {
            discount = orderAmount;
        }

        return discount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Build a failure response for invalid coupon validation.
     *
     * @param request the validation request
     * @param message the failure message
     * @return validation response indicating failure
     */
    private CouponValidationResponseDto buildFailureResponse(CouponValidationRequestDto request, String message) {
        log.warn("Coupon validation failed: {}", message);

        return CouponValidationResponseDto.builder()
                .valid(false)
                .message(message)
                .couponCode(request.getCouponCode())
                .orderAmount(request.getOrderAmount())
                .discountAmount(BigDecimal.ZERO)
                .finalAmount(request.getOrderAmount())
                .build();
    }
}
