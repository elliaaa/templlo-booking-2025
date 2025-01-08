package com.templlo.service.user_coupon.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.templlo.service.user_coupon.entity.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon, UUID> {
	List<UserCoupon> findByUserId(UUID userId);

	List<UserCoupon> findByUserIdAndIsUsed(UUID userId, boolean isUsed);

	Optional<UserCoupon> findByCouponId(UUID couponId);

	@Query("SELECT uc FROM UserCoupon uc WHERE uc.userId = :userId " +
		"AND (:promotionId IS NULL OR uc.promotionId = :promotionId) " +
		"AND (:status IS NULL OR uc.status = :status)")
	Page<UserCoupon> findByUserAndPromotionAndStatus(UUID userId, UUID promotionId, String status, Pageable pageable);

	Optional<UserCoupon> findByUserIdAndCouponId(UUID userId, UUID couponId);
}
