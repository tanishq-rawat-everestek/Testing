package ai.agilery.coupon.validation.impl;

import ai.agilery.coupon.dto.CouponValidationRequestDto;
import ai.agilery.coupon.dto.ValidationErrorDto;
import ai.agilery.coupon.entity.Redemption;
import ai.agilery.coupon.enumeration.RedemptionStatus;
import ai.agilery.coupon.repository.RedemptionRepository;
import ai.agilery.coupon.validation.ValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator to ensure an order hasn't already had a coupon successfully applied.
 * Prevents multiple coupon redemptions on the same order.
 */
@Component
@RequiredArgsConstructor
public class RedemptionOrderValidator implements ValidationHandler<CouponValidationRequestDto> {

    private final RedemptionRepository redemptionRepository;

    @Override
    public List<ValidationErrorDto> validate(CouponValidationRequestDto request) {
        List<ValidationErrorDto> errors = new ArrayList<>();

        if (request.getOrderId() != null) {
            List<Redemption> existingRedemptions = redemptionRepository.findByOrderId(request.getOrderId());

            boolean hasSuccessfulRedemption = existingRedemptions.stream()
                    .anyMatch(r -> r.getStatus() == RedemptionStatus.SUCCESS);

            if (hasSuccessfulRedemption) {
                errors.add(new ValidationErrorDto(
                        "CouponValidationRequestDto",
                        "orderId",
                        "This order has already been used with another coupon"
                ));
            }
        }

        return errors;
    }
}
