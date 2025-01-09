package com.templlo.service.user_coupon.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.templlo.service.user_coupon.entity.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon, UUID> {

	// 특정 쿠폰 ID로 UserCoupon 조회
	@Query("SELECT uc FROM UserCoupon uc WHERE uc.coupon.couponId = :couponId")
	Optional<UserCoupon> findByCouponId(@Param("couponId") UUID couponId);

	// 특정 사용자의 모든 쿠폰 조회 (페이징)
	@Query("SELECT uc FROM UserCoupon uc WHERE uc.userId = :userId")
	Page<UserCoupon> findByUserId(@Param("userId") UUID userId, Pageable pageable);

	// 특정 사용자의 특정 프로모션 쿠폰 조회 (페이징)
	@Query("SELECT uc FROM UserCoupon uc WHERE uc.userId = :userId AND uc.coupon.promotion.promotionId = :promotionId")
	Page<UserCoupon> findByUserIdAndPromotionId(@Param("userId") UUID userId, @Param("promotionId") UUID promotionId,
		Pageable pageable);

	// 특정 사용자의 특정 상태 쿠폰 조회 (페이징)
	@Query("SELECT uc FROM UserCoupon uc WHERE uc.userId = :userId AND uc.status = :status")
	Page<UserCoupon> findByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") String status,
		Pageable pageable);

	// 특정 사용자의 특정 프로모션 및 상태 쿠폰 조회 (페이징)
	@Query("SELECT uc FROM UserCoupon uc WHERE uc.userId = :userId AND uc.coupon.promotion.promotionId = :promotionId AND uc.status = :status")
	Page<UserCoupon> findByUserIdAndPromotionIdAndStatus(
		@Param("userId") UUID userId,
		@Param("promotionId") UUID promotionId,
		@Param("status") String status,
		Pageable pageable
	);

	@Query("SELECT uc FROM UserCoupon uc WHERE uc.userId = :userId AND uc.coupon.couponId = :couponId")
	Optional<UserCoupon> findByUserIdAndCouponId(
		@Param("userId") UUID userId,
		@Param("couponId") UUID couponId
	);

	// 특정 사용자가 특정 프로모션에서 이미 쿠폰을 발급받았는지 확인
	boolean existsByUserIdAndCoupon_Promotion_PromotionId(UUID userId, UUID promotionId);

	@Query("SELECT uc FROM UserCoupon uc WHERE uc.userLoginId = :loginId " +
		"AND (:promotionId IS NULL OR uc.coupon.promotion.promotionId = :promotionId) " +
		"AND (:status IS NULL OR uc.status = :status)")
	Page<UserCoupon> findByUserLoginIdAndPromotionIdAndStatus(
		@Param("loginId") String loginId,
		@Param("promotionId") UUID promotionId,
		@Param("status") String status,
		Pageable pageable
	);

	@Query("SELECT uc FROM UserCoupon uc WHERE uc.userLoginId = :loginId " +
		"AND (:promotionId IS NULL OR uc.coupon.promotion.promotionId = :promotionId)")
	Page<UserCoupon> findByUserLoginIdAndPromotionId(
		@Param("loginId") String loginId,
		@Param("promotionId") UUID promotionId,
		Pageable pageable
	);

	@Query("SELECT uc FROM UserCoupon uc WHERE uc.userLoginId = :loginId " +
		"AND (:status IS NULL OR uc.status = :status)")
	Page<UserCoupon> findByUserLoginIdAndStatus(
		@Param("loginId") String loginId,
		@Param("status") String status,
		Pageable pageable
	);

	@Query("SELECT uc FROM UserCoupon uc WHERE uc.userLoginId = :loginId")
	Page<UserCoupon> findByUserLoginId(
		@Param("loginId") String loginId,
		Pageable pageable
	);

}
