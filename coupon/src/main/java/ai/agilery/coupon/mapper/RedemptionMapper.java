package ai.agilery.coupon.mapper;

import ai.agilery.coupon.dto.RedemptionDto;
import ai.agilery.coupon.entity.Redemption;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between Redemption entity and RedemptionDto.
 * Handles bidirectional mapping with automatic enum conversions.
 */
@Mapper(componentModel = "spring")
public interface RedemptionMapper extends EntityMapper<RedemptionDto, Redemption> {
}
