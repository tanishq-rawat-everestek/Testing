package ai.agilery.coupon.validation.impl.chain;

import ai.agilery.coupon.validation.AbstractValidationChain;
import ai.agilery.coupon.validation.impl.CouponDeletionSafetyValidator;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Validation chain for coupon deletion operations.
 * Validates that the coupon can be safely deleted.
 */
@Component
public class CouponDeleteValidationChain extends AbstractValidationChain<UUID> {

    public CouponDeleteValidationChain(CouponDeletionSafetyValidator deletionSafetyValidator) {
        this.add(deletionSafetyValidator);
    }
}
