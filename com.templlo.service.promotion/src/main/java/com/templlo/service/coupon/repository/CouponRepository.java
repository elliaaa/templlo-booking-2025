package com.templlo.service.coupon.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.templlo.service.coupon.entity.Coupon;
import com.templlo.service.promotion.entity.Promotion;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {

	Optional<Coupon> findFirstByPromotion_PromotionIdAndStatus(UUID promotionId, String status);

	Optional<Coupon> findFirstByPromotionAndGenderAndStatus(Promotion promotion, String gender, String status);

	Optional<Coupon> findFirstByPromotionAndStatus(Promotion promotion, String status);

	long countByPromotionAndGenderAndStatus(Promotion promotion, String gender, String status);

	boolean existsByPromotion_PromotionId(UUID promotionId);
}
