package ai.agilery.coupon.repository;

import ai.agilery.coupon.entity.Coupon;
import ai.agilery.coupon.enumeration.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Coupon entity data access operations.
 * Provides CRUD operations and custom queries for coupon management.
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID>, JpaSpecificationExecutor<Coupon> {

    /**
     * Find a coupon by its unique code.
     *
     * @param code the coupon code
     * @return Optional containing the coupon if found
     */
    Optional<Coupon> findByCode(String code);

    /**
     * Find a coupon by code and status.
     *
     * @param code the coupon code
     * @param status the coupon status
     * @return Optional containing the coupon if found
     */
    Optional<Coupon> findByCodeAndStatus(String code, CouponStatus status);

    /**
     * Check if a coupon with the given code already exists.
     *
     * @param code the coupon code
     * @return true if a coupon with the code exists
     */
    boolean existsByCode(String code);

    /**
     * Atomically increment the redemption count for a coupon.
     * This method uses optimistic locking to ensure thread-safe updates.
     *
     * @param id the coupon ID
     * @param version the current version for optimistic locking
     * @return the number of rows updated (1 if successful, 0 if coupon not found or optimistic lock failed)
     */
    @Modifying
    @Query("UPDATE Coupon c SET c.redemptionCount = c.redemptionCount + 1, c.version = c.version + 1 WHERE c.id = :id AND c.version = :version")
    int incrementRedemptionCount(@Param("id") UUID id, @Param("version") Long version);
}
