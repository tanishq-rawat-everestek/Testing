package ai.agilery.coupon.validation.impl.chain;

import ai.agilery.coupon.dto.CouponDto;
import ai.agilery.coupon.validation.AbstractValidationChain;
import ai.agilery.coupon.validation.impl.CouponDiscountValueValidator;
import ai.agilery.coupon.validation.impl.CouponExpiryDateValidator;
import org.springframework.stereotype.Component;

/**
 * Validation chain for coupon patch operations.
 * Validates discount value rules and expiry date if provided.
 */
@Component
public class CouponPatchValidationChain extends AbstractValidationChain<CouponDto> {

    public CouponPatchValidationChain(
            CouponExpiryDateValidator expiryDateValidator,
            CouponDiscountValueValidator discountValueValidator) {
        this.add(expiryDateValidator);
        this.add(discountValueValidator);
    }
}
