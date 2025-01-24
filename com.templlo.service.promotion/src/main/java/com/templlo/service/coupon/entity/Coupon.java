package com.templlo.service.coupon.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.templlo.service.common.config.BaseEntity;
import com.templlo.service.promotion.entity.Promotion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "coupon")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Coupon extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "coupon_id", nullable = false, updatable = false)
	private UUID couponId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "promotion_id", nullable = false)
	@JsonIgnore
	private Promotion promotion;

	@Column(name = "type", nullable = false, length = 50)
	private String type;

	@Column(name = "discount_type", nullable = true, length = 20)
	private String discountType; // PERCENTAGE, AMOUNT 등

	@Column(name = "value", precision = 10, scale = 2)
	private BigDecimal value; // 할인 금액 또는 할인율

	@Column(name = "gender", length = 10)
	private String gender;

	@Column(name = "status", nullable = false, length = 20)
	private String status;

	@Builder(toBuilder = true)
	public Coupon(UUID couponId, Promotion promotion, String type, String discountType, BigDecimal value, String gender,
		String status, String createdBy, String updatedBy) {
		this.couponId = couponId;
		this.promotion = promotion;
		this.type = type;
		this.discountType = discountType;
		this.value = value;
		this.gender = gender;
		this.status = status;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}

	// 상태 업데이트 메서드
	public void updateStatus(String newStatus) {
		this.status = newStatus;
	}

	public void checkUsable(String programType) {
		// 3. 프로그램 타입 검증
		if ("BLIND_DATE".equals(programType) && !"ADVANCED_TICKET".equals(this.type)) {
			throw new IllegalStateException("이 프로그램에서는 ADVANCED_TICKET 쿠폰만 사용할 수 있습니다.");
//			return new CouponUseResponseDto("FAILURE", "이 프로그램에서는 ADVANCED_TICKET 쿠폰만 사용할 수 있습니다.");
		}

		// 4. 쿠폰 상태 검증
		if (!"ISSUED".equals(this.status)) {
			String message = switch (this.status) {
				case "AVAILABLE" -> "쿠폰이 발급되지 않았습니다.";
				case "EXPIRED" -> "쿠폰이 만료되었습니다.";
				case "USED" -> "쿠폰이 이미 사용되었습니다.";
				default -> "알 수 없는 쿠폰 상태입니다.";
			};
			throw new IllegalStateException(message);
//			return new CouponUseResponseDto("FAILURE", message);
		}
	}
}
