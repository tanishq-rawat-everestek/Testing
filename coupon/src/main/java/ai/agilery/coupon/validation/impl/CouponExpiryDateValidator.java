package ai.agilery.coupon.validation.impl;

import ai.agilery.coupon.dto.CouponDto;
import ai.agilery.coupon.dto.ValidationErrorDto;
import ai.agilery.coupon.validation.ValidationHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator to ensure coupon expiry date is in the future.
 * Prevents creation or update of coupons with past expiry dates.
 */
@Component
public class CouponExpiryDateValidator implements ValidationHandler<CouponDto> {

    @Override
    public List<ValidationErrorDto> validate(CouponDto dto) {
        List<ValidationErrorDto> errors = new ArrayList<>();

        if (dto.getExpiryDate() != null && dto.getExpiryDate().isBefore(LocalDate.now())) {
            errors.add(new ValidationErrorDto(
                    "CouponDto",
                    "expiryDate",
                    "Expiry date must be in the future"
            ));
        }

        return errors;
    }
}
