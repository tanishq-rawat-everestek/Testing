package ai.agilery.coupon.validation.impl;

import ai.agilery.coupon.dto.CouponDto;
import ai.agilery.coupon.dto.ValidationErrorDto;
import ai.agilery.coupon.repository.CouponRepository;
import ai.agilery.coupon.validation.ValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator to ensure coupon exists during update operations.
 * Checks that the coupon ID references an existing coupon.
 */
@Component
@RequiredArgsConstructor
public class CouponExistenceValidator implements ValidationHandler<CouponDto> {

    private final CouponRepository couponRepository;

    @Override
    public List<ValidationErrorDto> validate(CouponDto dto) {
        List<ValidationErrorDto> errors = new ArrayList<>();

        if (dto.getId() != null && !couponRepository.existsById(dto.getId())) {
            errors.add(new ValidationErrorDto(
                    "CouponDto",
                    "id",
                    "Coupon with ID '" + dto.getId() + "' does not exist"
            ));
        }

        return errors;
    }
}
