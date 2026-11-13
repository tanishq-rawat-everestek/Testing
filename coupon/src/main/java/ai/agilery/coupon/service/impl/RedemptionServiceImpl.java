package ai.agilery.coupon.service.impl;

import ai.agilery.coupon.dto.CouponValidationRequestDto;
import ai.agilery.coupon.dto.CouponValidationResponseDto;
import ai.agilery.coupon.dto.RedemptionDto;
import ai.agilery.coupon.entity.Coupon;
import ai.agilery.coupon.entity.Redemption;
import ai.agilery.coupon.enumeration.RedemptionStatus;
import ai.agilery.coupon.exceptions.ProcessingException;
import ai.agilery.coupon.exceptions.ResourceNotFoundException;
import ai.agilery.coupon.mapper.RedemptionMapper;
import ai.agilery.coupon.repository.CouponRepository;
import ai.agilery.coupon.repository.RedemptionRepository;
import ai.agilery.coupon.service.CouponService;
import ai.agilery.coupon.service.RedemptionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of RedemptionService for managing redemption records.
 * Handles recording redemptions and retrieving redemption history.
 */
@Service
@RequiredArgsConstructor
public class RedemptionServiceImpl implements RedemptionService {

    private static final Logger log = LoggerFactory.getLogger(RedemptionServiceImpl.class);

    private final RedemptionRepository redemptionRepository;
    private final CouponRepository couponRepository;
    private final RedemptionMapper redemptionMapper;
    private final CouponService couponService;

    @Override
    @Transactional
    public RedemptionDto recordRedemption(CouponValidationRequestDto request) {
        log.info("Recording redemption for coupon: {} by customer: {} on order: {}",
                request.getCouponCode(), request.getCustomerId(), request.getOrderId());

        CouponValidationResponseDto validationResponse = couponService.validateAndCalculateDiscount(request);

        Redemption redemption = Redemption.builder()
                .couponId(validationResponse.getCouponId())
                .customerId(request.getCustomerId())
                .orderId(request.getOrderId())
                .orderAmount(request.getOrderAmount())
                .discountAmount(validationResponse.getDiscountAmount())
                .finalAmount(validationResponse.getFinalAmount())
                .status(validationResponse.getValid() ? RedemptionStatus.SUCCESS : RedemptionStatus.FAILED)
                .failureReason(validationResponse.getValid() ? null : validationResponse.getMessage())
                .build();

        if (validationResponse.getValid()) {
            // Fetch the coupon to get current version for optimistic locking
            Coupon coupon = couponRepository.findById(validationResponse.getCouponId())
                    .orElseThrow(() -> new ProcessingException(
                            "Coupon not found with ID: " + validationResponse.getCouponId()));

            int updated = couponRepository.incrementRedemptionCount(
                    validationResponse.getCouponId(), coupon.getVersion());
            if (updated == 0) {
                throw new ProcessingException(
                        "Failed to increment redemption count for coupon ID: " + validationResponse.getCouponId()
                        + ". Possible concurrent modification detected.");
            }
            log.info("Incremented redemption count for coupon ID: {}", validationResponse.getCouponId());
        }

        Redemption savedRedemption = redemptionRepository.save(redemption);
        log.info("Redemption recorded successfully with ID: {} and status: {}",
                savedRedemption.getId(), savedRedemption.getStatus());

        return redemptionMapper.toDto(savedRedemption);
    }

    @Override
    @Transactional(readOnly = true)
    public RedemptionDto getById(UUID id) {
        log.info("Retrieving redemption with ID: {}", id);

        Redemption redemption = redemptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Redemption not found with ID: " + id));

        return redemptionMapper.toDto(redemption);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RedemptionDto> getByCustomerId(UUID customerId) {
        log.info("Retrieving redemptions for customer ID: {}", customerId);

        return redemptionRepository.findByCustomerId(customerId).stream()
                .map(redemptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RedemptionDto> getByCouponId(UUID couponId) {
        log.info("Retrieving redemptions for coupon ID: {}", couponId);

        return redemptionRepository.findByCouponId(couponId).stream()
                .map(redemptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RedemptionDto> getByOrderId(UUID orderId) {
        log.info("Retrieving redemptions for order ID: {}", orderId);

        return redemptionRepository.findByOrderId(orderId).stream()
                .map(redemptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RedemptionDto> getAll() {
        log.info("Retrieving all redemptions");

        return redemptionRepository.findAll().stream()
                .map(redemptionMapper::toDto)
                .collect(Collectors.toList());
    }
}
