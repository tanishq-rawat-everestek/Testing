package ai.agilery.coupon.validation.impl.service;

import ai.agilery.coupon.dto.CouponValidationRequestDto;
import ai.agilery.coupon.dto.ValidationErrorDto;
import ai.agilery.coupon.validation.impl.chain.RedemptionCreateValidationChain;
import ai.agilery.coupon.validation.util.CommonValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Semantic validation service for Redemption entity lifecycle operations.
 * Orchestrates validation for redemption recording.
 */
@Service
@RequiredArgsConstructor
public class RedemptionValidationService {

    private final CommonValidationUtil commonValidationUtil;
    private final RedemptionCreateValidationChain createChain;

    /**
     * Validate redemption request before recording.
     *
     * @param request the coupon validation request
     */
    public void onCreate(CouponValidationRequestDto request) {
        List<ValidationErrorDto> errors = createChain.validate(request);
        commonValidationUtil.throwValidationException(errors);
    }
}
