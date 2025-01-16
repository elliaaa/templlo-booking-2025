package com.templlo.service.common.aop;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class RoleCheckAspect {

	@Around("@annotation(roleCheck)")
	public Object checkRole(ProceedingJoinPoint joinPoint, RoleCheck roleCheck) throws Throwable {
		String[] allowedRoles = roleCheck.allowedRoles();

		// 메서드 인자에서 role 찾기
		Object[] args = joinPoint.getArgs();
		String userRole = null;
		for (Object arg : args) {
			if (arg instanceof String role && List.of(allowedRoles).contains(role.toUpperCase())) {
				userRole = role;
				break;
			}
		}

		if (userRole == null) {
			log.warn("권한이 부족합니다. 허용된 역할: {}", List.of(allowedRoles));
			throw new IllegalArgumentException("권한이 부족합니다.");
		}

		log.info("권한 검증 성공: 역할 = {}", userRole);
		return joinPoint.proceed(); // 메서드 실행
	}
}
