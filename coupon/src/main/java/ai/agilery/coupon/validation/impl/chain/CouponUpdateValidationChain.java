package ai.agilery.coupon.validation.impl.chain;

import ai.agilery.coupon.dto.CouponDto;
import ai.agilery.coupon.validation.AbstractValidationChain;
import ai.agilery.coupon.validation.impl.CouponDiscountValueValidator;
import ai.agilery.coupon.validation.impl.CouponExistenceValidator;
import ai.agilery.coupon.validation.impl.CouponExpiryDateValidator;
import org.springframework.stereotype.Component;

/**
 * Validation chain for coupon update operations.
 * Validates coupon existence, discount value rules, and expiry date.
 */
@Component
public class CouponUpdateValidationChain extends AbstractValidationChain<CouponDto> {

    public CouponUpdateValidationChain(
            CouponExistenceValidator existenceValidator,
            CouponExpiryDateValidator expiryDateValidator,
            CouponDiscountValueValidator discountValueValidator) {
        this.add(existenceValidator);
        this.add(expiryDateValidator);
        this.add(discountValueValidator);
    }
}
