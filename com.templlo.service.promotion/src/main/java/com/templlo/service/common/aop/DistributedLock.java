package com.templlo.service.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
	String key(); // SpEL 표현식으로 락 키를 설정

	long waitTime() default 10; // 락 획득 대기 시간 (초)

	long leaseTime() default 5; // 락 유지 시간 (초)
}
