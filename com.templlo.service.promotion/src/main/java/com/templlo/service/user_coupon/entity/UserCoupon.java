package com.templlo.service.user_coupon.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_coupon")
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_coupon_id")
	private UUID userCouponId;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "coupon_id", nullable = false)
	private UUID couponId;

	@Column(name = "promotion_id", nullable = false)
	private UUID promotionId;

	@Column(name = "issued_at", nullable = false)
	private LocalDateTime issuedAt;

	@Column(name = "is_used", nullable = false)
	private boolean isUsed;

	@Column(name = "used_at")
	private LocalDateTime usedAt;

	@Column(name = "from_user_id")
	private UUID fromUserId; // 양도자 ID

	@Column(name = "transfer_date")
	private LocalDateTime transferDate; // 양도 날짜

	@Column(name = "status", nullable = false)
	private String status;

	public UserCoupon markAsUsed() {
		return this.toBuilder()
			.isUsed(true)
			.usedAt(LocalDateTime.now())
			.status("USED")
			.build();
	}

	public UserCoupon transferToUser(UUID toUserId) {
		return this.toBuilder()
			.fromUserId(this.userId) // 현재 사용자 ID를 양도자 ID로 설정
			.userId(toUserId) // 새 사용자 ID 설정
			.transferDate(LocalDateTime.now()) // 양도 날짜 설정
			.status("TRANSFERRED") // 상태를 양도된 상태로 설정
			.build();
	}

	public UserCoupon markAsExpired() {
		return this.toBuilder()
			.status("EXPIRED")
			.build();
	}
}
