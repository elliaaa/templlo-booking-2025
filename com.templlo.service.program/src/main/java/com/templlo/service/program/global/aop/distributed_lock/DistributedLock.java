package com.templlo.service.program.global.aop.distributed_lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    /**
     * key 유형
     */
    DistributedLockKey keyType();

    /**
     * 구체적인 식별자를 표현하는 Spring Expression Language
     */
    String idSpEL() default "";

    /**
     * 락 획득을 위해 기다릴 최대 시간
     */
    long maxWaitTime() default 5L;

    /**
     * 락을 소유하고 있을 최대 시간
     * : 이 시간이 지나면 자동으로 락을 해제(반납)한다
     */
    long maxLeaseTime() default 3L;
}
