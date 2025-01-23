package com.templlo.service.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OutboxPublisher {
	String eventType(); // 이벤트 타입

	String payloadExpression(); // SpEL 표현식으로 payload 정의
}
