package ai.agilery.coupon.validation.impl.service;

import ai.agilery.coupon.dto.CouponDto;
import ai.agilery.coupon.dto.ValidationErrorDto;
import ai.agilery.coupon.validation.AbstractValidationService;
import ai.agilery.coupon.validation.impl.chain.CouponCreateValidationChain;
import ai.agilery.coupon.validation.impl.chain.CouponDeleteValidationChain;
import ai.agilery.coupon.validation.impl.chain.CouponPatchValidationChain;
import ai.agilery.coupon.validation.impl.chain.CouponUpdateValidationChain;
import ai.agilery.coupon.validation.util.CommonValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Semantic validation service for Coupon entity lifecycle operations.
 * Orchestrates validation chains for create, update, patch, and delete operations.
 */
@Service
@RequiredArgsConstructor
public class CouponValidationService implements AbstractValidationService<CouponDto, UUID> {

    private final CommonValidationUtil commonValidationUtil;
    private final CouponCreateValidationChain createChain;
    private final CouponUpdateValidationChain updateChain;
    private final CouponPatchValidationChain patchChain;
    private final CouponDeleteValidationChain deleteChain;

    @Override
    public void onCreate(CouponDto dto) {
        List<ValidationErrorDto> errors = createChain.validate(dto);
        commonValidationUtil.throwValidationException(errors);
    }

    @Override
    public void onUpdate(CouponDto dto) {
        List<ValidationErrorDto> errors = updateChain.validate(dto);
        commonValidationUtil.throwValidationException(errors);
    }

    @Override
    public void onPatch(CouponDto dto) {
        List<ValidationErrorDto> errors = patchChain.validate(dto);
        commonValidationUtil.throwValidationException(errors);
    }

    public void onDelete(UUID id) {
        List<ValidationErrorDto> errors = deleteChain.validate(id);
        commonValidationUtil.throwValidationException(errors);
    }
}
