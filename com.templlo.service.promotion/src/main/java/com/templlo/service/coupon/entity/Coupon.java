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
}
