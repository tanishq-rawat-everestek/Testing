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
 * Validator to ensure coupon code uniqueness during creation.
 * Checks that no existing coupon has the same code.
 */
@Component
@RequiredArgsConstructor
public class CouponCodeUniquenessValidator implements ValidationHandler<CouponDto> {

    private final CouponRepository couponRepository;

    @Override
    public List<ValidationErrorDto> validate(CouponDto dto) {
        List<ValidationErrorDto> errors = new ArrayList<>();

        if (dto.getCode() != null && couponRepository.existsByCode(dto.getCode())) {
            errors.add(new ValidationErrorDto(
                    "CouponDto",
                    "code",
                    "A coupon with code '" + dto.getCode() + "' already exists"
            ));
        }

        return errors;
    }
}
