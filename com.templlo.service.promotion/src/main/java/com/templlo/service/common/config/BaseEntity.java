package com.templlo.service.common.config;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@CreatedBy
	@Column(nullable = false, updatable = false)
	protected String createdBy;

	@LastModifiedDate
	@Column(nullable = false)
	protected LocalDateTime updatedAt;

	@LastModifiedBy
	@Column(nullable = false)
	protected String updatedBy;

	private LocalDateTime deletedAt;

	private String deletedBy;

	@Column(nullable = false)
	private boolean isDeleted = false;

	public void delete(String loginId) {
		isDeleted = true;
		deletedAt = LocalDateTime.now();
		deletedBy = loginId;
	}
}