package com.templlo.service.user_coupon.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.templlo.service.common.config.BaseEntity;
import com.templlo.service.coupon.entity.Coupon;

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
@Table(name = "user_coupon")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserCoupon extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_coupon_id", nullable = false, updatable = false)
	private UUID userCouponId;

	@Column(name = "user_id", nullable = false)
	private UUID userId; // UUID 형태 유지

	@Column(name = "user_login_id", nullable = false, length = 50) // 추가된 필드
	private String userLoginId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_id", nullable = false)
	private Coupon coupon;

	@Column(name = "status", nullable = false, length = 20)
	private String status; // 예: ACTIVE, USED, EXPIRED

	@Column(name = "issued_at", nullable = false)
	private LocalDateTime issuedAt;

	@Column(name = "used_at")
	private LocalDateTime usedAt;

	@Column(name = "transferred_at")
	private LocalDateTime transferredAt;

	@Column(name = "from_user_id")
	private UUID fromUserId;

	@Column(name = "to_user_id")
	private UUID toUserId;

	@Builder(toBuilder = true)
	public UserCoupon(UUID userId, String userLoginId, Coupon coupon, String status, LocalDateTime issuedAt,
		LocalDateTime usedAt, LocalDateTime transferredAt, UUID fromUserId, UUID toUserId) {
		this.userId = userId;
		this.userLoginId = userLoginId; // 새 필드 초기화
		this.coupon = coupon;
		this.status = status;
		this.issuedAt = issuedAt;
		this.usedAt = usedAt;
		this.transferredAt = transferredAt;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
	}

	// 쿠폰 사용 메서드
	public void useCoupon() {
		this.status = "USED";
		this.usedAt = LocalDateTime.now();
	}

	// 쿠폰 상태 확인 메서드
	public boolean isUsed() {
		return "USED".equalsIgnoreCase(this.status);
	}

	// 쿠폰 양도 메서드
	public void transferCoupon(UUID toUserId) {
		this.status = "TRANSFERRED";
		this.transferredAt = LocalDateTime.now();
		this.toUserId = toUserId;
	}
}
