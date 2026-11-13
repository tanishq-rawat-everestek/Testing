package ai.agilery.coupon.repository;

import ai.agilery.coupon.entity.Redemption;
import ai.agilery.coupon.enumeration.RedemptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Redemption entity data access operations.
 * Provides CRUD operations and custom queries for redemption history management.
 */
@Repository
public interface RedemptionRepository extends JpaRepository<Redemption, UUID>, JpaSpecificationExecutor<Redemption> {

    /**
     * Find all redemptions for a specific customer.
     *
     * @param customerId the customer identifier
     * @return list of redemptions for the customer
     */
    List<Redemption> findByCustomerId(UUID customerId);

    /**
     * Find all redemptions for a specific coupon.
     *
     * @param couponId the coupon identifier
     * @return list of redemptions for the coupon
     */
    List<Redemption> findByCouponId(UUID couponId);

    /**
     * Count successful redemptions for a specific coupon.
     *
     * @param couponId the coupon identifier
     * @param status the redemption status
     * @return count of successful redemptions
     */
    long countByCouponIdAndStatus(UUID couponId, RedemptionStatus status);

    /**
     * Count successful redemptions by a specific customer for a specific coupon.
     *
     * @param customerId the customer identifier
     * @param couponId the coupon identifier
     * @param status the redemption status
     * @return count of customer's successful redemptions for the coupon
     */
    long countByCustomerIdAndCouponIdAndStatus(UUID customerId, UUID couponId, RedemptionStatus status);

    /**
     * Find all redemptions for a specific order.
     *
     * @param orderId the order identifier
     * @return list of redemptions for the order
     */
    List<Redemption> findByOrderId(UUID orderId);
}
