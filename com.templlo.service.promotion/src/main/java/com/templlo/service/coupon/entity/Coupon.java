package com.templlo.service.coupon.entity;

import java.util.UUID;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coupon")
@Getter
@Builder(toBuilder = true) // Builder를 활성화하고 toBuilder를 허용
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID couponId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "promotion_id", nullable = false)
	private Promotion promotion;

	@Column(nullable = true)
	private String gender;

	@Column(nullable = false)
	private String status; // AVAILABLE, ISSUED, etc.

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted; // Soft Delete

	// 삭제 상태 처리
	public Coupon markAsDeleted() {
		return this.toBuilder()
			.isDeleted(true)
			.build();
	}
}
