package ai.agilery.coupon.validation.impl.chain;

import ai.agilery.coupon.dto.CouponValidationRequestDto;
import ai.agilery.coupon.validation.AbstractValidationChain;
import ai.agilery.coupon.validation.impl.RedemptionOrderValidator;
import org.springframework.stereotype.Component;

/**
 * Validation chain for redemption creation operations.
 * Validates that the order hasn't already been used with another coupon.
 */
@Component
public class RedemptionCreateValidationChain extends AbstractValidationChain<CouponValidationRequestDto> {

    public RedemptionCreateValidationChain(RedemptionOrderValidator orderValidator) {
        this.add(orderValidator);
    }
}
