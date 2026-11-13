package ai.agilery.coupon.validation.impl;

import ai.agilery.coupon.dto.ValidationErrorDto;
import ai.agilery.coupon.enumeration.RedemptionStatus;
import ai.agilery.coupon.repository.RedemptionRepository;
import ai.agilery.coupon.validation.ValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Validator to ensure safe deletion of coupons.
 * Warns if the coupon has successful redemptions (soft validation).
 */
@Component
@RequiredArgsConstructor
public class CouponDeletionSafetyValidator implements ValidationHandler<UUID> {

    private final RedemptionRepository redemptionRepository;

    @Override
    public List<ValidationErrorDto> validate(UUID couponId) {
        List<ValidationErrorDto> errors = new ArrayList<>();

        long successfulRedemptions = redemptionRepository.countByCouponIdAndStatus(
                couponId, RedemptionStatus.SUCCESS);

        if (successfulRedemptions > 0) {
            errors.add(new ValidationErrorDto(
                    "Coupon",
                    "id",
                    "Warning: This coupon has " + successfulRedemptions +
                            " successful redemption(s). Deleting will affect historical records."
            ));
        }

        return errors;
    }
}
