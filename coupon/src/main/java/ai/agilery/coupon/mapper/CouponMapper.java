package ai.agilery.coupon.mapper;

import ai.agilery.coupon.dto.CouponDto;
import ai.agilery.coupon.entity.Coupon;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between Coupon entity and CouponDto.
 * Handles bidirectional mapping with automatic enum conversions.
 */
@Mapper(componentModel = "spring")
public interface CouponMapper extends EntityMapper<CouponDto, Coupon> {
}
