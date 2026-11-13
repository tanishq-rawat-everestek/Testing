package ai.agilery.coupon.validation.impl.chain;

import ai.agilery.coupon.dto.CouponDto;
import ai.agilery.coupon.validation.AbstractValidationChain;
import ai.agilery.coupon.validation.impl.CouponCodeUniquenessValidator;
import ai.agilery.coupon.validation.impl.CouponDiscountValueValidator;
import ai.agilery.coupon.validation.impl.CouponExpiryDateValidator;
import org.springframework.stereotype.Component;

/**
 * Validation chain for coupon creation operations.
 * Validates code uniqueness, discount value rules, and expiry date.
 */
@Component
public class CouponCreateValidationChain extends AbstractValidationChain<CouponDto> {

    public CouponCreateValidationChain(
            CouponCodeUniquenessValidator codeUniquenessValidator,
            CouponExpiryDateValidator expiryDateValidator,
            CouponDiscountValueValidator discountValueValidator) {
        this.add(codeUniquenessValidator);
        this.add(expiryDateValidator);
        this.add(discountValueValidator);
    }
}
