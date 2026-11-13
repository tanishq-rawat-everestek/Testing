package ai.agilery.coupon.validation.impl;

import ai.agilery.coupon.dto.CouponDto;
import ai.agilery.coupon.dto.ValidationErrorDto;
import ai.agilery.coupon.enumeration.CouponType;
import ai.agilery.coupon.validation.ValidationHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator to ensure discount value is appropriate for coupon type.
 * For PERCENTAGE coupons: validates value is between 0 and 100.
 * For FIXED_AMOUNT coupons: validates value is positive.
 */
@Component
public class CouponDiscountValueValidator implements ValidationHandler<CouponDto> {

    @Override
    public List<ValidationErrorDto> validate(CouponDto dto) {
        List<ValidationErrorDto> errors = new ArrayList<>();

        if (dto.getType() != null && dto.getDiscountValue() != null) {
            try {
                CouponType couponType = CouponType.valueOf(dto.getType());

                if (couponType == CouponType.PERCENTAGE) {
                    if (dto.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0 ||
                            dto.getDiscountValue().compareTo(BigDecimal.valueOf(100)) > 0) {
                        errors.add(new ValidationErrorDto(
                                "CouponDto",
                                "discountValue",
                                "Percentage discount must be between 0.01 and 100"
                        ));
                    }
                } else if (couponType == CouponType.FIXED_AMOUNT) {
                    if (dto.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0) {
                        errors.add(new ValidationErrorDto(
                                "CouponDto",
                                "discountValue",
                                "Fixed amount discount must be greater than zero"
                        ));
                    }
                }
            } catch (IllegalArgumentException e) {
            }
        }

        return errors;
    }
}
