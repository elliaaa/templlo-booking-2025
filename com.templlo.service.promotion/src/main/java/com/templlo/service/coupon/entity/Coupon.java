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
	@GeneratedValue(strategy = GenerationType.UUID) // UUID 전략 사용
	@Column(name = "coupon_id", nullable = false, updatable = false)
	private UUID couponId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "promotion_id", nullable = false) // 외래 키 설정
	@JsonIgnore
	private Promotion promotion;

	@Column(name = "type", nullable = false, length = 50)
	private String type; // 예: DISCOUNT, GIFT 등

	@Column(name = "value", precision = 10, scale = 2)
	private BigDecimal value; // 할인 금액 또는 할인율 (null 가능)

	@Column(name = "gender", length = 10)
	private String gender; // MALE, FEMALE 또는 NULL(성별 구분 없음)

	@Column(name = "status", nullable = false, length = 20)
	private String status; // AVAILABLE, ISSUED, EXPIRED 등

	@Builder(toBuilder = true)
	public Coupon(UUID couponId, Promotion promotion, String type, BigDecimal value, String gender, String status) {
		this.couponId = couponId;
		this.promotion = promotion;
		this.type = type;
		this.value = value;
		this.gender = gender;
		this.status = status;
	}

	// 상태 업데이트 메서드
	public void updateStatus(String newStatus) {
		this.status = newStatus;
	}
}
